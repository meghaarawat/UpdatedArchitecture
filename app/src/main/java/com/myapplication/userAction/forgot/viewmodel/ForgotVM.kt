package com.myapplication.userAction.forgot.viewmodel

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.base.BaseModel
import com.myapplication.network.*
import com.myapplication.others.Cons
import com.myapplication.others.MyUtils
import com.myapplication.userAction.register.model.SignUpBean
import kotlinx.coroutines.launch

class ForgotVM : ViewModel() {

    private val repository = Repository()

    private val _userForgot = MutableLiveData<NetworkResult<BaseResponse>>()
    val userForgot: LiveData<NetworkResult<BaseResponse>> get() = _userForgot

    /** Initialize Text Field. */
    var email = ObservableField<String>()

    /** Initialize Validate Fields. */
    val isValidEmail = ObservableField(BaseModel(true))

    /** Check Validation
     * If all fields valid then return true
     * */
    private fun checkValidation(): Boolean {
        var valid = false
        when {
            MyUtils.isEmptyString(email.get()) -> {
                isValidEmail.set(BaseModel(message = "Enter email"))
            }
            !Patterns.EMAIL_ADDRESS.matcher(email.get().toString().trim()).matches() -> {
                isValidEmail.set(BaseModel(message = "Please enter valid email address"))
            }
            else -> {
                valid = true
            }
        }
        return valid
    }

    /** Reset User Password*/
    fun forgotPassword() = viewModelScope.launch {
        if (checkValidation()) {
            val map: HashMap<String, Any> = HashMap()
            map[Cons.EMAIL] = email.get().toString().trim()
            _userForgot.value = NetworkResult.Loading()
            _userForgot.value = repository.safeApiCall { repository.apiWithoutToken.forgotPassword(map) }
        }
    }

}