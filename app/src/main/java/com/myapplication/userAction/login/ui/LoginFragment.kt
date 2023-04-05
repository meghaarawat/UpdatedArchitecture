package com.myapplication.userAction.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.myapplication.R
import com.myapplication.userAction.login.socialLogin.SocialLoginHelper
import com.myapplication.userAction.login.viewmodel.LoginVM
import com.myapplication.base.SharedPref
import com.myapplication.dashboard.DashboardActivity
import com.myapplication.databinding.FragmentLoginBinding
import com.myapplication.network.NetworkResult
import com.myapplication.others.Cons
import com.myapplication.others.CustomWatcher
import com.myapplication.others.MyUtils
import com.myapplication.others.Toaster
import com.myapplication.userAction.forgot.ui.ForgotPasswordFragment
import com.myapplication.userAction.login.model.LoginBean
import com.myapplication.userAction.register.ui.RegistrationFragment

class LoginFragment : SocialLoginHelper() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginVM>()
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userModel = viewModel
        sharedPref = SharedPref(requireContext())
        onCreate()
        validateInputType()
        setClicks()
        bindObserver()
    }

    /**
     * observe API Status
     * If response success than Save data to shared preference
     * moving to next activity
     */
    private fun bindObserver() {
        viewModel.userLogin.observe(viewLifecycleOwner) {
            MyUtils.viewGone(binding.loader)
            MyUtils.viewVisible(binding.tvLogin)
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.data?.let { it1 -> onLoginSuccess(it1) }
                    startActivity(Intent(requireContext(), DashboardActivity::class.java))
                    activity?.finish()
                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> Toaster.shortToast(it1) }
                }
                is NetworkResult.Loading -> {
                    MyUtils.viewGone(binding.tvLogin)
                    MyUtils.viewVisible(binding.loader)
                }
            }
        }
    }

    /** save user data to shared preference at the time of login */
    private fun onLoginSuccess(it: LoginBean.Data) {
        sharedPref.save(Cons.firstName, it.firstName)
        sharedPref.save(Cons.lastName, it.lastName)
        sharedPref.save(Cons.name, it.name)
        sharedPref.save(Cons.user_email, it.email)
        sharedPref.save(Cons.token, it.token)
        sharedPref.save(Cons.phone, it.phone)
        sharedPref.save(Cons.location, it.location)
        sharedPref.save(Cons.imagePath, it.imagePath)
        sharedPref.save(Cons.aboutUser, it.aboutUser)
    }

    /** for handling clicks */
    private fun setClicks() {
        /** @LogIn click listener */
        binding.buttonLogin.setOnClickListener {
            viewModel.login()
        }

        /** @ForgotPassword click listener */
        binding.tvForgotPassword.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.container_user_activity, ForgotPasswordFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        /** @SignUp click listener */
        binding.tvSignUpLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.container_user_activity, RegistrationFragment())
                ?.commit()
        }

        /** @Google click listener */
        binding.buttonGoogle.setOnClickListener {
            actionGoogleLogin()
        }

        /** @Facebook click listener */
        binding.buttonFacebook.setOnClickListener {
            actionLoginToFacebook()
        }

    }

    /** TextWatcher that watch a input text field and can instantly update data */
    private fun validateInputType() {
        binding.etEmailLogin.addTextChangedListener(CustomWatcher(binding.etEmailLogin,
                binding.tilEmailLogin, "Enter email", CustomWatcher.EditTextType.FirstName)
        )
        binding.etPasswordLogin.addTextChangedListener(
            CustomWatcher(binding.etPasswordLogin,
                binding.tilPasswordLogin, "Enter password", CustomWatcher.EditTextType.Password)
        )
    }

    /**
     * This method is override by SocialLoginHelper class
     * We are getting user data after login is successful
     */
    override fun onSocialLoginSuccess(socialId: String, name: String, email: String) {
        Toaster.shortToast("Success")
    }

}