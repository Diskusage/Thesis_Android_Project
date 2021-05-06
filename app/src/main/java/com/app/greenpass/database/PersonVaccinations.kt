package com.app.greenpass.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.enums.Vaccines
import com.app.greenpass.models.PersonModel

@Entity
data class PersonVaccinations(
        //@PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "PERSON_FIRSTNAME") val firstName: String,
        @ColumnInfo(name = "PERSON_SECONDNAME") val secondName: String,
        @ColumnInfo(name = "PERSON_IDNP") val iDnp: String,
        @ColumnInfo(name = "PERSON_VACCINE") val vaccineType: Int,
        @ColumnInfo(name = "PERSON_DATE") val vaccinationDate: String,
        @PrimaryKey(autoGenerate = false) val personalCode: String
)

fun PersonModel.toMap() = PersonVaccinations(
        firstName = this.firstName,
        secondName = this.secondName,
        iDnp = this.iDNP,
        vaccineType = this.type.t,
        vaccinationDate = this.vaccDate,
        personalCode = firstName + secondName + iDNP + vaccDate + type.toString()
)
fun PersonVaccinations.toMap() = PersonModel(
        firstName = this.firstName,
        secondName = this.secondName,
        iDNP = this.iDnp,
        type = Vaccines.fromInt(vaccineType),
        date = this.vaccinationDate
)

