package com.example.android_application_course.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.example.android_application_course.MyApplication
import com.example.android_application_course.data.local.AppLocalDb
import com.example.android_application_course.data.model.Student
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentRepository {
    companion object {
        private const val COLLECTION = "students"
        private const val LAST_UPDATED = "studentsLastUpdated"

        private val studentRepository = StudentRepository()

        fun getInstance(): StudentRepository {
            return studentRepository
        }
    }

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val imageRepository = ImageRepository(COLLECTION)

    suspend fun save(student: Student) {
        val documentRef = if (student.id.isNotEmpty())
            db.collection(COLLECTION).document(student.id)
        else
            db.collection(COLLECTION).document().also { student.id = it.id }

        db.runBatch { batch ->
            batch.set(documentRef, student)
            batch.update(documentRef, Student.AVATAR_URL, null)
            batch.update(documentRef, Student.LAST_UPDATED, FieldValue.serverTimestamp())
        }.await()
        uploadImage(student.avatarUrl, student.id)
    }

    suspend fun checkStudent(studentId: String, isChecked: Boolean) {
        val documentRef = db.collection(COLLECTION).document(studentId)

        db.runBatch { batch ->
            batch.update(documentRef, Student.IS_CHECKED, isChecked)
            batch.update(documentRef, Student.LAST_UPDATED, FieldValue.serverTimestamp())
        }.await()
    }

    suspend fun delete(studentId: String) {
        db.collection(COLLECTION).document(studentId).delete().await()
        AppLocalDb.getInstance().studentDao().delete(studentId)
        imageRepository.delete(studentId)
    }

    private suspend fun uploadImage(imageUri: String, reviewId: String) {
        imageRepository.upload(imageUri.toUri(), reviewId)
    }

    suspend fun getImagePath(imageId: String): String {
        return imageRepository.getImagePathById(imageId)
    }

    fun getAll(): LiveData<List<Student>> {
        return AppLocalDb.getInstance().studentDao().getAll()
    }

    fun getByIdLiveData(studentId: String): LiveData<Student> {
        return AppLocalDb.getInstance().studentDao().getByIdLiveData(studentId)
    }

    suspend fun getById(studentId: String): Student? {
        var student = AppLocalDb.getInstance().studentDao().getById(studentId)

        if (student == null) {
            student = db.collection(COLLECTION)
                .document(studentId)
                .get()
                .await().let { document -> document.data?.let { Student.fromJSON(it).apply { id = document.id } } }
            student?.avatarUrl = imageRepository.downloadAndCacheImage(imageRepository.getImageRemoteUri(studentId), studentId)

            if (student == null) return null

            AppLocalDb.getInstance().studentDao().insertAll(student)
        }

        return student.apply { avatarUrl = imageRepository.getImagePathById(studentId) }
    }

    suspend fun refreshById(studentId: String) {
        val student = db.collection(COLLECTION)
            .document(studentId)
            .get()
            .await().let { document ->
                document.data?.let {
                    Student.fromJSON(it).apply { id = document.id }
                }
            }

        if (student == null) return

        student.avatarUrl = imageRepository.downloadAndCacheImage(
            imageRepository.getImageRemoteUri(student.id),
            student.id
        )

        AppLocalDb.getInstance().studentDao().insertAll(student)
    }

    suspend fun refresh() {
        var time: Long = getLastUpdate()

        val students = db.collection(COLLECTION)
            .whereGreaterThanOrEqualTo(Student.LAST_UPDATED, Timestamp(time, 0))
            .get().await().documents.map { document ->
                document.data?.let {
                    Student.fromJSON(it).apply { id = document.id }
                }
            }

        for (student in students) {
            if (student == null) continue

            imageRepository.deleteLocal(student.id)
            AppLocalDb.getInstance().studentDao().insertAll(student)
            val lastUpdated = student.lastUpdated
            if (lastUpdated != null && lastUpdated > time) {
                time = lastUpdated
            }
        }

        setLastUpdate(time)
    }

    private fun getLastUpdate(): Long {
        val sharedPef: SharedPreferences =
            MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
        return sharedPef.getLong(LAST_UPDATED, 0)
    }

    private fun setLastUpdate(time: Long) {
        MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
            .edit().putLong(LAST_UPDATED, time).apply()
    }
}