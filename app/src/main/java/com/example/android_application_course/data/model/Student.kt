package com.example.android_application_course.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "students")
data class Student(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var idNumber: String = "",
    var phone: String = "",
    var address: String = "",
    var avatarUrl: String = "",
    var isChecked: Boolean = false,
    val birthHour: Int = 0,
    val birthMinute: Int = 0,
    val birthDay: Int = 0,
    val birthMonth: Int = 0,
    val birthYear: Int = 0,
    var lastUpdated: Long? = null
) {
    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val ID_NUMBER = "idNumber"
        const val PHONE = "phone"
        const val ADDRESS = "address"
        const val AVATAR_URL = "avatarUrl"
        const val IS_CHECKED = "isChecked"
        const val BIRTH_HOUR = "birthHour"
        const val BIRTH_MINUTE = "birthMinute"
        const val BIRTH_DAY = "birthDay"
        const val BIRTH_MONTH = "birthMonth"
        const val BIRTH_YEAR = "birthYear"
        const val LAST_UPDATED = "lastUpdated"

        fun fromJSON(json: Map<String, Any>): Student {
            val id = json[ID] as? String ?: ""
            val name = json[NAME] as? String ?: ""
            val idNumber = json[ID_NUMBER] as? String ?: ""
            val phone = json[PHONE] as? String ?: ""
            val address = json[ADDRESS] as? String ?: ""
            val avatarUrl = json[AVATAR_URL] as? String ?: ""
            val isChecked = json[IS_CHECKED] as? Boolean ?: false
            val birthHour = json[BIRTH_HOUR] as? Int ?: (json[BIRTH_HOUR] as? Long)?.toInt() ?: 0
            val birthMinute = json[BIRTH_MINUTE] as? Int ?: (json[BIRTH_MINUTE] as? Long)?.toInt() ?: 0
            val birthDay = json[BIRTH_DAY] as? Int ?: (json[BIRTH_DAY] as? Long)?.toInt() ?: 0
            val birthMonth = json[BIRTH_MONTH] as? Int ?: (json[BIRTH_MONTH] as? Long)?.toInt() ?: 0
            val birthYear = json[BIRTH_YEAR] as? Int ?: (json[BIRTH_YEAR] as? Long)?.toInt() ?: 0
            val lastUpdated = (json[LAST_UPDATED] as? Timestamp ?: Timestamp(0,0)).seconds

            return Student(
                id = id,
                name = name,
                idNumber = idNumber,
                phone = phone,
                address = address,
                avatarUrl = avatarUrl,
                isChecked = isChecked,
                birthHour = birthHour,
                birthMinute = birthMinute,
                birthDay = birthDay,
                birthMonth = birthMonth,
                birthYear = birthYear,
                lastUpdated = lastUpdated
            )
        }
    }
}