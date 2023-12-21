package com.cashcue.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cashcue.R
import com.cashcue.data.Result
import com.cashcue.databinding.FragmentLoginBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.MainActivity

class LoginFragment : Fragment() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogin.setOnClickListener {
            login()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        loginViewModel.login(email, password).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        showAlert(true, result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showAlert(false, result.message)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.edlEmail.isEnabled = false
            binding.edlPassword.isEnabled = false
            binding.btnLogin.isEnabled = false
            binding.pbLogin.visibility = View.VISIBLE
        } else {
            binding.pbLogin.visibility = View.GONE
            binding.edlEmail.isEnabled = true
            binding.edlPassword.isEnabled = true
            binding.btnLogin.isEnabled = true
        }
    }

    private fun showAlert(isSuccess: Boolean, message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(message)
            setPositiveButton(getString(R.string.text_positive_button)) { dialog, _ ->
                if (isSuccess) {
                    val intentMain = Intent(requireContext(), MainActivity::class.java)
                    intentMain.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intentMain)
                    activity?.finish()
                } else {
                    dialog.dismiss()
                }
            }
            setCancelable(false)
            create()
            show()
        }
    }
}