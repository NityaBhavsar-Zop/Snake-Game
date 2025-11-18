package com.example.somegame.di

import android.content.Context
import androidx.room.Room
import com.example.somegame.data.AppDatabase
import com.example.somegame.data.dao.ScoresDao
import com.example.somegame.repo.ScoresRepository
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
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "scores_db"
        ).build()

    @Provides
    fun provideScoresDao(db: AppDatabase): ScoresDao =
        db.scoresDao()

    @Provides
    @Singleton
    fun provideScoresRepository(dao: ScoresDao): ScoresRepository =
        ScoresRepository(dao)
}
