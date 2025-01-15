package com.example.android_application_course.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_application_course.MyApplication
import com.example.android_application_course.data.local.dao.ImageDao
import com.example.android_application_course.data.local.dao.StudentDao
import com.example.android_application_course.data.model.Image
import com.example.android_application_course.data.model.Student

@Database(
    entities = [Student::class, Image::class],
    version = 1
)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun imageDao(): ImageDao
}

object AppLocalDb {
    private val database: AppLocalDbRepository by lazy {
        Room.databaseBuilder(
            context = MyApplication.context,
            klass = AppLocalDbRepository::class.java,
            name = "dbFileName.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getInstance(): AppLocalDbRepository {
        return database
    }
}