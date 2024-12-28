package com.example.android_application_course.model

data class Student(
    var name: String,
    var id: String,
    var phone: String,
    var address: String,
    var avatarUrl: String,
    var isChecked: Boolean,
    val birthHour: Int,
    val birthMinute: Int,
    val birthDay: Int,
    val birthMonth: Int,
    val birthYear: Int
)