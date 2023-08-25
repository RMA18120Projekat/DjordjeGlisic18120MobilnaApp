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
    //SVI KORISNICI IZ BAZE
    private var korisnici:ArrayList<User> = ArrayList()
    private val _users=MutableLiveData<ArrayList<User>>()
     val users:LiveData<ArrayList<User>>
        get()=_users
    //SVA MESTA IZ BAZE
    private var mojaMesta:ArrayList<Places> = ArrayList()
    private val _myPlaces= MutableLiveData<ArrayList<Places>>()
    val myPlaces: LiveData<ArrayList<Places>>
        get()=_myPlaces
    //SVI KOMENTARI IZ BAZE
    private var komentari:ArrayList<Comments> = ArrayList()
    private val _comments= MutableLiveData<ArrayList<Comments>>()
    val comments: LiveData<ArrayList<Comments>>
        get()=_comments
    //VEZA 1 TO 1 KOMENTAR KORISNIK
    private var podrzao:ArrayList<KorisnikKomentarP> = ArrayList()
    private val _osobePodrzale=MutableLiveData<ArrayList<KorisnikKomentarP>>()
    val osobePodrzale:LiveData<ArrayList<KorisnikKomentarP>>
    get()=_osobePodrzale
    //NIZ FILITRIRANIH MESTA
    private var nizFiltriranihMesta : ArrayList<Places> = ArrayList()

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

        //SVE KOMENTARE IZ BAZE UZMI
        DataBase.databaseComments.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var nizKomentara=ArrayList<Comments>()
                for(commentSnapshot in snapshot.children)
                {


                    val comment=commentSnapshot.getValue(Comments::class.java)
                    comment?.let {
                            //IF ZA MESTO SPECIFICNO CE BITI NAPISAN KASNIJE
                            nizKomentara.add(it)


                    }


                }
                komentari.clear()
                komentari.addAll(nizKomentara)
                _comments.postValue(komentari)

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
            }

        })
        //SVI KOJI SU PODRZALI KOMENTARE IZ BAZE
        DataBase.dataBaseOneToOne.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedO= ArrayList<KorisnikKomentarP>()
                for(korisnik in snapshot.children)
                {
                    val el=korisnik.getValue(KorisnikKomentarP::class.java)
                    el?.let {
                        updatedO.add(it)
                    }
                }
                podrzao.clear()
                podrzao.addAll(updatedO)
                _osobePodrzale.postValue(podrzao)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG,"Greska prilikom preuzimanja korisnika sa baze ${error.message}")
            }
        })
    }




}