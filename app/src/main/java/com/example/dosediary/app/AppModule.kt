package com.example.dosediary.app

import android.content.Context
import com.example.dosediary.event.UserEvent
import com.example.dosediary.model.dao.MedicationHistoryDao
import com.example.dosediary.state.UserState
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
    @Singleton
    fun provideUserState(): UserState {
        return UserState()
    }
//
//    @Provides
//    @Singleton
//    fun provideUserEventHandler(userState: UserState): (UserEvent) -> Unit {
//        return { event -> UserEvent(event) }
//    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DoseDiaryDatabase {
        return DoseDiaryDatabase.getInstance(context)
    }

    @Provides
    fun provideMedicationHistoryDao(database: DoseDiaryDatabase): MedicationHistoryDao {
        return database.medicationHistoryDao
    }
}