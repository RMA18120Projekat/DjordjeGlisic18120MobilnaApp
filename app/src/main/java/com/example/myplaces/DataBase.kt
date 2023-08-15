package com.example.myplaces

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DataBase {
    companion object{

        val auth: FirebaseAuth= FirebaseAuth.getInstance()
        private val database = FirebaseDatabase.getInstance()
        val databaseUsers: DatabaseReference= database.getReference("Users")
        val databasePlaces:DatabaseReference= database.getReference("Places")
        var databaseComments:DatabaseReference= database.getReference("Comments")
        val storageRef: StorageReference= FirebaseStorage.getInstance().reference


    }
}