package com.app.greenpass.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.models.TestModel

@Entity
data class Tests(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "PERSON_CODE") val personCode: Int,
        @ColumnInfo(name = "TEST_DATE") val testDate: String,
        @ColumnInfo(name = "TEST_RESULT") val testResult: Boolean,
        @ColumnInfo(name = "ANTIBODIES") val antibodies: Boolean,
)

fun TestModel.toMap() = Tests(
        personCode = this.owner,
        testDate = this.testDate,
        testResult = this.testResult,
        antibodies = this.antibodies,
)

fun Tests.toMap() = TestModel(
        owner = this.personCode,
        testDate = this.testDate,
        testResult = this.testResult,
        antibodies = this.antibodies,
)
