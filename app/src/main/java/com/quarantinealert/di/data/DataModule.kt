package com.quarantinealert.di.data

import android.content.Context
import androidx.room.Room
import com.quarantinealert.BuildConfig
import com.quarantinealert.data.database.AppDatabase
import com.quarantinealert.data.net.RestApi
import com.quarantinealert.data.session.SessionManager
import com.quarantinealert.data.session.SessionManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(RestApi.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RestApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()
    }

    single<SessionManager> {
        SessionManagerImpl(
            androidContext().getSharedPreferences(
                SessionManagerImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )
        )
    }
}