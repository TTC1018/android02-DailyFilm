package com.boostcamp.dailyfilm.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint


private val TAG = DailyFilmActivity::class.simpleName

@AndroidEntryPoint
class DailyFilmActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DailyFilmApp()
        }
    }
}