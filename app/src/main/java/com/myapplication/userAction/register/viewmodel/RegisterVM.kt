package com.myapplication.userAction.register.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.base.BaseModel
import com.myapplication.network.NetworkResult
import com.myapplication.network.Repository
import com.myapplication.network.RetrofitApi
import com.myapplication.network.RetrofitClient
import com.myapplication.others.CaseSensitiveHelper
import com.myapplication.others.Cons
import com.myapplication.others.EmailValidator
import com.myapplication.others.MyUtils
import com.myapplication.userAction.register.model.SignUpBean
import kotlinx.coroutines.launch
import java.util.regex.Pattern


class RegisterVM: ViewModel() {

    private val repository = Repository()

    private val _userRegister = MutableLiveData<NetworkResult<SignUpBean>>()
    val userRegister: LiveData<NetworkResult<SignUpBean>> get() = _userRegister

    /** Initialize Text Field. */
    var fName = ObservableField<String>()
    var lName = ObservableField<String>()
    var email = ObservableField<String>()
    var password = ObservableField<String>()
    var confirmPassword = ObservableField<String>()

    /** Initialize Validate Fields. */
    val isValidFName = ObservableField(BaseModel(true))
    val isValidLName = ObservableField(BaseModel(true))
    val isValidEmail = ObservableField(BaseModel(true))
    val isValidPassword = ObservableField(BaseModel(true))
    val isValidCPassword = ObservableField(BaseModel(true))

    // defining our own password pattern
    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=])" +  // at least 1 special character
                "(?=\\S+$)" +  // no white spaces
                ".{7,}" +  // at least 7 characters
                "$"
    )

    /** Check Validation
     * If all fields valid then return true
     * */
    private fun checkValidation(): Boolean {
        var valid = false
        when {
            MyUtils.isEmptyString(fName.get()) -> {
                isValidFName.set(BaseModel(message = "Enter first name"))
            }
            MyUtils.isEmptyString(lName.get()) -> {
                isValidLName.set(BaseModel(message = "Enter last name"))
            }
            MyUtils.isEmptyString(email.get()) -> {
                isValidEmail.set(BaseModel(message = "Enter email"))
            }
            !EmailValidator.getInstance().validate(email.get().toString().trim()) -> {
                isValidEmail.set(BaseModel(message = "Please enter valid email address"))
            }
            MyUtils.isEmptyString(password.get()) -> {
                isValidPassword.set(BaseModel(message = "Enter password"))
            }
            password.get() != confirmPassword.get() -> {
                isValidCPassword.set(BaseModel(message = "Password mismatch"))
            }
            (password.get().toString().trim().length < 7) -> {
                isValidPassword.set(BaseModel(message = "Password should be at least minimum 7 characters"))
            }
            (!PASSWORD_PATTERN.matcher(password.get().toString().trim()).matches()) -> {
                isValidPassword.set(BaseModel(message = "Password should contain at least a number, an uppercase letter, a lowercase letter and a special character."))
            }
            (password.get().toString().trim().length > 18) -> {
                isValidPassword.set(BaseModel(message = "Password should be maximum of 18 characters"))
            }
            else -> {
                valid = true
            }
        }
        return valid
    }

    /** Register User */
    fun userSignUp() = viewModelScope.launch {
        if (checkValidation()) {
            val map: HashMap<String, Any> = HashMap()
            map[Cons.F_NAME] = CaseSensitiveHelper.returnCamelCaseWord(fName.get().toString().trim())
            map[Cons.L_NAME] = CaseSensitiveHelper.returnCamelCaseWord(lName.get().toString().trim())
            map[Cons.EMAIL] = email.get().toString().trim()
            map[Cons.PASSWORD] = password.get().toString().trim()
            map[Cons.REGISTER_TYPE] = "Native"
            map[Cons.DEVICE_TYPE] = "android"
            map[Cons.ROLE_TYPE] = "family"
            _userRegister.value = NetworkResult.Loading()
            _userRegister.value = repository.safeApiCall { repository.apiWithoutToken.registerWithEmail(map) }
        }
    }
}