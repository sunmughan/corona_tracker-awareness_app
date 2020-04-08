package com.quarantinealert.di.main

import com.quarantinealert.di.data.dataModule
import com.quarantinealert.di.feature.*
import com.quarantinealert.di.repository.repositoryModule

val listModule = listOf(
    dataModule,
    repositoryModule,
    splashModule,
    homeModule,
    searchModule,
    globalCasesModule,
    firebaseModule,
    feedbackModule
)