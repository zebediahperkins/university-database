const mongoose = require('mongoose');

module.exports = mongoose.model('courses', new mongoose.Schema({
    title: String,
    credits: Number,
    prerequisites: [],
    eligible: String
}));