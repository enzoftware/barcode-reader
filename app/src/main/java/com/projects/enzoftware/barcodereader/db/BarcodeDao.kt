package com.projects.enzoftware.barcodereader.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.projects.enzoftware.barcodereader.model.Barcode

@Dao
interface BarcodeDao {
    @Query("Select * from barcode")
    fun getAllBarcodes() : LiveData<List<Barcode>>

    @Query("Delete from barcode")
    fun cleanDB()

    @Delete
    fun deleteBarcode(barcode: Barcode)

    @Insert(onConflict = REPLACE)
    fun insertNewBarcode(barcode: Barcode)
}