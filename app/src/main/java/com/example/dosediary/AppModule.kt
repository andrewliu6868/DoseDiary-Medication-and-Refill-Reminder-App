package com.example.dosediary

import android.content.Context
import com.example.dosediary.model.UserState
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.utils.DoseDiaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUserState(): UserState {
        return UserState()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): DoseDiaryDatabase {
        return DoseDiaryDatabase.getInstance(appContext)
    }

    @Provides
    fun provideMedicationHistoryDao(database: DoseDiaryDatabase): MedicationHistoryDao {
        return database.medicationHistoryDao
    }
}