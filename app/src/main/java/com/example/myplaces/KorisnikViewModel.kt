package com.example.myplaces

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.osmdroid.views.overlay.Marker

class KorisnikViewModel: ViewModel() {
    var ime:String=""
    var img:String=""
    var izabranoMesto:String=""
    var longituda=""
    var latituda=""
    var user:User=User()
    private var myPlaces: ArrayList<Places> = ArrayList()

    fun getMyPlaces(): ArrayList<Places> {
        return myPlaces
    }

    fun setMyPlaces(newMyPlaces: ArrayList<Places>) {
        myPlaces = newMyPlaces
    }
    init
    {

        DataBase.databasePlaces.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myPlaces.clear() // Očisti listu prije dodavanja novih podataka

                for (placeSnapshot in snapshot.children) {
                    val place = placeSnapshot.getValue(Places::class.java)
                    place?.let {
                        myPlaces.add(it)
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Greška pri dohvaćanju podataka
                Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
            }
        })
    }


}