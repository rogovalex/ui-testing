package com.example.uitesting.inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uitesting.ui.PasswordCheckActivity
import com.example.uitesting.ui.PasswordCheckViewModel
import com.example.uitesting.domain.PasswordRepository
import dagger.Module
import dagger.Provides

@Module
class PasswordCheckActivityModule(private val activity: PasswordCheckActivity) {

    @Provides
    fun provideViewModel(passwordRepository: PasswordRepository): PasswordCheckViewModel {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return checkNotNull(modelClass.cast(PasswordCheckViewModel(passwordRepository)))
            }
        }
        return ViewModelProvider(activity, factory)[PasswordCheckViewModel::class.java]
    }
}
