package com.quarantinealert.di.repository

import com.quarantinealert.repository.CovidRepository
import com.quarantinealert.repository.CovidRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<CovidRepository> { CovidRepositoryImpl(androidContext(), get(), get(), get()) }
}