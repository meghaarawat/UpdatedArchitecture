package com.myapplication.userAction.profile.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.myapplication.R
import com.myapplication.base.BaseFragment
import com.myapplication.base.Permission
import com.myapplication.base.PermissionManager
import com.myapplication.base.SharedPref
import com.myapplication.databinding.FragmentForgotPasswordBinding
import com.myapplication.databinding.FragmentUpdateProfileBinding
import com.myapplication.dialogs.SelectionMediaDialog
import com.myapplication.network.NetworkResult
import com.myapplication.others.CustomWatcher
import com.myapplication.others.ImageHelper
import com.myapplication.others.MyUtils
import com.myapplication.others.Toaster
import com.myapplication.userAction.forgot.viewmodel.ForgotVM
import com.myapplication.userAction.profile.viewmodel.ProfileVM
import java.io.File
import java.io.IOException

class UpdateProfileFragment : BaseFragment() {

    private val binding by lazy { FragmentUpdateProfileBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<ProfileVM>()
    private lateinit var sharedPref: SharedPref
    private val permissionManager = PermissionManager.from(this)
    private val imageHelper = ImageHelper()
    private var currentPhotoPath = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        sharedPref = SharedPref(requireContext())
        validateInputType()
        setClicks()
        bindObserver()
    }

    /**
     * observe API Status
     * show toast according to response
     */
    private fun bindObserver() {
        viewModel.userUpdate.observe(viewLifecycleOwner) {
            hideLoader()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.message?.let { it1 -> Toaster.shortToast(it1) }
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> Toaster.shortToast(it1) }
                }
                is NetworkResult.Loading -> {
                    showLoader()
                }
            }
        }
    }

    /** for handling clicks */
    private fun setClicks() {
        /** Button @Back click listener */
        binding.buttonBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        /** @Update User Profile click listener */
        binding.buttonSave.setOnClickListener {
            viewModel.updateProfile()
        }

        binding.chooseImage.setOnClickListener {
            SelectionMediaDialog(context = requireContext(),
                onCamera = {
                    permissionManager
                        .request(Permission.Camera)
                        .rationale("Camera Permission is required to capture an image")
                        .checkPermission { granted ->
                            if (granted) {
                                takePhotoFromCamera()
                            }
                        }
                },
                onGallery = {
                    permissionManager
                        .request(Permission.Storage)
                        .rationale("Storage Permissions are required to select an image")
                        .checkPermission { granted ->
                            if (granted) {
                                takePhotoFromGallery()
                            }
                        }
                }
            ).show()
        }
    }

    private fun takePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)
    }

    /** for clicking image from camera */
    private fun takePhotoFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = imageHelper.createImageFile(requireContext())
                currentPhotoPath = photoFile.absolutePath
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(requireContext(), imageHelper.authority, photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                cameraLauncher.launch(takePictureIntent)
            }
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            if (selectedImageUri != null) {
                imageHelper.uriToFile(requireContext() , selectedImageUri).let { file ->
                    setImage(binding.profileImage, file!!.absolutePath)
                }
            }
        }
    }

    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val f = File(currentPhotoPath)
            setImage(binding.profileImage,  f.absolutePath)
        }
    }

    private fun setImage(imageView: ImageView, filePath: String){
        viewModel.fil = File(filePath)
        Glide.with(imageView.context).asBitmap().load(filePath).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView)
    }

    private fun validateInputType() {
        binding.etFirstName.addTextChangedListener(
            CustomWatcher(binding.etFirstName,
                binding.tilFirstName, "Enter first name", CustomWatcher.EditTextType.FirstName)
        )
        binding.etLastName.addTextChangedListener(
            CustomWatcher(binding.etLastName,
                binding.tilLastName, "Enter last name", CustomWatcher.EditTextType.LastName)
        )
        binding.etLocationProfile.addTextChangedListener(
            CustomWatcher(binding.etLocationProfile,
                binding.tilLocationProfile, "Enter location", CustomWatcher.EditTextType.FirstName)
        )
    }
}