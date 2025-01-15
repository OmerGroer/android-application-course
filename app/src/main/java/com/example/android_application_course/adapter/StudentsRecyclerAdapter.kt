package com.example.android_application_course.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_application_course.fragments.studentsList.OnItemClickListener
import com.example.android_application_course.R
import com.example.android_application_course.data.model.Student
import com.example.android_application_course.fragments.studentsList.StudentsListViewModel

class StudentsRecyclerAdapter(private var students: List<Student>, private val viewModel: StudentsListViewModel): RecyclerView.Adapter<StudentViewHolder>() {

    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = students.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.student_row,
            parent,
            false
        )
        return StudentViewHolder(itemView, listener, viewModel)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(
            student = students.get(position),
            position = position
        )
    }

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}