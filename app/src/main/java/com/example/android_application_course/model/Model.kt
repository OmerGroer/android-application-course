package com.example.android_application_course.model

class Model private constructor() {

    val students: MutableList<Student> = ArrayList()

    companion object {
        val shared = Model()
    }

    fun get(index: Int): Student {
        return students.get(index)
    }

    fun remove(index: Int) {
        students.removeAt(index)
    }

    fun createOrUpdate(index: Int?, student: Student) {
        if (index == null) {
            students.add(student)
        } else {
            students[index] = student
        }
    }
}