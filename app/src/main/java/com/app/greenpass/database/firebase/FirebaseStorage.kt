package com.app.greenpass.database.firebase

import com.app.greenpass.database.daos.DaoFireStorePerson
import com.app.greenpass.database.daos.DaoFireStoreTest
import com.app.greenpass.database.daos.DaoFireStoreVaccination
import com.app.greenpass.database.factory.IStorage
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseStorage : IStorage {

    private val firebaseFireStore : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun daoPerson(): DaoFireStorePerson {
        return DaoFireStorePerson(firebaseFireStore)
    }
    fun daoVaccinations(): DaoFireStoreVaccination{
        return DaoFireStoreVaccination(firebaseFireStore)
    }
    fun daoTest(): DaoFireStoreTest{
        return  DaoFireStoreTest(firebaseFireStore)
    }

}