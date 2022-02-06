const mongoose = require('mongoose');

module.exports = mongoose.model('student', new mongoose.Schema({
    username: String,
    password: String,
    apiKey: String,
    admin: Boolean,
    academics: {
        major: String,
        courses: [],
        completedCourses: [],
        holds: [],
        credits: Number,
        totalCredits: Number
    },
    finances: {
        totalBill: Number,
        financialAid: Number
    },
    personalInfo: {
        firstName: String,
        lastName: String,
        address: String
    }
}));