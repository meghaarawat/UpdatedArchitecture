package com.myapplication.userAction.register.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.myapplication.R
import com.myapplication.base.SharedPref
import com.myapplication.dashboard.DashboardActivity
import com.myapplication.databinding.FragmentLoginBinding
import com.myapplication.databinding.FragmentRegistrationBinding
import com.myapplication.network.NetworkResult
import com.myapplication.others.Cons
import com.myapplication.others.CustomWatcher
import com.myapplication.others.MyUtils
import com.myapplication.others.Toaster
import com.myapplication.userAction.login.ui.LoginFragment
import com.myapplication.userAction.login.viewmodel.LoginVM
import com.myapplication.userAction.register.model.SignUpBean
import com.myapplication.userAction.register.viewmodel.RegisterVM

class RegistrationFragment : Fragment() {

    private val binding by lazy { FragmentRegistrationBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterVM>()
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

    /**
     * observe API Status
     * If response success than Save data to shared preference
     * moving to next activity
     */
    private fun bindObserver() {
        viewModel.userRegister.observe(viewLifecycleOwner) {
            MyUtils.viewGone(binding.loader)
            MyUtils.viewVisible(binding.tvSignup)
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.data?.let { it1 -> onSignUpSuccess(it1) }
                    startActivity(Intent(requireContext(), DashboardActivity::class.java))
                    activity?.finish()
                }
                is NetworkResult.Error -> {
                    it.message?.let { it1 -> Toaster.shortToast(it1) }
                }
                is NetworkResult.Loading -> {
                    MyUtils.viewGone(binding.tvSignup)
                    MyUtils.viewVisible(binding.loader)
                }
            }
        }
    }

    /** save user data to shared preference at the time of SignUp */
    private fun onSignUpSuccess(it: SignUpBean.Data) {
        sharedPref.save(Cons.firstName, it.firstName)
        sharedPref.save(Cons.lastName, it.lastName)
        SharedPref.get().save(Cons.USER_ID, it.id.toString())
        sharedPref.save(Cons.name, it.name)
        sharedPref.save(Cons.user_email, it.email)
        sharedPref.save(Cons.token, it.token)
        sharedPref.save(Cons.imagePath, it.imagePath)
    }

    /** for handling clicks */
    private fun setClicks() {

        /** @Signup click listener */
        binding.buttonSignup.setOnClickListener {
            viewModel.userSignUp()
        }

        /** @LogIn click listener */
        binding.tvSignUpLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container_user_activity, LoginFragment())?.commit()
        }

    }

    /** TextWatcher that watch a input text field and can instantly update data */
    private fun validateInputType() {
        binding.etFirstName.addTextChangedListener(
            CustomWatcher(binding.etFirstName,
                binding.tilFirstName, "Enter first name", CustomWatcher.EditTextType.FirstName)
        )
        binding.etLastName.addTextChangedListener(
            CustomWatcher(binding.etLastName,
                binding.tilLastName, "Enter last name", CustomWatcher.EditTextType.LastName)
        )
        binding.etEmailSignup.addTextChangedListener(
            CustomWatcher(binding.etEmailSignup,
            binding.tilEmailSignup, "Enter email", CustomWatcher.EditTextType.Email)
        )
        binding.etPasswordSignup.addTextChangedListener(
            CustomWatcher(binding.etPasswordSignup,
                binding.tilPasswordSignup, "Password cannot be left blank", CustomWatcher.EditTextType.Password)
        )
        binding.etPasswordSignup.setOnFocusChangeListener { _, hasFocus ->
            if (binding.etPasswordSignup.text?.isEmpty() == true && hasFocus) {
                binding.tilPasswordSignup.error = "Password should contain at least a number, an uppercase letter, a lowercase letter and a special character."
            }
        }
        binding.etConfirmPasswordSignup.addTextChangedListener(
            CustomWatcher(binding.etConfirmPasswordSignup,
                binding.tilConfirmPasswordSignup, "Password mismatch", CustomWatcher.EditTextType.ConfirmPassword)
        )
    }

}