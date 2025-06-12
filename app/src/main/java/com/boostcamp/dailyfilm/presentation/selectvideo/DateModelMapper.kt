package com.boostcamp.dailyfilm.presentation.selectvideo

import com.boostcamp.dailyfilm.presentation.calendar.model.DateModel

internal fun DateModel.asNavigationModel() = DateNavigationModel(
    year = year,
    month = month,
    day = day,
    text = text,
    videoUrl = videoUrl,
)