package com.quarantinealert.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quarantinealert.BuildConfig
import com.quarantinealert.data.database.dao.CountryDao
import com.quarantinealert.data.database.dao.TotalCasesDao
import com.quarantinealert.model.Country
import com.quarantinealert.model.TotalCases

@Database(entities = [Country::class, TotalCases::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID
    }

    abstract fun countryDao(): CountryDao

    abstract fun totalCasesDao(): TotalCasesDao
}