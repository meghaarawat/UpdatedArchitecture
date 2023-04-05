package com.myapplication.userAction.profile.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.base.BaseModel
import com.myapplication.network.*
import com.myapplication.others.CaseSensitiveHelper
import com.myapplication.others.Cons
import com.myapplication.others.MyUtils
import com.myapplication.userAction.profile.model.ProfileBean
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileVM : ViewModel() {

    private val repository = Repository()

    private val _userUpdate = MutableLiveData<NetworkResult<ProfileBean>>()
    val userUpdate: LiveData<NetworkResult<ProfileBean>> get() = _userUpdate

    /** Initialize Text Field. */
    var firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    var email = ObservableField<String>()
    var phone = ObservableField<String>()
    var location = ObservableField<String>()
    var about = ObservableField<String>()
    var fil: File? = null

    /** Initialize Validate Fields. */
    var isValidFName = ObservableField(BaseModel(true))
    var isValidLName = ObservableField(BaseModel(true))
    var isValidLocation = ObservableField(BaseModel(true))

    private fun checkValidation(): Boolean {
        var valid = false
        when {
            MyUtils.isEmptyString(firstName.get()) -> {
                isValidFName.set(BaseModel(message = "Enter first name"))
            }
            MyUtils.isEmptyString(lastName.get()) -> {
                isValidLName.set(BaseModel(message = "Enter last name"))
            }
            MyUtils.isEmptyString(location.get()) -> {
                isValidLocation.set(BaseModel(message = "Enter location"))
            }
            else -> {
                valid = true
            }
        }
        return valid
    }

    fun updateProfile() = viewModelScope.launch {
        if (checkValidation()) {
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart(Cons.firstName, CaseSensitiveHelper.returnCamelCaseWord(firstName.get().toString().trim()))
            builder.addFormDataPart(Cons.lastName, CaseSensitiveHelper.returnCamelCaseWord(lastName.get().toString().trim()))
            builder.addFormDataPart(Cons.phone, location.get().toString().trim())
            builder.addFormDataPart(Cons.location, phone.get().toString().trim())
            builder.addFormDataPart(Cons.aboutUser, about.get().toString().trim())
            if (fil != null) {
                builder.addFormDataPart(Cons.image, fil!!.name,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fil!!))
            }
            val requestBody = builder.build()
            _userUpdate.value = NetworkResult.Loading()
            _userUpdate.value = repository.safeApiCall { repository.apiWithToken.updateProfile(requestBody) }
        }
    }
}