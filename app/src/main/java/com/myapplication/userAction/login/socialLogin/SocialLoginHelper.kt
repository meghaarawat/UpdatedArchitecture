package com.myapplication.userAction.login.socialLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.myapplication.base.BaseFragment
import com.myapplication.base.SharedPref
import com.myapplication.others.Cons
import com.myapplication.others.Toaster

abstract class SocialLoginHelper : BaseFragment(), FacebookCallback<LoginResult> {

    val TAG = "SocialLoginHelper"

    @NonNull
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var callbackManager: CallbackManager

    var LOGIN_TYPE = -1
    protected val GOOGLE_LOGIN = 1
    protected val FB_LOGIN = 2

    abstract fun onSocialLoginSuccess(socialId: String, name: String, email: String)

    /**
     * This method is assigning social media elements
     * call in OnCreateView or OnCreate
     */
    fun onCreate() {
        /*FACEBOOK*/
        callbackManager = CallbackManager.Factory.create()

        /*GOOGLE*/
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        mGoogleSignInClient.signOut()
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            account.id?.let {
                account.email?.let { it1 ->
                    account.displayName?.let { it2 ->
                        onSocialLoginSuccess(it, it2, it1)
                    }
                }
            }

        } else {
            Log.e(TAG, "UPDATE ACCOUNT NULL")
        }
    }

    open fun actionLoginToFacebook() {
        SharedPref(context).save(Cons.loginType, Cons.FACEBOOK_FB)
        LOGIN_TYPE = FB_LOGIN
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logIn(this, listOf("public_profile, email"))
        LoginManager.getInstance().registerCallback(callbackManager, this@SocialLoginHelper)
    }

    fun actionGoogleLogin() {
        // TODO start google sign-in loader
        SharedPref(context).save(Cons.loginType, Cons.GMAIL)
        LOGIN_TYPE = GOOGLE_LOGIN
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Cons.GOOGLE_SIGN_IN)
    }

    private fun onLoginSuccessGetUserDetails(vAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(vAccessToken) { js, response ->
            Log.d(TAG, "onLoginSuccessGetUserDetails $js -- $response")
            Log.d("Email", "Email" + js?.optString("email"))
            onSocialLoginSuccess(js!!.optString("id"), js.optString("name"), js.optString("email"))
        }
        val bundle = Bundle()
        bundle.putString("fields", "id,name,email")
        request.parameters = bundle
        request.executeAsync()
    }

    /*FACEBOOK LOGIN CALL BACK METHODS*/
    override fun onSuccess(result: LoginResult) {
        val vAccessToken = result?.accessToken
        if (vAccessToken != null) {
            SharedPref(context).save(Cons.loginType, Cons.FACEBOOK)
            onLoginSuccessGetUserDetails(vAccessToken)
        }
    }

    override fun onCancel() {
        Toaster.shortToast("Login canceled")
        Log.e(TAG, "cancel")
    }

    override fun onError(error: FacebookException) {
        error.message?.let { Toaster.shortToast(it) }
        Log.d(TAG, error.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Cons.GOOGLE_SIGN_IN) {
            // TODO stop google sign-in loader
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount?>? =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount?>?) {
        try {
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.e(TAG, "signInResult:failed code=" + e.statusCode + " -- " + e.localizedMessage)
            updateUI(null)
        }
    }


}