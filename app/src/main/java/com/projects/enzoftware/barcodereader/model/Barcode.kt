package com.projects.enzoftware.barcodereader.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "barcode")
class Barcode(code: String) {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    @ColumnInfo(name = "code")
    var code:String ?= code

}