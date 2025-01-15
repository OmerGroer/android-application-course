package com.example.android_application_course.fragments.studentDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_application_course.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentDetailsViewModel(private val studentId: String) : ViewModel() {
    val student = StudentRepository.getInstance().getByIdLiveData(studentId)
    val isLoading = MutableLiveData(false)

    fun fetchStudent() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                StudentRepository.getInstance().refreshById(studentId)
            } catch (e: Exception) {
                Log.e("StudentDetails", "Error fetching student", e)
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }
}