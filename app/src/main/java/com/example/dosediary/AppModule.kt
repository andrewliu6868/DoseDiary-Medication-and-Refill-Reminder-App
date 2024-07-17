package com.example.dosediary

import com.example.dosediary.model.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUserState(): UserState {
        return UserState()
    }
}