package com.example.android_application_course.fragments.studentsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_application_course.R
import com.example.android_application_course.adapter.StudentsRecyclerAdapter
import com.example.android_application_course.data.model.Student
import com.example.android_application_course.databinding.FragmentStudentsListBinding

interface OnItemClickListener {
    fun onItemClick(student: Student)
}

class StudentsListFragment : Fragment() {
    private val viewModel: StudentsListViewModel by viewModels()
    private var binding: FragmentStudentsListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_students_list, container, false
        )
        bindViews()

        setupList()
        viewModel.fetchStudents()

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel.fetchStudents()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.swipeRefreshLayout?.isRefreshing = it
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupList() {
        binding?.studentsRecyclerView?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        binding?.studentsRecyclerView?.layoutManager = layoutManager
        val adapter = StudentsRecyclerAdapter(emptyList(), viewModel)
        binding?.studentsRecyclerView?.adapter = adapter

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(student: Student) {
                val action = StudentsListFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(student.id)
                findNavController().navigate(action)
            }
        }

        viewModel.students.observe(viewLifecycleOwner) {
            adapter.updateStudents(it)
        }
    }
}