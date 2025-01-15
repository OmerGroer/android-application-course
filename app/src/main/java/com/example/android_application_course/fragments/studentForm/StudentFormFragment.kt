package com.example.android_application_course.fragments.studentForm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android_application_course.R
import com.example.android_application_course.databinding.FragmentStudentFormBinding
import com.example.android_application_course.views.DatePickerFragment
import com.example.android_application_course.views.OnDateSetListener
import com.example.android_application_course.views.OnTimeSetListener
import com.example.android_application_course.views.TimePickerFragment
import com.example.android_application_course.views.dateString
import com.example.android_application_course.views.timeString
import com.example.tabletalk.utils.BasicAlert
import com.yalantis.ucrop.UCrop

class StudentFormFragment : Fragment(), OnTimeSetListener, OnDateSetListener {
    private val args: StudentFormFragmentArgs? by navArgs()
    private val viewModel: StudentFormViewModel by viewModels()
    private var binding: FragmentStudentFormBinding? = null

    private val imagePicker: ActivityResultLauncher<String> = getImagePicker()
    private val uCropLauncher: ActivityResultLauncher<Intent> = getUCropLauncher()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_student_form, container, false
        )
        bindViews()

        val studentId = args?.studentId
        if (studentId != null) {
            viewModel.initForm(studentId)
            initiateForm()
        } else {
            binding?.studentFormDeleteButton?.visibility = View.GONE
        }

        binding?.studentFormSaveButton?.setOnClickListener(::onSaveButton)
        binding?.studentFormCancelButton?.setOnClickListener(::onCancelButton)
        binding?.studentFormDeleteButton?.setOnClickListener(::onDeleteButton)

        setupImagePicker()

        binding?.studentFormBirthTimeEditText?.setOnClickListener {
            TimePickerFragment.newInstance(viewModel.birthHour, viewModel.birthMinute).setOnTimeSet(this).show(getChildFragmentManager(), "timePicker")
        }
        binding?.studentFormBirthDateEditText?.setOnClickListener {
            DatePickerFragment.newInstance(viewModel.birthYear, viewModel.birthMonth, viewModel.birthDay).setOnDateSet(this).show(getChildFragmentManager(), "datePicker")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showProgressBar()
            else hideProgressBar()
        }

        return binding?.root
    }

    private fun bindViews() {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initiateForm() {
        viewModel.studentData.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            onTimeSet(it.birthHour, it.birthMinute)
            onDateSet(it.birthYear, it.birthMonth, it.birthDay)
        }
    }

    private fun onSaveButton(view: View) {
        viewModel.submit({
            BasicAlert("Success", "Student saved successfully", requireContext()).show()
            findNavController().popBackStack()
        }, { message ->
            BasicAlert("Invalid Form", message, requireContext()).show()
        }, { error ->
            BasicAlert("Fail", "Failed to save student", requireContext()).show()
            hideProgressBar()
        })
    }

    private fun onDeleteButton(view: View) {
        viewModel.delete({
            BasicAlert("Success", "Student deleted successfully", requireContext()).show()
            findNavController().popBackStack()
        }, { error ->
            BasicAlert("Fail", "Failed to delete student", requireContext()).show()
            hideProgressBar()
        })
    }

    private fun onCancelButton(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    override fun onTimeSet(hourOfDay: Int, minute: Int) {
        viewModel.birthHour = hourOfDay
        viewModel.birthMinute = minute
        binding?.studentFormBirthTimeEditText?.setText(timeString(hourOfDay, minute))
    }

    override fun onDateSet(year: Int, month: Int, day: Int) {
        viewModel.birthDay = day
        viewModel.birthMonth = month
        viewModel.birthYear = year
        binding?.studentFormBirthDateEditText?.setText(dateString(year, month, day))
    }
    private fun hideProgressBar() {
        binding?.studentFormSaveButton?.visibility = View.VISIBLE
        binding?.studentFormCancelButton?.visibility = View.VISIBLE
        if (args?.studentId != null) {
            binding?.studentFormDeleteButton?.visibility = View.VISIBLE
        }
        binding?.progressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.studentFormSaveButton?.visibility = View.INVISIBLE
        binding?.studentFormCancelButton?.visibility = View.INVISIBLE
        binding?.studentFormDeleteButton?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun setupImagePicker() {
        binding?.imageView?.setOnClickListener {
            imagePicker.launch("image/*")
        }
        viewModel.avatar.observe(viewLifecycleOwner) { uri ->
            if (uri != "") binding?.imageView?.setImageURI(uri.toUri())
        }
    }

    private fun getImagePicker() =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding?.imageView?.getImagePicker(uri, uCropLauncher)
        }

    private fun getUCropLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = UCrop.getOutput(result.data!!)
                binding?.imageView?.setImageURI(uri)
                viewModel.avatar.value = uri.toString()
            }
        }
}