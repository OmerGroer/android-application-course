package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AddNewStudentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_student, container, false)

        getChildFragmentManager().beginTransaction()
            .replace(R.id.container, StudentFormFragment.newInstance())
            .commitNow()

        return view
    }
}