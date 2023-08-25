package com.example.myplaces

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private var korisnici:ArrayList<User> = ArrayList()
    private val _users=MutableLiveData<ArrayList<User>>()
     val users:LiveData<ArrayList<User>>
        get()=_users
    //MESTA sva,svoja,tudja
    private var mojaMesta:ArrayList<Places> = ArrayList()
    private val _myPlaces= MutableLiveData<ArrayList<Places>>()
    val myPlaces: LiveData<ArrayList<Places>>
        get()=_myPlaces
    private var svoje:ArrayList<Places> = ArrayList()
    private var tudje:ArrayList<Places> = ArrayList()
    private var nizFiltriranihMesta : ArrayList<Places> = ArrayList()
    //KOMENTARI SVI ZA MESTO(MADA SE TO KASNIJE PROVERAVA),SVI KLJUCEVI ZA SVI KOMENTARI I SVI ZA JEDNO MESTO(TO SE ODMAH PROVERAVA)
    private var nizKomentara:ArrayList<Comments> = ArrayList()
    private var nizKljuceva:ArrayList<String> = ArrayList()
    private var nizMesnihKomentara:ArrayList <Comments> =ArrayList()


    fun getNizMesnihKomentara():ArrayList<Comments>
    {
        return nizMesnihKomentara
    }
    fun setNizMesnihKomentara(niz:ArrayList<Comments>)
    {
        nizKomentara=niz
    }
    fun getNizKomentara():ArrayList<Comments>
    {
        return nizKomentara
    }
    fun setNizKomentara(niz:ArrayList<Comments>)
    {
        nizKomentara=niz
    }
    fun getNizKljuceva():ArrayList<String>
    {
        return nizKljuceva
    }
    fun setNizKljuceva(niz:ArrayList<String>)
    {
        nizKljuceva=niz
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

    fun setFiltriranaMesta(newFM: ArrayList<Places>) {
        nizFiltriranihMesta = newFM
    }
    fun getNizFIltriranihMesta(): ArrayList<Places> {
        return nizFiltriranihMesta
    }
////////////KOORDINATE SVIH MESTA
private  var koordinate:ArrayList<Koordinate> = ArrayList<Koordinate>()
    fun getKoordinate():ArrayList<Koordinate>
    {
        return koordinate
    }
    fun setKoordinate(koor:ArrayList<Koordinate>)
    {
        koordinate=koor
    }
    fun addOne(koor:Koordinate)
    {
        koordinate.add(koor)
    }

    init
    {
        DataBase.databaseUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedUsers= ArrayList<User>()
                for(korisnik in snapshot.children)
                {
                    val el=korisnik.getValue(User::class.java)
                    el?.let {
                        updatedUsers.add(it)
                    }
                }
                korisnici.clear()
                korisnici.addAll(updatedUsers)
                _users.postValue(korisnici)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG,"Greska prilikom preuzimanja korisnika sa baze ${error.message}")
            }
        })
        //SVA MESTA UZMI IZ BAZE
        DataBase.databasePlaces.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedPlaces = ArrayList<Places>()
                for (placeSnapshot in snapshot.children) {
                    val place = placeSnapshot.getValue(Places::class.java)
                    place?.let {
                        updatedPlaces.add(it)

                    }
                }

                mojaMesta.clear() // Obrišite postojeće podatke iz liste
                mojaMesta.addAll(updatedPlaces) // Ažurirajte listu sa novim podacima
                _myPlaces.postValue(mojaMesta)

            }

            override fun onCancelled(error: DatabaseError) {
                // Greška pri dohvaćanju podataka
                Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
            }
        })
        //SVI KOMENTARI UZMI IZ BAZE
        DataBase.databaseComments.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nizKomentara.clear()
                for(commentSnapshot in snapshot.children)
                {


                    val comment=commentSnapshot.getValue(Comments::class.java)
                    comment?.let {
                        if(it.autor.toString()==ime.toString())
                        {
                            //IZDVOJI SAMO KOJE JE TRENUTNI KORISNIK NAPISAO
                            nizKomentara.add(it)
                            nizKljuceva.add(it.id)

                        }
                    }


                }

            }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
                }

        })
        //SVE KOMENTARE IZ BAZE UZMI
        DataBase.databaseComments.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nizMesnihKomentara.clear()
                for(commentSnapshot in snapshot.children)
                {


                    val comment=commentSnapshot.getValue(Comments::class.java)
                    comment?.let {
                            //IF ZA MESTO SPECIFICNO CE BITI NAPISAN KASNIJE
                            nizMesnihKomentara.add(it)


                    }


                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
            }

        })
    }




}