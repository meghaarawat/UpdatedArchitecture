package com.myapplication.userAction.forgot.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.myapplication.base.SharedPref
import com.myapplication.databinding.FragmentForgotPasswordBinding
import com.myapplication.network.NetworkResult
import com.myapplication.others.CustomWatcher
import com.myapplication.others.MyUtils
import com.myapplication.others.Toaster
import com.myapplication.userAction.forgot.viewmodel.ForgotVM

class ForgotPasswordFragment : Fragment() {

    private val binding by lazy { FragmentForgotPasswordBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<ForgotVM>()
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userModel = viewModel
        sharedPref = SharedPref(requireContext())
        validateInputType()
        setClicks()
        bindObserver()
    }

    private fun bindObserver() {
        viewModel.userForgot.observe(viewLifecycleOwner) {
            MyUtils.viewGone(binding.loader)
            MyUtils.viewVisible(binding.tvSend)
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.message?.let { it1 -> Toaster.shortToast(it1) }
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> Toaster.shortToast(it1) }
                }
                is NetworkResult.Loading -> {
                    MyUtils.viewGone(binding.tvSend)
                    MyUtils.viewVisible(binding.loader)
                }
            }
        }
    }

    /** for handling clicks */
    private fun setClicks() {

        /** @Send click listener */
        binding.buttonSend.setOnClickListener {
            viewModel.forgotPassword()
        }

        /** Button @Back click listener */
        binding.buttonBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

    }

    /** TextWatcher that watch a input text field and can instantly update data */
    private fun validateInputType() {
        binding.etEmailForgotP.addTextChangedListener(
            CustomWatcher(binding.etEmailForgotP,
                binding.tilEmailForgotP, "Enter email", CustomWatcher.EditTextType.FirstName)
        )
    }
}