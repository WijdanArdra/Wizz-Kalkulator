package org.d3if3137.assessment1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kalkulator")
data class Kalkulator(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val masukkan: String,
    val opsi: String,
    val hasil: String
)