package com.example.android_application_course.fragments.studentForm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_application_course.data.model.Student
import com.example.android_application_course.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val phoneRegex = "^[\\d-]+$".toRegex()
val idRegex = "^\\d+$".toRegex()

class StudentFormViewModel : ViewModel() {
    val name = MutableLiveData("")
    val id = MutableLiveData("")
    val phone = MutableLiveData("")
    val address = MutableLiveData("")
    val checked = MutableLiveData(false)
    val avatar = MutableLiveData("")
    val studentData: MutableLiveData<Student> = MutableLiveData(null)

    var birthHour: Int? = null
    var birthMinute: Int? = null
    var birthDay: Int? = null
    var birthMonth: Int? = null
    var birthYear: Int? = null

    var studentId: String? = null
    val isLoading = MutableLiveData(false)

    fun initForm(studentId: String) {
        isLoading.value = true
        this.studentId = studentId

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val student = StudentRepository.getInstance().getById(studentId) ?: throw Exception("Student not found")
                withContext(Dispatchers.Main) {
                    name.value = student.name
                    id.value = student.idNumber
                    phone.value = student.phone
                    address.value = student.address
                    checked.value = student.isChecked
                    avatar.value = student.avatarUrl
                    birthDay = student.birthDay
                    birthMonth = student.birthMonth
                    birthYear = student.birthYear
                    birthHour = student.birthHour
                    birthMinute = student.birthMinute
                    studentData.value = student
                }
            } catch (e: Exception) {
                Log.e("StudentForm", "Error fetching student", e)
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    fun delete(onSuccess: () -> Unit, onFailure: (error: Exception?) -> Unit) {
        try {
            val studentId = studentId ?: return

            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    StudentRepository.getInstance().delete(studentId)

                    withContext(Dispatchers.Main) { onSuccess() }
                } catch (e: Exception) {
                    Log.e("StudentForm", "Error deleting student", e)
                    withContext(Dispatchers.Main) { onFailure(e) }
                } finally {
                    withContext(Dispatchers.Main) { isLoading.value = false }
                }
            }
        } catch (e: Exception) {
            Log.e("StudentForm", "Error deleting student", e)
            onFailure(e)
        }
    }

    fun submit(onSuccess: () -> Unit, onInvalid: (message: String) -> Unit, onFailure: (error: Exception?) -> Unit) {
        val errorMessage = validateForm()

        if (errorMessage != null) {
            onInvalid(errorMessage)
            return
        }

        try {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val student = getStudent()
                    StudentRepository.getInstance().save(student)

                    withContext(Dispatchers.Main) { onSuccess() }
                } catch (e: Exception) {
                    Log.e("StudentForm", "Error adding student", e)
                    withContext(Dispatchers.Main) { onFailure(e) }
                } finally {
                    withContext(Dispatchers.Main) { isLoading.value = false }
                }
            }
        } catch (e: Exception) {
            Log.e("StudentForm", "Error adding student", e)
            onFailure(e)
        }
    }

    private fun validateForm() : String? {
        if (name.value?.isNotEmpty() != true) {
            return "Name is required"
        }
        val id = id.value
        if (id?.isNotEmpty() != true) {
            return "ID is required"
        }
        if (!idRegex.matches(id)) {
            return "ID can be only digits"
        }
        val phone = phone.value
        if (phone?.isNotEmpty() != true) {
            return "Phone is required"
        }
        if (!phoneRegex.matches(phone)) {
            return "Phone can be only digits"
        }
        if (address.value?.isNotEmpty() != true) {
            return "Address is required"
        }
        if (birthDay == null) {
            return "Birth date is required"
        }
        if (birthHour == null) {
            return "Birth time is required"
        }
        if (avatar.value?.isNotEmpty() != true) {
            return "Avatar is required"
        }

        return null
    }

    private fun getStudent(): Student {
        val name = name.value ?: throw Exception("Name is required")
        val id = id.value ?: throw Exception("ID is required")
        val phone = phone.value ?: throw Exception("Phone is required")
        val address = address.value ?: throw Exception("Address is required")
        val checked = checked.value ?: throw Exception("Checked is required")
        val avatar = avatar.value ?: throw Exception("Avatar is required")
        val birthDay = birthDay ?: throw Exception("Birth date is required")
        val birthHour = birthHour ?: throw Exception("Birth time is required")
        val birthMinute = birthMinute ?: throw Exception("Birth time is required")
        val birthMonth = birthMonth ?: throw Exception("Birth date is required")
        val birthYear = birthYear ?: throw Exception("Birth date is required")

        return Student(
            id = studentId ?: "",
            name = name,
            idNumber = id,
            phone = phone,
            address = address,
            isChecked = checked,
            avatarUrl = avatar.let {
                if (!it.startsWith("file:///")) "file://$it" else it
            },
            birthDay = birthDay,
            birthHour = birthHour,
            birthMinute = birthMinute,
            birthMonth = birthMonth,
            birthYear = birthYear
        )
    }
}