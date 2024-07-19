package com.example.dosediary.app

import com.example.dosediary.state.UserState
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