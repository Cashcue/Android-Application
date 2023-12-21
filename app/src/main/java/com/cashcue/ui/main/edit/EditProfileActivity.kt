package com.cashcue.ui.main.edit

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cashcue.R
import com.cashcue.data.Result
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.databinding.ActivityEditProfileBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.MainViewModel

class EditProfileActivity : AppCompatActivity() {

    private val editProfileViewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityEditProfileBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.edit_profile)

        editProfileViewModel.getSession().observe(this) {
            setProfile(it)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.sendButton.setOnClickListener { send() }

        fun allPermissionsGranted() =
            ContextCompat.checkSelfPermission(
                this,
                REQUIRED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
                }
            }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

    }

    private fun setProfile(user: UserModel) {
        Glide.with(this).load(user.fotoUrl).into(binding.previewImageView)
        binding.editNama.setText(user.nama)
        binding.editEmail.setText(user.email)
    }

    private fun send() {
        val nama = binding.editNama.text.toString()
        val email = binding.editEmail.text.toString()
        var saldo = 0
        mainViewModel.getSession().observe(this) {
            saldo = it.saldo
        }

        editProfileViewModel.send(nama, email, saldo).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        showToast(result.data)
                        finish()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.message)
                    }
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.galleryButton.isEnabled = !isLoading
        binding.cameraButton.isEnabled = !isLoading
        binding.inputLayout.isEnabled = !isLoading
        binding.inputLayout1.isEnabled = !isLoading
        binding.sendButton.isEnabled = !isLoading
        binding.ProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}