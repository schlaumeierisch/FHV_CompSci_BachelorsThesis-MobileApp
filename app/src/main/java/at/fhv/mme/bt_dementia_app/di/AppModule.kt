package at.fhv.mme.bt_dementia_app.di

import android.content.Context
import androidx.room.Room
import at.fhv.mme.bt_dementia_app.database.AppRoomDatabase
import at.fhv.mme.bt_dementia_app.database.dao.ActivityDao
import at.fhv.mme.bt_dementia_app.database.dao.ContactDao
import at.fhv.mme.bt_dementia_app.database.dao.MedicationDao
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
    fun provideAppRoomDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppRoomDatabase::class.java, "APP_DATABASE"
        ).build()
    }

    @Provides
    fun provideActivityDao(appRoomDatabase: AppRoomDatabase): ActivityDao {
        return appRoomDatabase.activityDao()
    }

    @Provides
    fun provideContactDao(appRoomDatabase: AppRoomDatabase): ContactDao {
        return appRoomDatabase.contactDao()
    }

    @Provides
    fun provideMedicationDao(appRoomDatabase: AppRoomDatabase): MedicationDao {
        return appRoomDatabase.medicationDao()
    }
}