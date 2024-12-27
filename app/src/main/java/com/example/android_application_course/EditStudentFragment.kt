package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class EditStudentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_student, container, false)

        val studentPosition = EditStudentFragmentArgs.fromBundle(requireArguments()).studentPosition
        getChildFragmentManager().beginTransaction()
            .replace(R.id.container, StudentFormFragment.newInstance(studentPosition))
            .commitNow()

        return view
    }
}