package com.accuratebits.tallkingheads.navigation

import androidx.annotation.DrawableRes
import com.accuratebits.tallkingheads.R

enum class AppScreens(val title: String, @DrawableRes val icon: Int) {
    CREATE("Create talk", R.drawable.create),
    TALKS("Talks", R.drawable.book)
}

val AppScreens.route: String
    get() = "$name/home"