package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_application_course.adapter.StudentsRecyclerAdapter
import com.example.android_application_course.model.Model
import com.example.android_application_course.model.Student

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class StudentsListFragment : Fragment() {
    var students: MutableList<Student>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_students_list, container, false)

        students = Model.shared.students

        val recyclerView: RecyclerView = view.findViewById(R.id.students_recycler_view)
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val adapter = StudentsRecyclerAdapter(students)

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val action =
                    StudentsListFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(
                        position
                    )
                Navigation.findNavController(view).navigate(action)
            }
        }
        recyclerView.adapter = adapter

        val addStudentBtn: Button = view.findViewById(R.id.list_add_student_button)
        addStudentBtn.setOnClickListener(
            Navigation.createNavigateOnClickListener(StudentsListFragmentDirections.actionStudentsListFragmentToAddNewStudentFragment())
        )

        return view
    }
}