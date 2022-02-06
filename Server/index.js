const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const jsonParser = bodyParser.json();
const crypto = require('crypto');
const mongoose = require('mongoose');
const Student = require('./Student');
const Course = require('./Course');
const bcrypt = require('bcrypt');

mongoose.connect(process.env.DB_URL || 'mongodb://127.0.0.1:27017/university');

app.post('/admin/register-student', jsonParser, (req, res) => { //Expects x-api-key in header and student info in body
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            if (!student.admin) res.send(`Error: User ${student.username} may not access this content`);
            else {
                const username = req.body.username;
                const password = req.body.password;
                Student.findOne({ username: username }, (_err, student) => {
                    if (student) res.send(`Student ${student.username} already exists`);
                    else {
                        bcrypt.hash(password, 10, (_err, hash) => {
                            new Student({
                                username: username,
                                password: hash,
                                apiKey: '',
                                academics: {
                                    major: req.body.major,
                                    courses: [],
                                    completedCourses: [],
                                    holds: [],
                                    credits: 0,
                                    totalCredits: 0
                                },
                                finances: {
                                    totalBill: 0.00,
                                    financialAid: 0.00
                                },
                                personalInfo: {
                                    firstName: req.body.firstName,
                                    lastName: req.body.lastName,
                                    address: req.body.address
                                }
                            }).save();
                        });
                        res.send(`Succcess! Student ${username} was registered!`);
                    }
                });
            }
        }
        else res.send('Error: Invalid API key');
    });
});

app.post('/admin/register-course', jsonParser, (req, res) => { //Expects x-api-key in header and course info in body
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            if (!student.admin) res.send(`Error: User ${student.username} may not access this content`);
            else {
                Course.findOne({ title: req.body.title }, (_err, course) => {
                    if (course) res.send(`Error: Course ${req.body.title} already exists in the courses directory!`);
                    else {
                        new Course({
                            title: req.body.title,
                            credits: req.body.credits,
                            prerequisites: req.body.prerequisites,
                            eligible: req.body.eligible
                        }).save();
                        res.send(`Success! ${req.body.title} has been added to the courses directory!`);
                    }
                });
            }
        }
        else res.send('Error: Invalid API key');
    });
});

app.post('/admin/update-student', jsonParser, (req, res) => { //Expects x-api-key in header and whatever student info will be changed in body
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            if (!student.admin) res.send(`Error: User ${student.username} may not access this content`);
            else {
                const username = req.body.username;
                const password = req.body.password;
                if (!username) res.send('Error: Please specify student username');
                else {
                    Student.findOne({ username: username }, (_err, student) => {
                        if (!student) res.send(`Student ${username} does not exist!`);
                        else {
                            if (req.body.username) student.username = req.body.username;
                            if (password) {
                                bcrypt.hash(password, 10, (_err, hash) => {
                                    student.password = hash;
                                });
                            }
                            if (req.body.major) student.academics.major = req.body.major;
                            if (req.body.coursesToEnroll) student.academics.courses.push(req.body.coursesToEnroll);
                            if (req.body.completedCourses) student.academics.push(completedCourses);
                            if (req.body.holds) student.academics.holds.push(req.body.holds);
                            if (req.body.clearHolds) student.academics.holds = [];
                            if (req.body.enrolledCredits) student.academics.credits = req.body.enrolledCredits;
                            if (req.body.totalCredits) student.academics.totalCredits = req.body.totalCredits;
                            if (req.body.bill) student.finances.totalBill = req.body.bill;
                            if (req.body.financialAid) student.finances.financialAid = req.body.financialAid;
                            if (req.body.firstName) student.personalInfo.firstName = req.body.firstName;
                            if (req.body.lastName) student.personalInfo.lastName = req.body.lastName;
                            if (req.body.address) student.personalInfo.address = req.body.address;
                            student.save();
                            res.send(`Succcess! Student ${username} was updated!`);
                        }
                    });
                }
            }
        }
        else res.send('Error: Invalid API key');
    });
});

