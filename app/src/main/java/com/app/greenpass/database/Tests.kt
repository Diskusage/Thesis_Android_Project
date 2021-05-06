package com.app.greenpass.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.models.TestModel

@Entity
data class Tests(
        //@PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "IDNP") val iDnp: String,
        @ColumnInfo(name = "TEST_DATE") val testDate: String,
        @ColumnInfo(name = "TEST_RESULT") val testResult: Int,
        @ColumnInfo(name = "ANTIBODIES") val antibodies: Int,
        @PrimaryKey(autoGenerate = false) val testCode: String

)

fun TestModel.toMap() = Tests(
        iDnp = this.idnp,
        testDate = this.testDate,
        testResult = if (this.testResult) 1 else 0,
        antibodies = if (this.isAntibodies) 1 else 0,
        testCode = idnp+testDate+testResult+isAntibodies
)

fun Tests.toMap() = TestModel(
        idnp = this.iDnp,
        testDate = this.testDate,
        testResult = this.testResult == 1,
        antibodies = this.antibodies == 1
)
