package com.example.myplaces

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var textIme:TextView
    lateinit var database:DatabaseReference
    val sharedViewModel:KorisnikViewModel by activityViewModels()
    val locationViewModel:LocationViewModel by activityViewModels()
    val storage = FirebaseStorage.getInstance()
    lateinit var ucitaj:ProgressBar
    val storageRef = storage.reference
    //val imageRef=storageRef.child(sharedViewModel.img)
    private lateinit var storageReference: StorageReference
    lateinit var mojaMesta:Button
    lateinit var dodajMesto:Button
    lateinit var mape:ImageView
    lateinit var profilna:ImageView

    private lateinit var prezimeBaza:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home,container,false)
        textIme=view.findViewById(R.id.textViewKorisnikHome)
        auth=FirebaseAuth.getInstance()
        ucitaj=view.findViewById(R.id.ucitajImeSlika)
        profilna=view.findViewById(R.id.profilna)
        prezimeBaza=view.findViewById(R.id.textViewKorisnikPrezime)

        setHasOptionsMenu(true)
        try {
            ucitaj.visibility=View.VISIBLE
            database = FirebaseDatabase.getInstance().getReference("Users")
            val key = sharedViewModel.ime.replace(".", "").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
            database.child(key).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    textIme.text = snapshot.child("ime").value.toString()
                    sharedViewModel.user=User(snapshot.child("korisnicko").value.toString(),snapshot.child("sifra").value.toString(),snapshot.child("ime").value.toString(),snapshot.child("prezime").value.toString(),snapshot.child("brojTelefona").value.toString().toLongOrNull(),snapshot.child("img").value.toString())
                    prezimeBaza.text=snapshot.child("prezime").value.toString()
                    val imageName=snapshot.child("img").value.toString()
                    if(imageName!="")
                    {
                        Glide.with(requireContext())
                            .load(imageName)
                            .into(profilna)
                    }
                    ucitaj.visibility=View.GONE
                }
            }.addOnFailureListener { exception ->
                // Handle the exception here
                Log.e(TAG, "Error getting data from Firebase: ${exception.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error accessing Firebase: ${e.message}")
        }






        mojaMesta=view.findViewById(R.id.buttonMojaMesta)
        mojaMesta.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_mojaMestaFragment)
        }
        dodajMesto=view.findViewById(R.id.buttonDodajObjekat)
        dodajMesto.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_dodajMestoFragment)
        }
        mape=view.findViewById(R.id.imageViewMape)
        mape.setOnClickListener{

            locationViewModel.samoPregled=true
            locationViewModel.dodajObjekat=false
            locationViewModel.jedanObjekat=false
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment)

        }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.fragment_login)
        {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

        }
        if(item.itemId==R.id.fragment_info)
        {
            findNavController().navigate(R.id.action_homeFragment_to_infoFragment)
        }
        if(item.itemId==R.id.fragment_edit)
        {
            findNavController().navigate(R.id.action_homeFragment_to_editFragment)

        }

        return NavigationUI.onNavDestinationSelected(item!!,requireView().findNavController())||super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu,menu)
    }


}