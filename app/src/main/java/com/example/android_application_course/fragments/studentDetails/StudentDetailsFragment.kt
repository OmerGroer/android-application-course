package com.example.android_application_course.fragments.studentDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.android_application_course.R
import com.example.android_application_course.databinding.FragmentStudentDetailsBinding
import com.example.android_application_course.views.dateString
import com.example.android_application_course.views.timeString

class StudentDetailsFragment : Fragment() {
    private val args: StudentDetailsFragmentArgs by navArgs()
    private var viewModel: StudentDetailsViewModel? = null
    private var binding: FragmentStudentDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_student_details, container, false
        )
        viewModel = StudentDetailsViewModel(args.studentId)
        bindViews()

        viewModel?.fetchStudent()
        setupStudentDetails()

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel?.fetchStudent()
        }

        viewModel?.isLoading?.observe(viewLifecycleOwner) {
            binding?.swipeRefreshLayout?.isRefreshing = it
        }

        binding?.swipeRefreshLayout?.visibility = View.GONE

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_student_menu) {
            val action = StudentDetailsFragmentDirections.actionStudentDetailsFragmentToStudentFormFragment(args.studentId)
            findNavController().navigate(action)
            return true
        }

        return false
    }

    private fun setupStudentDetails() {
        viewModel?.student?.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().popBackStack()
                return@observe
            }
            binding?.swipeRefreshLayout?.visibility = View.VISIBLE

            binding?.studentDetailsName?.text = "Name: ${it.name}"
            binding?.studentDetailsId?.text = "ID: ${it.idNumber}"
            binding?.studentDetailsPhone?.text = "Phone: ${it.phone}"
            binding?.studentDetailsAddress?.text = "Address: ${it.address}"
            binding?.studentDetailsBirthDate?.text = "Birth Date: ${dateString(it.birthYear, it.birthMonth, it.birthDay)}"
            binding?.studentDetailsBirthTime?.text = "Birth Time: ${timeString(it.birthHour, it.birthMinute)}"

            if (it.isChecked) {
                binding?.studentDetailsCheckBox?.isChecked = true
                binding?.studentDetailsChecked?.text = "Checked"
            } else {
                binding?.studentDetailsCheckBox?.isChecked = false
                binding?.studentDetailsChecked?.text = "Not Checked"
            }

            val context = context ?: return@observe
            val avatar = binding?.studentDetailsAvatarImageView ?: return@observe
            Glide.with(context)
                .load(it?.avatarUrl)
                .into(avatar)
        }
    }
}