package org.d3if3137.assessment1.navigation

import org.d3if3137.assessment1.ui.screen.KEY_ID_KALKULATOR

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
    data object About : Screen("aboutScreen")
    data object CountBaru : Screen("countScreen")
    data object CountUbah : Screen("countScreen/{$KEY_ID_KALKULATOR}") {
        fun withId(id: Long) = "countScreen/$id"
    }
}