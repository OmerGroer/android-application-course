package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class EditStudentFragment : Fragment(), OnSaveListener {
    private var studentPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_student, container, false)

        studentPosition = EditStudentFragmentArgs.fromBundle(requireArguments()).studentPosition

        getChildFragmentManager().beginTransaction()
            .replace(R.id.container, StudentFormFragment.newInstance(studentPosition as Int).setOnSave(this))
            .commitNow()

        return view
    }

    override fun onSave(view: View) {
        val action = EditStudentFragmentDirections.actionEditStudentFragmentToStudentDetailsFragment(studentPosition as Int)
        Navigation.findNavController(view).navigate(action)
    }
}