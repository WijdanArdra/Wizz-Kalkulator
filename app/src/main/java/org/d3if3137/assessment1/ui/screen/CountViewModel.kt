package org.d3if3137.assessment1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3137.assessment1.database.KalkulatorDao
import org.d3if3137.assessment1.model.Kalkulator

class DetailViewModel(private val dao: KalkulatorDao) : ViewModel() {

    fun insert(judul: String, masukkan: String, opsi: String, hasil: String) {
        val kalkulator = Kalkulator(
            judul = judul,
            masukkan = masukkan,
            opsi = opsi,
            hasil = hasil
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(kalkulator)
        }
    }

    suspend fun getKalkulator(id: Long): Kalkulator? {
        return dao.getKalkulatorById(id)
    }

    fun update(id: Long, judul: String, masukkan: String, opsi: String, hasil: String) {
        val kalkulator = Kalkulator(
            id = id,
            judul = judul,
            masukkan = masukkan,
            opsi = opsi,
            hasil = hasil
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(kalkulator)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}