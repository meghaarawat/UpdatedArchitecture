package com.myapplication.dashboard

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListAdapter
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.myapplication.R
import com.myapplication.base.SharedPref
import com.myapplication.dashboard.home.adapter.DrawerListAdapter
import com.myapplication.dashboard.home.viewmodel.DashboardVM
import com.myapplication.databinding.ActivityDashboardBinding
import com.myapplication.databinding.DrawerDashboardBinding
import com.myapplication.databinding.FragmentForgotPasswordBinding
import com.myapplication.others.Cons
import com.myapplication.others.ImageUtils
import com.myapplication.others.NeTWorkChange
import com.myapplication.others.Toaster
import com.myapplication.userAction.UserActivity
import com.myapplication.userAction.forgot.viewmodel.ForgotVM
import com.myapplication.userAction.profile.ui.UpdateProfileFragment

class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DashboardVM>()
    private val adapter = DrawerListAdapter()
    private val neTWorkChange = NeTWorkChange()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = viewModel
        setDrawerData()
        setSupportActionBar(binding.drawer.toolbar)
        setupDrawerContent(binding.navView)
        setClicks()
    }

    private fun setClicks() {
        binding.drawer.toolbar.setNavigationOnClickListener {
            setNavUserDetails()
            toggleDrawer()
        }

        binding.drawer.icNotification.setOnClickListener {
            Toaster.shortToast("No Notifications")
        }

        binding.header.layoutEdit.setOnClickListener {
            val i = Intent(this, UserActivity::class.java)
            i.putExtra(Cons.FRAG_NAME, Cons.PROFILE)
            startActivity(i)
            toggleDrawer()
        }
    }

    private fun setDrawerData() {
        binding.expandableLV.setAdapter(adapter)
        binding.expandableLV.setOnGroupClickListener { _, _, i, _ ->
            when (i) {
                1 -> {
                    val intent = Intent(this, UserActivity::class.java)
                    intent.putExtra(Cons.FRAG_NAME, Cons.RESET)
                    startActivity(intent)
                }
                2 -> {
                    SharedPref.get().clearAll()
                    this.startActivity(Intent(this, UserActivity::class.java))
                    this.finishAffinity()
                }
            }
            toggleDrawer()
            false
        }
    }

    private fun setNavUserDetails() {
        binding.header.tvNameNavDrawer.text = SharedPref.get().get(Cons.name)
        binding.header.tvAboutNavDrawer.text = SharedPref.get().get(Cons.aboutUser)
        ImageUtils.loadImage(binding.header.profileImageNavDrawer, SharedPref.get().get(Cons.imagePath))
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onStart() {
        registerReceiver(neTWorkChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(neTWorkChange);
        super.onStop()
    }

}