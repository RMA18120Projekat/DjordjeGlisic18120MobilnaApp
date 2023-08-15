package com.example.myplaces

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.osmdroid.views.overlay.Marker

class KorisnikViewModel: ViewModel() {
    var id:Int=0
    var ime:String=""
    var img:String=""
    var izabranoMesto:String=""
    var longituda=""
    var latituda=""
    var user:User=User()
    var place:Places=Places()
    private var myPlaces: ArrayList<Places> = ArrayList()
    private var svoje:ArrayList<Places> = ArrayList()
    private var tudje:ArrayList<Places> = ArrayList()

    fun getMyPlaces(): ArrayList<Places> {
        return myPlaces
    }

    fun setMyPlaces(newMyPlaces: ArrayList<Places>) {
        myPlaces = newMyPlaces
    }
    fun getSvoje(): ArrayList<Places> {
        return svoje
    }
    fun setSvoje(newSvoje: ArrayList<Places>) {
        svoje = newSvoje
    }

    fun setTudje(newTudje: ArrayList<Places>) {
        tudje = newTudje
    }
    fun getTudje(): ArrayList<Places> {
        return tudje
    }



    init
    {

        DataBase.databasePlaces.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedPlaces = ArrayList<Places>()
                for (placeSnapshot in snapshot.children) {
                    val place = placeSnapshot.getValue(Places::class.java)
                    place?.let {
                        updatedPlaces.add(it)

                    }
                }

                myPlaces.clear() // Obrišite postojeće podatke iz liste
                myPlaces.addAll(updatedPlaces) // Ažurirajte listu sa novim podacima
            }

            override fun onCancelled(error: DatabaseError) {
                // Greška pri dohvaćanju podataka
                Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
            }
        })


    }


}