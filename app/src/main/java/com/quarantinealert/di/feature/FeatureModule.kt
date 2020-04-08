package com.quarantinealert.di.feature

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.quarantinealert.feature.feedback.FeedbackViewModel
import com.quarantinealert.feature.global.GlobalCasesAdapter
import com.quarantinealert.feature.global.GlobalCasesViewModel
import com.quarantinealert.feature.home.HomeViewModel
import com.quarantinealert.feature.search.SearchAdapter
import com.quarantinealert.feature.search.SearchViewModel
import com.quarantinealert.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(androidApplication(), get(), get()) }
}

val homeModule = module {
    viewModel { HomeViewModel(androidApplication(), get()) }
}

val searchModule = module {
    viewModel { SearchViewModel(androidApplication(), get()) }
    factory { SearchAdapter() }
}

val globalCasesModule = module {
    viewModel { GlobalCasesViewModel(get()) }
    factory { GlobalCasesAdapter() }
}

val firebaseModule = module {
    single { FirebaseRemoteConfig.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val feedbackModule = module {
    viewModel { FeedbackViewModel(androidApplication(), get()) }
}