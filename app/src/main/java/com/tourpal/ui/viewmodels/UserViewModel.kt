package com.tourpal.ui.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourpal.data.model.User
import com.tourpal.data.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow<Boolean?>(null)
    val success: StateFlow<Boolean?> = _success

    fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                val result = userRepository.getUser(userId)
                _user.value = result
                _error.value = null
            } catch (e: Exception) {
                _user.value = null
                _error.value = e.message
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            val success = userRepository.saveUser(user)
            _success.value = success
            if (!success) _error.value = "Failed to save user"
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            val success = userRepository.updateUser(user)
            _success.value = success
            if (!success) _error.value = "Failed to update user"
        }
    }
}