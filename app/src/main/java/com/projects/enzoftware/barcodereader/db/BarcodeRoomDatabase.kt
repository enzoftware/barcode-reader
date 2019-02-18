package com.projects.enzoftware.barcodereader.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.projects.enzoftware.barcodereader.model.Barcode

@Database(entities = arrayOf(Barcode::class), version = 2)
abstract class BarcodeRoomDatabase : RoomDatabase() {
    abstract fun barcode() : BarcodeDao

    companion object {
        private lateinit var appDatabase : BarcodeRoomDatabase
        fun getInstance(context: Context) : BarcodeRoomDatabase{
            if(appDatabase == null){
                synchronized(BarcodeRoomDatabase::class){
                    appDatabase = Room  .databaseBuilder(context.applicationContext, BarcodeRoomDatabase::class.java,"barcode.db")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return appDatabase
        }
    }
}