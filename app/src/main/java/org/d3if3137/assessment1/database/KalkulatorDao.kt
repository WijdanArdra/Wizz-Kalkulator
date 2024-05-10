package org.d3if3137.assessment1.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if3137.assessment1.model.Kalkulator

@Dao
interface KalkulatorDao {

    @Insert
    suspend fun insert(kalkulator: Kalkulator)

    @Update
    suspend fun update(kalkulator: Kalkulator)

    @Query("SELECT * FROM kalkulator ORDER BY opsi DESC")
    fun getKalkulator(): Flow<List<Kalkulator>>

    @Query("SELECT * FROM kalkulator WHERE id = :id")
    suspend fun getKalkulatorById(id: Long): Kalkulator?

    @Query("DELETE FROM kalkulator WHERE id = :id")
    suspend fun deleteById(id: Long)
}