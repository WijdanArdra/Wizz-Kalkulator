package org.d3if3137.assessment1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3137.assessment1.model.Kalkulator

@Database(entities = [Kalkulator::class], version = 1, exportSchema = false)
abstract class KalkulatorDb : RoomDatabase() {

    abstract val dao: KalkulatorDao

    companion object {

        @Volatile
        private var INSTANCE: KalkulatorDb? = null

        fun getInstance(context: Context): KalkulatorDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        KalkulatorDb::class.java,
                        "kalkulator.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}