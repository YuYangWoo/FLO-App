package com.example.floapplication.data.di

import android.app.Application


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidLogger()
//            androidContext(this@MyApplication)
//            modules(appModule)
//        }
    }

}