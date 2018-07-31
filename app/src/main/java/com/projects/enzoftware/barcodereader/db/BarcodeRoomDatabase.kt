package com.projects.enzoftware.barcodereader.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.google.android.gms.vision.barcode.Barcode

@Database(entities = [(Barcode::class)], version = 1)
abstract class BarcodeRoomDatabase : RoomDatabase() {

}