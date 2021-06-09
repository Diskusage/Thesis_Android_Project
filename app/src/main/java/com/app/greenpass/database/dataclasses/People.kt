package com.app.greenpass.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.models.PersonModel

@Entity
data class People(
    @ColumnInfo(name = "FIRST_NAME") val fName: String,
    @ColumnInfo(name = "SECOND_NAME") val sName: String,
    @ColumnInfo(name = "IDNP") val idnp: String,
    @PrimaryKey(autoGenerate = false) val personCode: Int,
)

fun PersonModel.toMap() = People(
    fName = this.firstName,
    sName = this.secondName,
    idnp = this.iDNP,
    personCode = this.hashCode(),
)

fun People.toMap() = PersonModel(
    firstName = this.fName,
    secondName = this.sName,
    iDNP = this.idnp,

    )


