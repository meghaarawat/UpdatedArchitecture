package com.myapplication.userAction.login.viewmodel

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
import com.myapplication.userAction.login.model.LoginBean
import kotlinx.coroutines.launch

class LoginVM : ViewModel(){

    private val repository = Repository()

    private val _userLogin = MutableLiveData<NetworkResult<LoginBean>>()
    val userLogin: LiveData<NetworkResult<LoginBean>> get() = _userLogin

    /** Initialize Text Field. */
    var email = ObservableField<String>()
    var password = ObservableField<String>()

    /** Initialize Validate Fields. */
    val isValidEmail = ObservableField(BaseModel(true))
    val isValidPassword = ObservableField(BaseModel(true))

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
            MyUtils.isEmptyString(password.get()) -> {
                isValidPassword.set(BaseModel(message = "Enter password"))
            }
            else -> {
                valid = true
            }
        }
        return valid
    }

    /** Login User API call*/
    fun login() = viewModelScope.launch {
        if (checkValidation()) {
            val map: HashMap<String, Any> = HashMap()
            map[Cons.EMAIL] = email.get().toString().trim()
            map[Cons.PASSWORD] = password.get().toString().trim()
            _userLogin.value = NetworkResult.Loading()
            _userLogin.value = repository.safeApiCall { repository.apiWithoutToken.loginWithEmail(map) }
        }
    }

}