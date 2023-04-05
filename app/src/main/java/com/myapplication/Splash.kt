package com.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.myapplication.base.SharedPref
import com.myapplication.dashboard.DashboardActivity
import com.myapplication.others.Cons
import com.myapplication.others.MyUtils
import com.myapplication.userAction.UserActivity

class Splash : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /** Check token
         * If token is not empty than move to Dashboard Screen
         * Else move to LogIn Screen
         * */

        sharedPref = SharedPref(applicationContext)
        val token = sharedPref.getStringValue(Cons.token)
        // we used the postDelayed(Runnable, time) method to send a user to login page after delayed time.
        Handler().postDelayed({
            when {
                MyUtils.isEmptyString(token) -> {
                    startActivity(Intent(this, UserActivity::class.java))
                    this.finish()
                }
                else -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    this.finish()
                }
            }
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}