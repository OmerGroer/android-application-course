package com.example.android_application_course.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_application_course.data.model.Student

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg student: Student)

    @Query("SELECT * FROM students WHERE id = :studentId")
    fun getByIdLiveData(studentId: String): LiveData<Student>

    @Query("SELECT * FROM students WHERE id = :studentId")
    fun getById(studentId: String): Student?

    @Query("SELECT * FROM students ORDER BY lastUpdated")
    fun getAll(): LiveData<List<Student>>

    @Query("DELETE FROM students WHERE id = :studentId")
    fun delete(studentId: String)
}