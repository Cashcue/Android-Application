package com.cashcue.ui.auth.register

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
import com.cashcue.databinding.FragmentRegisterBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.MainActivity

class RegisterFragment : Fragment() {

    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnRegister.setOnClickListener {
            register()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun register() {
        val nama = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        registerViewModel.register(nama, email, password).observe(viewLifecycleOwner) { result ->
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
            binding.edlName.isEnabled = false
            binding.edlEmail.isEnabled = false
            binding.edlPassword.isEnabled = false
            binding.btnRegister.isEnabled = false
            binding.pbRegister.visibility = View.VISIBLE
        } else {
            binding.pbRegister.visibility = View.GONE
            binding.edlName.isEnabled = true
            binding.edlEmail.isEnabled = true
            binding.edlPassword.isEnabled = true
            binding.btnRegister.isEnabled = true
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