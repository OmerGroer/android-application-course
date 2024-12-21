package com.example.android_application_course.model

class Model private constructor() {

    val students: MutableList<Student> = ArrayList()

    companion object {
        val shared = Model()
    }

    init {
        for (i in 0..20) {
            val student = Student(
                name = "Ben Shapiro $i",
                id = i.toString(),
                phone = "054$i",
                address = "there $i",
                avatarUrl = "",
                isChecked = false
            )
            students.add(student)
        }
    }
}