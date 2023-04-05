package com.myapplication.userAction.reset.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.myapplication.base.SharedPref
import com.myapplication.databinding.FragmentResetPasswordBinding
import com.myapplication.network.NetworkResult
import com.myapplication.others.CustomWatcher
import com.myapplication.others.MyUtils
import com.myapplication.others.Toaster
import com.myapplication.userAction.reset.viewmodel.ResetVM

class ResetPasswordFragment : Fragment() {

    private val binding by lazy { FragmentResetPasswordBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<ResetVM>()
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        sharedPref = SharedPref(requireContext())
        validateInputType()
        setClicks()
        bindObserver()
    }

    private fun bindObserver() {
        viewModel.userReset.observe(viewLifecycleOwner) {
            MyUtils.viewGone(binding.loader)
            MyUtils.viewVisible(binding.tvUpdate)
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.message?.let { it1 -> Toaster.shortToast(it1) }
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> Toaster.shortToast(it1) }
                }
                is NetworkResult.Loading -> {
                    MyUtils.viewGone(binding.tvUpdate)
                    MyUtils.viewVisible(binding.loader)
                }
            }
        }
    }

    /** for handling clicks */
    private fun setClicks() {

        /** @Send click listener */
        binding.buttonUpdate.setOnClickListener {
            viewModel.resetPassword()
        }

        /** Button @Back click listener */
        binding.buttonBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

    }

    /** TextWatcher that watch a input text field and can instantly update data */
    private fun validateInputType() {
        binding.etCurrentPassword.addTextChangedListener(
            CustomWatcher(binding.etCurrentPassword,
                binding.tilCurrentPassword, "Enter current password", CustomWatcher.EditTextType.Password)
        )

        binding.etNewPassword.addTextChangedListener(
            CustomWatcher(binding.etNewPassword,
                binding.tilNewPassword, "Enter new password", CustomWatcher.EditTextType.Password)
        )

        binding.etConfirmPassword.addTextChangedListener(
            CustomWatcher(binding.etConfirmPassword,
                binding.tilConfirmPassword, "Enter confirm password", CustomWatcher.EditTextType.Password)
        )
    }
}