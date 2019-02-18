package com.projects.enzoftware.barcodereader.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.projects.enzoftware.barcodereader.model.BarcodeEntity

@Dao
interface BarcodeDao {
    @Query("Select * from barcode")
    fun getAllBarcodes() : LiveData<List<BarcodeEntity>>

    @Query("Delete from barcode")
    fun cleanDB()

    @Delete
    fun deleteBarcode(barcodeEntity: BarcodeEntity)

    @Insert(onConflict = REPLACE)
    fun insertNewBarcode(barcodeEntity: BarcodeEntity)
}