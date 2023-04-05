package com.myapplication.userAction

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myapplication.userAction.login.ui.LoginFragment
import com.myapplication.R
import com.myapplication.others.Cons
import com.myapplication.others.NeTWorkChange
import com.myapplication.userAction.profile.ui.UpdateProfileFragment
import com.myapplication.userAction.reset.ui.ResetPasswordFragment

class UserActivity : AppCompatActivity() {

    /** Initialize for @Internet Check */
    private val neTWorkChange = NeTWorkChange()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        /** @LoginFragment */
        when(intent.getStringExtra(Cons.FRAG_NAME)) {
            Cons.RESET -> {
                supportFragmentManager.beginTransaction().add(R.id.container_user_activity,
                    ResetPasswordFragment()
                ).commit()
            }
            Cons.PROFILE -> {
                supportFragmentManager.beginTransaction().add(R.id.container_user_activity,
                    UpdateProfileFragment()
                ).commit()
            }
            else -> {
                /** @LoginFragment */
                supportFragmentManager.beginTransaction().add(R.id.container_user_activity,
                    LoginFragment()).commit()
            }
        }
    }

    /** Check @Internet Status */
    override fun onStart() {
        registerReceiver(neTWorkChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        super.onStart()
    }

    /** Check @Internet Status */
    override fun onStop() {
        unregisterReceiver(neTWorkChange);
        super.onStop()
    }
}