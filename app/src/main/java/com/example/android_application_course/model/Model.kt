package com.example.android_application_course.model

class Model private constructor() {

    val students: MutableList<Student> = ArrayList()

    companion object {
        val shared = Model()
    }

    fun get(index: Int): Student {
        return students.get(index)
    }

    fun add(student: Student) {
        students.add(student)
    }

    fun remove(index: Int) {
        students.removeAt(index)
    }
}