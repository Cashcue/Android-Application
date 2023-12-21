package com.cashcue.ui.main.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.cashcue.R
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.databinding.FragmentSettingsBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.MainViewModel
import com.cashcue.ui.main.changepass.ChangePassActivity
import com.cashcue.ui.main.edit.EditProfileActivity

class SettingsFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val settingsViewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            setProfile(user)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnLanguage.setOnClickListener{
            setupAction()
        }

        binding.btnEdit.setOnClickListener{
            editProfile()
        }

        binding.btnPassword.setOnClickListener {
            newPassword()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setProfile(user: UserModel) {
        Glide.with(requireContext()).load(user.fotoUrl).into(binding.ivProfile)
        binding.tvName.text = user.nama
        binding.tvEmail.text = user.email
    }

    private fun logout() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.are_u_sure))
            setPositiveButton(getString(R.string.logout)) { _, _ ->
                settingsViewModel.logout()
                Toast.makeText(requireContext(),
                    getString(R.string.logout_successfully), Toast.LENGTH_LONG).show()
            }
            setNegativeButton(getString(R.string.text_negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun setupAction() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun editProfile(){
        val intent = Intent (activity, EditProfileActivity::class.java)
        activity?.startActivity(intent)
    }

    private fun newPassword(){
        val intent = Intent (activity, ChangePassActivity::class.java)
        activity?.startActivity(intent)
    }



}
