package com.example.myplaces

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MojaMestaFragment : Fragment() {
    private lateinit var MyPlaces: ArrayList<Places>
    val sharedViewModel: KorisnikViewModel by activityViewModels()
    lateinit var progresBar: ProgressBar
    lateinit var  listView: ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moja_mesta, container, false)
        MyPlaces = ArrayList()
        progresBar = view.findViewById(R.id.ucitajMojaMesta)
        progresBar.visibility = View.VISIBLE
        listView= view.findViewById(R.id.mestaView)

        DataBase.databasePlaces.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                MyPlaces.clear()
                for (placeSnapshot in snapshot.children) {
                    val place = placeSnapshot.getValue(Places::class.java)
                    place?.let {
                        MyPlaces.add(it)
                    }
                }

                updateListView()
            }

            override fun onCancelled(error: DatabaseError) {
                // Greška pri dohvaćanju podataka
                Log.e(TAG, "Error fetching Places data: ${error.message}")
            }
        })
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var mojeMesto: String = p0?.adapter?.getItem(p2) as String
                sharedViewModel.izabranoMesto=mojeMesto
                sharedViewModel.azurirajBrisi=true
                sharedViewModel.komentarisi=false
                findNavController().navigate(R.id.action_mojaMestaFragment_to_detaljniFragment2)
            }
        })

        return view
    }

    private fun updateListView() {
        val imena = ArrayList<String>()

        for (name in MyPlaces) {
            if (name.autor == sharedViewModel.ime) {
                imena.add(name.naziv.toString())
            }
        }

        if (isAdded) {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, imena)
            listView.adapter = adapter
            progresBar.visibility = View.GONE
        }
    }

}
