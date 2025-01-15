package com.example.android_application_course.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.android_application_course.R
import com.example.android_application_course.data.repository.UserRepository
import com.example.android_application_course.databinding.FragmentLoginBinding
import com.example.tabletalk.utils.BasicAlert
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        bindViews()

        if (UserRepository.getInstance().isLogged()) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToStudentsListFragment())
        }

        binding?.loginButton?.setOnClickListener {
            showProgressBar()
            viewModel.login { error -> onLoginFailure(error) }
        }

        return binding?.root
    }

    private fun bindViews() {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
    }

    private fun onLoginFailure(error: Exception?) {
        UserRepository.getInstance().logout()

        if (error != null) {
            Log.e("Login", "Error signing in user", error)
            when (error) {
                is FirebaseAuthInvalidUserException -> {
                    viewModel.register { innerError -> onLoginFailure(innerError) }
                }
                is FirebaseAuthInvalidCredentialsException -> {
                    BasicAlert(
                        "Login Error",
                        "Email or password is incorrect",
                        requireContext()
                    ).show()
                }
                is FirebaseAuthUserCollisionException -> {
                    BasicAlert(
                        "Register Error",
                        "User with this email already exists",
                        requireContext()
                    ).show()
                }
                else -> {
                    BasicAlert("Login Error", "An error occurred", requireContext()).show()
                }
            }
        }

        showLoginButton()
    }

    private fun showLoginButton() {
        binding?.loginButton?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.loginButton?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}