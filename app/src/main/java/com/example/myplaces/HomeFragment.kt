package com.example.myplaces

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class HomeFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var textIme:TextView
    lateinit var database:DatabaseReference
    val sharedViewModel:KorisnikViewModel by activityViewModels()
    val locationViewModel:LocationViewModel by activityViewModels()
    lateinit var ucitaj:ProgressBar
    lateinit var mojaMesta:Button
    lateinit var dodajMesto:Button
    lateinit var profilna:ImageView
    lateinit var komentarisiMesto:Button
    lateinit var svojiKomentari:Button
    lateinit var pretrazi:Button
    private lateinit var map:MapView

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
                    sharedViewModel.user=User(snapshot.child("korisnicko").value.toString(),snapshot.child("sifra").value.toString(),snapshot.child("ime").value.toString(),snapshot.child("prezime").value.toString(),snapshot.child("brojTelefona").value.toString().toLongOrNull(),snapshot.child("img").value.toString(),ArrayList(),snapshot.child("bodovi").value.toString().toInt())
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

        komentarisiMesto=view.findViewById(R.id.buttonKomentarisiOcena)
        komentarisiMesto.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_komentarOcenaFragment)
        }
        svojiKomentari=view.findViewById(R.id.buttonSvojiKomentari)
        svojiKomentari.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_svojiKomentariFragment)
        }
        pretrazi=view.findViewById(R.id.buttonPretraziObjekat)
        pretrazi.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_pretraziObjekatFragment)
        }
        var vidiListuKorisnika=view.findViewById<Button>(R.id.buttonVidiListuKorisnika)
        vidiListuKorisnika.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_rangListaFragment)
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = activity?.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map = requireView().findViewById(R.id.mapGPS)
        map.setMultiTouchControls(true)
        var startPoint: GeoPoint = GeoPoint(43.158495, 22.585555)
        map.controller.setCenter(startPoint)


        setupLocation()

    }

    private fun setupLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    setMyLocationOverlay()
                    obeleziSveObjekteNaMapi()

                }
            }

            // Pokretanje zahtjeva za dozvolom
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            setUpMap()
        }
    }

    private fun setMyLocationOverlay() {
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
        map.controller.setCenter(myLocationOverlay.myLocation)
    }
    private fun obeleziSveObjekteNaMapi()
    {

        for(mojeMesto in sharedViewModel.getMyPlaces())
        {
            val sPoint= GeoPoint(mojeMesto.latituda!!.toDouble(),mojeMesto.longituda!!.toDouble())
            val marker = Marker(map)
            marker.position = sPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM) // Postavljanje tačke spajanja markera
            marker.title = mojeMesto.naziv
            marker.subDescription = "Mesto je dodao:"+mojeMesto.autor+"<br>"+"Teren je:"+mojeMesto.teren+"<br>"+"Ocena:"+mojeMesto.ocena+"<br>"+"Dimenzije:"+mojeMesto.dimenzije+"<br>"+"Rasveta:"+mojeMesto.rasveta+"<br>"+"Prosecan broj ljudi:"+mojeMesto.prosecanBrojLjudi.toString()+"<br>"+"Posecenost:"+mojeMesto.posecenost+"<br>"
            if(mojeMesto.teren=="Kosarkaski")
            {
                marker.subDescription+="Sirinca obruca:"+mojeMesto.sirinaObruca+"<br>"+"Osobina obruca:"+mojeMesto.osobinaObruca+"<br>"+"Podloga:"+mojeMesto.podlogaKosarka+"<br>"+"Mrezica:"+mojeMesto.mrezica+"<br>"+"Visina kosa:"+mojeMesto.visinaKosa
            }
            else if(mojeMesto.teren=="Fudbalski")
            {
                marker.subDescription+="Mreza"+mojeMesto.mreza+"<br>"+"Golovi:"+mojeMesto.golovi+"<br>"+"Podloga:"+mojeMesto.podlogaFudbal
            }

            // Dodavanje markera na mapu
            map.overlays.add(marker)
            map.invalidate()



        }

    }
    private fun setUpMap()
    {

        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        map.overlays.add(myLocationOverlay)
        map.controller.setCenter(myLocationOverlay.myLocation)
        map.invalidate()
        var start = GeoPoint(
            locationViewModel.getSvojaLati(),
            locationViewModel.getSvojaLongi()
        )

        var startPoint:GeoPoint= start

        map.controller.setZoom(14.0)
        map.invalidate()

        obeleziSveObjekteNaMapi()
        map.controller.animateTo(startPoint)



    }


}