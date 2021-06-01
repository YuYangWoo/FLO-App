package com.example.floapplication.data.di

import com.example.floapplication.data.repository.SongRepository
import com.example.floapplication.ui.main.viewmodel.SongViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

val appModule = module {
    single {SongRepository()}
    viewModel { SongViewModel(get()) }

}
