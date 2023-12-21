package com.cashcue.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cashcue.R
import com.cashcue.data.local.room.dao.AnggaranDao
import com.cashcue.data.local.room.dao.RiwayatKeuanganDao
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.data.local.room.entity.RiwayatKeuangan
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Database(
    entities = [Anggaran::class, RiwayatKeuangan::class],
    version = 1,
    exportSchema = false
)
abstract class CashcueDatabase: RoomDatabase() {
    abstract fun anggaranDao(): AnggaranDao
    abstract fun riwayatKeuanganDao(): RiwayatKeuanganDao

    companion object {

        @Volatile
        private var INSTANCE: CashcueDatabase? = null

        fun getInstance(context: Context): CashcueDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CashcueDatabase::class.java,
                    "cashcue.db"
                )
//                    .addCallback(object : Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            INSTANCE.let {
//                                Executors.newSingleThreadExecutor().execute {
//                                    if (it != null) {
//                                        fillWithStartingData(
//                                            context.applicationContext,
//                                            it.anggaranDao(),
//                                            it.riwayatKeuanganDao()
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    })
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(
            context: Context,
            anggaranDao: AnggaranDao,
            riwayatKeuanganDao: RiwayatKeuanganDao
        ) {
            val anggaran = loadJsonArrayAnggaran(context)
            val riwayatKeuangan = loadJsonArrayRiwayatKeuangan(context)
            try {
                if (anggaran != null) {
                    for (i in 0 until anggaran.length()) {
                        val item = anggaran.getJSONObject(i)
                        anggaranDao.insertAll(
                            Anggaran(
                                idUser = item.getInt("idUser"),
                                jenis = item.getInt("jenis"),
                                nama = item.getString("nama"),
                                saldo = item.getInt("saldo"),
                                saldoTarget = item.getInt("saldoTarget")
                            )
                        )
                    }
                }
                if (riwayatKeuangan != null) {
                    for (i in 0 until riwayatKeuangan.length()) {
                        val item = riwayatKeuangan.getJSONObject(i)
                        riwayatKeuanganDao.insertAll(
                            RiwayatKeuangan(
                                idUser = item.getInt("idUser"),
                                idAnggaran = item.getInt("idAnggaran"),
                                tanggal = item.getLong("tanggal"),
                                saldo = item.getInt("saldo")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArrayAnggaran(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.anggaran)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("anggaran")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }

        private fun loadJsonArrayRiwayatKeuangan(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.riwayat_keuangan)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("riwayat_keuangan")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }

    }
}