app.post('/login', jsonParser, (req, res) => { //Expects username and password in body
    const username = req.body.username;
    const password = req.body.password;
    Student.findOne({ username: username }, (_err, student) => {
        if (student) {
            bcrypt.compare(password, student.password, (_err, valid) => {
                if (valid) {
                    const apiKey = crypto.randomBytes(20).toString('hex');
                    student.apiKey = crypto.createHash('sha256').update(apiKey).digest('hex');
                    student.save();
                    res.json({ apiKey: apiKey });
                }
                else res.json({ apiKey: '' });
            });
        }
        else res.json({ apiKey: '' });
    });
});

app.post('/logout', (req, res) => { //Expects x-api-key in header
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            student.apiKey = '';
            student.save();
            res.send(`Success! Student ${student.username} has been logged out`);
        }
        else res.send('Error: Invalid API key');
    });
});

app.post('/reset-password', jsonParser, (req, res) => { //Expects x-api-key in header and password in body
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    const password = req.body.password;
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            bcrypt.hash(password, 10, (_err, hash) => {
                student.password = hash;
                student.save();
                res.send(`Success! Student ${student.username}'s password has been updated`);
            });
        }
        else res.send('Error: Invalid API key');
    });
});

app.get('/profile', (req, res) => { //Expects x-api-key in header
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            if (student.admin) {
                res.json({
                    username: student.username,
                    admin: student.admin
                });
            }
            else {
                res.json({
                    username: student.username,
                    academics: student.academics,
                    finances: student.finances,
                    personalInfo: student.personalInfo
                });
            }

        }
        else res.send('Error: Invalid API key');
    });
});

app.get('/view-courses', (req, res) => { //Expects x-api-key in header
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            Course.find({}, (err, courses) => {
                if (!req.query.eligible) res.json(courses);
                else res.json(courses.filter(course => course.eligible === req.query.eligible));
            });
        }
        else res.send('Error: Invalid API key');
    });
});

app.post('/add-course', jsonParser, (req, res) => { //Expects x-api-key in header and title in body
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            if (student.academics.courses.includes(req.body.title)) res.send(`Error: Student ${student.username} already registered for course ${req.body.title}`);
            else if (student.academics.holds.length > 0) res.send(`Error: Student has ${student.academics.holds.length} registration hold(s)!`);
            else if (student.academics.courses.length >= 5) res.send('Error: Student has already enrolled in max amount of courses!');
            else {
                Course.findOne({ title: req.body.title }, (_err, course) => {
                    if (!course) res.send(`Error: Course ${req.body.title} doesn't exist in the courses directory!`);
                    else if (course.eligible !== student.academics.major && course.eligible != 'Any') res.send(`Error: Student's major is ${student.academics.major}. This course is only available to students with the major ${course.eligible}.`);
                    else if (course.prerequisites.reduce((acc, e) => acc && student.academics.completedCourses.includes(e), true)) {
                        student.academics.courses.push(course.title);
                        student.academics.credits += course.credits;
                        student.academics.totalCredits += course.credits;
                        student.finances.totalBill += 445.73 * course.credits;
                        student.save();
                        res.send(`Success! Student ${student.username} has been registered in course ${course.title}`);
                    }
                    else res.send(`Error: Student has not satisfied all prerequisites for ${req.body.title}!`);
                });
            }
        }
        else res.send('Error: Invalid API key');
    });
});

app.post('/drop-course', jsonParser, (req, res) => { //Expects x-api-key in header and title in body
    const apiKeyHash = crypto.createHash('sha256').update(req.header('x-api-key')).digest('hex');
    Student.findOne({ apiKey: apiKeyHash }, (_err, student) => {
        if (student) {
            if (!student.academics.courses.includes(req.body.title)) res.send(`Error: Student ${student.username} is not registered for course ${req.body.title}`);
            else {
                Course.findOne({ title: req.body.title }, (_err, course) => {
                    if (!course) res.send(`Error: Course ${req.body.title} doesn't exist in the courses directory!`);
                    student.academics.courses = student.academics.courses.filter(item => item !== course.title);
                    student.academics.credits -= course.credits;
                    student.academics.totalCredits -= course.credits;
                    student.finances.totalBill -= 445.73 * course.credits;
                    student.save();
                    res.send(`Success! Student ${student.username} has dropped course ${course.title}`);
                });
            }
        }
        else res.send('Error: Invalid API key');
    });
});

app.listen(process.env.PORT || 3000);