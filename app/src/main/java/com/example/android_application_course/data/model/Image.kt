package com.example.android_application_course.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image(
    @PrimaryKey
    val id: String = "",
    val uri: String = ""
)
