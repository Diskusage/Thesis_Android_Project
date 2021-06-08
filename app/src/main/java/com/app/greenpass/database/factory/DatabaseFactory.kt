package com.app.greenpass.database.factory

import android.content.Context
import com.app.greenpass.database.room.AppDatabase
import com.app.greenpass.database.enums.Bases
import com.app.greenpass.database.firebase.FirebaseStorage

class DatabaseFactory {
    companion object{
        fun getDatabase(type: Bases, context: Context) : IStorage {
            return when (type){
                Bases.Firebase -> FirebaseStorage()
                Bases.Room -> AppDatabase.getInstance(context)
            }
        }
    }
}