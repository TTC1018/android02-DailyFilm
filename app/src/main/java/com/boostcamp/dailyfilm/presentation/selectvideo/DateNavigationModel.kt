package com.boostcamp.dailyfilm.presentation.selectvideo

import kotlinx.serialization.Serializable

@Serializable
data class DateNavigationModel(
    val year: String,
    val month: String,
    val day: String,
    val text: String? = null,
    val videoUrl: String? = null,
) {
    val date get() = year + month.padStart(2, '0') + day.padStart(2, '0')
}