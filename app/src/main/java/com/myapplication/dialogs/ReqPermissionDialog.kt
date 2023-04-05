package com.myapplication.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.myapplication.R
import com.myapplication.base.BaseDialog
import com.myapplication.databinding.DialogMediaSelectionTypeBinding
import com.myapplication.databinding.DialogRequestPermissionBinding
import com.myapplication.databinding.FragmentUpdateProfileBinding

class ReqPermissionDialog(context: Context, private val rationale: String) : BaseDialog(context) {

    private val binding by lazy { DialogRequestPermissionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setDimBlur(window)
        binding.tvTitleReq.text = context.getString(R.string.title_permission)
        binding.tvDescriptionReq.text = rationale
        binding.buttonCancelReq.setOnClickListener {
            dismiss()
        }
        binding.buttonOkReq.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
            dismiss()
        }
    }
}