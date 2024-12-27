package com.example.android_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_application_course.adapter.StudentsRecyclerAdapter
import com.example.android_application_course.model.Model
import com.example.android_application_course.model.Student

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class StudentsListActivity : AppCompatActivity() {

    var students: MutableList<Student>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Students List"

        students = Model.shared.students

        val recyclerView: RecyclerView = findViewById(R.id.students_recycler_view)
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = StudentsRecyclerAdapter(students)

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                adapter.notifyDataSetChanged()
            }
        }

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, StudentDetailsActivity::class.java)
                intent.putExtra("studentPosition", position)
                startForResult.launch(intent)
            }
        }
        recyclerView.adapter = adapter

        val addStudentBtn: Button = findViewById(R.id.list_add_student_button)
        addStudentBtn.setOnClickListener {
            val intent = Intent(applicationContext, AddNewStudentActivity::class.java)
            startForResult.launch(intent)
        }
    }
}