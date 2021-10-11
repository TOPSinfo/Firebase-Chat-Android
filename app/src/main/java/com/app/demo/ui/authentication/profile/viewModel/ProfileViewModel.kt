package com.app.demo.ui.authentication.profile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.demo.data.repository.UserRepository
import com.app.demo.network.Resource
import com.app.demo.model.user.UserModel
import com.app.demo.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {


    private val _userDataResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val userDataResponse: LiveData<Resource<String>> get() = _userDataResponse


    /**
     * Adding user info in firebase
     */
    fun addUserData(user: UserModel) {

        _userDataResponse.value = Resource.loading(null)
        val data = hashMapOf(
            Constant.FIELD_UID to user.uid,
            Constant.FIELD_FIRST_NAME to user.firstName,
            Constant.FIELD_LAST_NAME to user.lastName,
            Constant.FIELD_PHONE to user.phone,
            Constant.FIELD_EMAIL to user.email,
        )
        userRepository.getUserProfileRepository(user.uid.toString()).set(data)
            .addOnSuccessListener {
                _userDataResponse.postValue(
                    Resource.success(
                        Constant.validation_error,
                    )
                )
            }.addOnFailureListener {

                _userDataResponse.postValue(
                    Resource.error(
                        Constant.validation_error,
                        null
                    )
                )
            }
    }

}