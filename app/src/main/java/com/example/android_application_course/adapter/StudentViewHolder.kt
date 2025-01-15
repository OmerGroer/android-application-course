package com.example.android_application_course.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_application_course.fragments.studentsList.OnItemClickListener
import com.example.android_application_course.R
import com.example.android_application_course.data.model.Student
import com.example.android_application_course.fragments.studentsList.StudentsListViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

class StudentViewHolder(
    itemView: View,
    listener: OnItemClickListener?,
    private val viewModel: StudentsListViewModel
) : RecyclerView.ViewHolder(itemView) {

    private var nameTextView: TextView = itemView.findViewById(R.id.student_row_name_text_view)
    private var idTextView: TextView = itemView.findViewById(R.id.student_row_id_text_view)
    private var studentCheckBox: CheckBox = itemView.findViewById(R.id.student_row_check_box)
    private var avatar: ImageView = itemView.findViewById(R.id.student_row_image_view)
    private var progressBar: CircularProgressIndicator = itemView.findViewById(R.id.progress_bar)
    private var student: Student? = null

    init {
        studentCheckBox.apply {
            setOnClickListener {
                (tag as? Int)?.let { _ ->
                    val student = student ?: return@setOnClickListener
                    viewModel.checkStudent(student.id, (it as? CheckBox)?.isChecked ?: false) {
                    }
                }
            }

            itemView.setOnClickListener {
                val student = student ?: return@setOnClickListener
                listener?.onItemClick(student)
            }
        }
    }

    fun bind(student: Student?, position: Int) {
        this.student = student
        nameTextView.text = student?.name
        idTextView.text = student?.idNumber

        studentCheckBox.apply {
            isChecked = student?.isChecked ?: false
            tag = position
        }

        val studentId = student?.id ?: return
        viewModel.getImageUrl(studentId) {
            Glide.with(itemView.context)
                .load(it)
                .into(avatar)
            progressBar.visibility = View.GONE
        }
    }
}