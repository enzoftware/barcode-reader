package com.projects.enzoftware.barcodereader.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by enzoftware on 3/4/18.
 */
@Entity(tableName = "barcode")
class Barcode{
    @PrimaryKey(autoGenerate = true)
    var id:String ?= null

    @ColumnInfo(name = "code")
    var code:String ?= null

    constructor(code:String){
        this.code = code
    }
}