package com.myapplication.userAction.reset.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.base.BaseModel
import com.myapplication.network.*
import com.myapplication.others.Cons
import com.myapplication.others.MyUtils
import kotlinx.coroutines.launch

class ResetVM : ViewModel() {

    private val repository = Repository()

    private val _userReset = MutableLiveData<NetworkResult<BaseResponse>>()
    val userReset: LiveData<NetworkResult<BaseResponse>> get() = _userReset

    /** Initialize Text Field. */
    var currentPassword = ObservableField<String>()
    var newPassword = ObservableField<String>()
    var confirmPassword = ObservableField<String>()

    /** Initialize Validate Fields. */
    var isValidCurrentPassword = ObservableField(BaseModel(true))
    var isValidNewPassword = ObservableField(BaseModel(true))
    var isValidConfirmPassword = ObservableField(BaseModel(true))

    private fun checkValidation(): Boolean {
        var valid = false
        when {
            MyUtils.isEmptyString(currentPassword.get()) -> {
                isValidCurrentPassword.set(BaseModel(message = "Enter current password"))
            }
            MyUtils.isEmptyString(newPassword.get()) -> {
                isValidNewPassword.set(BaseModel(message = "Enter new password"))
            }
            MyUtils.isEmptyString(confirmPassword.get()) -> {
                isValidConfirmPassword.set(BaseModel(message = "Enter confirm password"))
            }
            newPassword.get() != confirmPassword.get() -> {
                isValidNewPassword.set(BaseModel(message = "New password and Confirm password should be same"))
            }
            (newPassword.get().toString().trim().length < 6) -> {
                isValidNewPassword.set(BaseModel(message = "Password should be at least minimum 6 characters"))
            }
            (newPassword.get().toString().trim().length > 16) -> {
                isValidNewPassword.set(BaseModel(message = "Password should be maximum of 16 characters"))
            }
            else -> {
                valid = true
            }
        }
        return valid
    }

    fun resetPassword() = viewModelScope.launch {
        if (checkValidation()) {
            val map: HashMap<String, Any> = HashMap()
            map[Cons.currentPassword] = currentPassword.get().toString().trim()
            map[Cons.newPassword] = newPassword.get().toString().trim()
            map[Cons.confirmPassword] = confirmPassword.get().toString().trim()
            _userReset.value = NetworkResult.Loading()
            _userReset.value = repository.safeApiCall { repository.apiWithToken.changePassword(map) }
        }
    }
}