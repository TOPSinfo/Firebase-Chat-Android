package com.app.demo.ui.authentication.profile.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import com.app.demo.R
import com.app.demo.core.BaseActivity
import com.app.demo.databinding.ActivityRegisterBinding
import com.app.demo.model.user.UserModel
import com.app.demo.network.Status
import com.app.demo.ui.authentication.profile.viewModel.ProfileViewModel
import com.app.demo.ui.dashboard.activity.DashboardActivity
import com.app.demo.utils.Utility
import com.app.demo.utils.toast
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var userId: String? = null
    private var phoneNumber: String? = null

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    /**
     * Initialize user interface
     */
    private fun initUI() {
        val fb = FirebaseAuth.getInstance().currentUser
        userId = fb?.uid.toString()
        phoneNumber = fb?.phoneNumber.toString()
        setClickListener()
        setObserver()
    }

    /**
     * set observer
     */
    private fun setObserver() {

        profileViewModel.userDataResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let {
                        redirectDashboard()
                    }
                }
                Status.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> toast(it1) }
                }
            }
        })
    }

    /**
     * Set click listener
     */
    private fun setClickListener() {

        binding.btnRegister.setOnClickListener {
            if (checkValidation()) {
                val userModel = UserModel()
                userModel.uid = userId
                userModel.email = binding.edEmail.text.toString().trim()
                userModel.firstName = binding.edFName.text.toString().trim()
                userModel.lastName = binding.edLastName.text.toString().trim()
                userModel.phone = phoneNumber
                profileViewModel.addUserData(userModel)
            }
        }
    }

    /**
     * Checking validation
     */
    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(binding.edFName.text.toString().trim())) {
            toast(getString(R.string.please_enter_first_name))
            return false
        } else if (TextUtils.isEmpty(binding.edLastName.text.toString().trim())) {
            toast(getString(R.string.please_enter_last_name))
            return false
        } else if (TextUtils.isEmpty(binding.edEmail.text.toString().trim())) {
            toast(getString(R.string.please_enter_email_address))
            return false
        } else if (!Utility.emailValidator(binding.edEmail.text.toString().trim())) {
            toast(getString(R.string.please_enter_valid_email_address))
            return false
        }
        return true
    }

    /**
     * Redirecting to dashboard
     */
    private fun redirectDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}