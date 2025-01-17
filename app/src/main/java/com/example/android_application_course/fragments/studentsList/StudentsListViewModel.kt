package com.example.android_application_course.fragments.studentsList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_application_course.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentsListViewModel : ViewModel() {
    val students = StudentRepository.getInstance().getAll()
    val isLoading = MutableLiveData(false)

    fun fetchStudents() {
        isLoading.value = true
        fetchStudentHelper()
    }

    private fun fetchStudentHelper() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                StudentRepository.getInstance().refresh()
            } catch (e: Exception) {
                Log.e("StudentsList", "Error fetching students", e)
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    fun checkStudent(studentId: String, isChecked: Boolean, onFailure: (error: Exception?) -> Unit) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                StudentRepository.getInstance().checkStudent(studentId, isChecked)
                fetchStudentHelper()
            } catch (e: Exception) {
                Log.e("StudentsList", "Error checking student", e)
                onFailure(e)
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    fun getImageUrl(studentId: String, onCompleted: (imageUrl: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = StudentRepository.getInstance().getImagePath(studentId)
                withContext(Dispatchers.Main) { onCompleted(imageUrl) }
            } catch (e: Exception) {
                Log.e("StudentsList", "Error loading image", e)
            }
        }
    }
}