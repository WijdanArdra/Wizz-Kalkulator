package org.d3if3137.assessment1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3137.assessment1.database.KalkulatorDao
import org.d3if3137.assessment1.model.Kalkulator

class MainViewModel(dao: KalkulatorDao) : ViewModel() {

    val data: StateFlow<List<Kalkulator>> = dao.getKalkulator().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}