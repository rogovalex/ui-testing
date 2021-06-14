package com.example.uitesting.inject

import com.example.uitesting.ui.PasswordCheckActivity
import dagger.Subcomponent

@Subcomponent(modules = [PasswordCheckActivityModule::class])
@ActivityScope
interface PasswordCheckActivityComponent {

    fun inject(activity: PasswordCheckActivity)
}
