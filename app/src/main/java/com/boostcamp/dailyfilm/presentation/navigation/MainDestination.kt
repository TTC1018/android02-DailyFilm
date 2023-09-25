package com.boostcamp.dailyfilm.presentation.navigation

enum class MainDestination(
    val route: String
) {
    LOGIN("LoginRoute"),
    CALENDAR("CalendarRoute"),
    PLAY_FILM("PlayFilmRoute"),
    SEARCH_FILM("SearchFilmRoute"),
    SELECT_VIDEO("SelectVideoRoute"),
    TRIM_VIDEO("TrimVideoRoute"),
    UPLOAD_FILM("UploadFilmRoute"),
    TOTAL_FILM("TotalFilmRoute"),
    SETTINGS("SettingsRoute")
}