package com.example.uitesting.inject

import com.example.uitesting.data.ApiPasswordRepository
import com.example.uitesting.domain.PasswordRepository
import dagger.Binds
import dagger.Module

@Module
interface BindingModule {

    @Binds
    fun bindPasswordRepository(repository: ApiPasswordRepository): PasswordRepository
}
