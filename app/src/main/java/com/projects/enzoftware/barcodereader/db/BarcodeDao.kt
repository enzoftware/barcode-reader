package com.projects.enzoftware.barcodereader.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.google.android.gms.vision.barcode.Barcode

@Dao
interface BarcodeDao {
    @Query("Select * from barcode")
    fun getAllBarcodes() : LiveData<Barcode>

    @Delete
    fun deleteBarcode(barcode: Barcode)

    @Insert(onConflict = REPLACE)
    fun insertNewBarcode(barcode: Barcode)
}