package com.example.myplaces

import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.lang.Math.cos
import java.lang.Math.sin

class MapFragment : Fragment() {
    private lateinit var map: MapView

    private val locationViewModel:LocationViewModel by activityViewModels()
    private val sharedViewModel:KorisnikViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = activity?.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map = requireView().findViewById(R.id.map)
        map.setMultiTouchControls(true)


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
                    setOnMapClickOverlay()

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
    private fun setOnMapClickOverlay()
    {
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
        map.controller.setCenter(myLocationOverlay.myLocation)
       var recive=object:MapEventsReceiver{
           override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
               val clickedPoint = p ?: return false


               var startPoint =GeoPoint(myLocationOverlay.myLocation.latitude ,myLocationOverlay.myLocation.longitude)
               var endPoint = GeoPoint( clickedPoint.latitude , clickedPoint.longitude)

               if ( startPoint.distanceToAsDouble(endPoint)<= 1000) {
                   if (locationViewModel.dodajObjekat == true||locationViewModel.jedanObjekat==true) {
                       for (koordinata in locationViewModel.getKoordinate()) {
                           var userPoint = GeoPoint(koordinata.latituda, koordinata.longituda)
                           if (endPoint.distanceToAsDouble(userPoint) < 30) {
                               Toast.makeText(
                                   context,
                                   "Ne mozete da izabere datu lokaciju jer je izabrana od strane drugog korisnika",
                                   Toast.LENGTH_SHORT
                               ).show()
                               return false


                           }
                       }
                       val lon = clickedPoint.longitude.toString()
                       val lati = clickedPoint.latitude.toString()
                       locationViewModel.setLocation(lon, lati)
                       findNavController().popBackStack()
                       return true

                   }
                   else
                   {
                       return false
                   }
               }
               else {
                   Toast.makeText(context,"Ne mozete da izabere datu lokaciju jer je dalja od 1km (nije Vam u blizini)",Toast.LENGTH_SHORT).show()
                   return false

               }
           }
           override fun longPressHelper(p: GeoPoint?): Boolean {
               return false
           }
       }
        var overlayEvents=MapEventsOverlay(recive)
        map.overlays.add(overlayEvents)

    }
    private fun obeleziSveObjekteNaMapi()
    {

        for(mojeMesto in sharedViewModel.getMyPlaces())
        {
            val sPoint= GeoPoint(mojeMesto.latituda!!.toDouble(),mojeMesto.longituda!!.toDouble())
            locationViewModel.addOne(Koordinate(mojeMesto.latituda!!.toDouble(),mojeMesto.longituda!!.toDouble(),mojeMesto.naziv.toString()))
            val marker = Marker(map)
            marker.position = sPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM) // Postavljanje tačke spajanja markera
            marker.title = mojeMesto.naziv
            marker.subDescription = "Mesto je dodao:"+mojeMesto.autor+"<br>"+"Teren je:"+mojeMesto.teren+"<br>"+"Ocena:"+mojeMesto.ocena+"<br>"+"Dimenzije:"+mojeMesto.dimenzije+"<br>"+"Posecenost:"+mojeMesto.posecenost+"<br>"
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
    private fun obeleziTudjeObjekte()
    {

        for(mojeMesto in sharedViewModel.getMyPlaces())
        {
            //AKO SE IZ NIZA SVIH MESTA NADJE NEKI KOGA NIJE DODAO TRENUTNI KORISNIK
            if(mojeMesto.autor!=sharedViewModel.ime) {
                //DODAJ TO MESTO U NIZ KOORDINATA
                locationViewModel.addOne(Koordinate(mojeMesto.latituda!!.toDouble(), mojeMesto.longituda!!.toDouble(),mojeMesto.naziv.toString()))
                //OBELEZI GA
                val sPoint= GeoPoint(mojeMesto.latituda!!.toDouble(),mojeMesto.longituda!!.toDouble())
                val marker = Marker(map)
                marker.position = sPoint
                marker.setAnchor(
                    Marker.ANCHOR_CENTER,
                    Marker.ANCHOR_BOTTOM
                ) // Postavljanje tačke spajanja markera
                marker.title = mojeMesto.naziv
                marker.subDescription =
                    "Mesto je dodao:" + mojeMesto.autor + "<br>" + "Teren je:" + mojeMesto.teren + "<br>" + "Ocena:" + mojeMesto.ocena + "<br>" + "Dimenzije:" + mojeMesto.dimenzije + "<br>" + "Posecenost:" + mojeMesto.posecenost + "<br>"
                if (mojeMesto.teren == "Kosarkaski") {
                    marker.subDescription += "Sirinca obruca:" + mojeMesto.sirinaObruca + "<br>" + "Osobina obruca:" + mojeMesto.osobinaObruca + "<br>" + "Podloga:" + mojeMesto.podlogaKosarka + "<br>" + "Mrezica:" + mojeMesto.mrezica + "<br>" + "Visina kosa:" + mojeMesto.visinaKosa
                } else if (mojeMesto.teren == "Fudbalski") {
                    marker.subDescription += "Mreza" + mojeMesto.mreza + "<br>" + "Golovi:" + mojeMesto.golovi + "<br>" + "Podloga:" + mojeMesto.podlogaFudbal
                }

                // Dodavanje markera na mapu
                map.overlays.add(marker)
                map.invalidate()
            }



        }

    }
    private fun dozvoliKlikNaTudjiObjekat() {
        val myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
        map.controller.setCenter(myLocationOverlay.myLocation)
        Toast.makeText(context,"Prikazuju se mesta drugih korisnika",Toast.LENGTH_SHORT).show()
        var recive = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                val clickedPoint = p ?: return false
                var startPoint = GeoPoint(myLocationOverlay.myLocation.latitude, myLocationOverlay.myLocation.longitude)
                var endPoint = GeoPoint(clickedPoint.latitude, clickedPoint.longitude)
                if(startPoint.distanceToAsDouble(endPoint)<1000)
                {
                    var i:Int=0
                    for(koordinate in locationViewModel.getKoordinate())
                    {
                        var objPoint=GeoPoint(koordinate.latituda,koordinate.longituda)
                        if(endPoint.distanceToAsDouble(objPoint)<=30)
                        {
                            val lon = clickedPoint.longitude.toString()
                            val lati = clickedPoint.latitude.toString()
                            locationViewModel.setLocationAndName(lon, lati,koordinate.naziv.toString(),true)
                            findNavController().popBackStack()
                            return true
                        }
                        else
                        {
                            Toast.makeText(context,"Nema objekta na toj lokacii",Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                    return false

                }
                else
                {
                    Toast.makeText(context,"Ne mozete da kometarisete objekte dalje od 1km",Toast.LENGTH_SHORT).show()
                    return false
                }

            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }


        }
        var overlayEvents=MapEventsOverlay(recive)
        map.overlays.add(overlayEvents)

    }
    private fun obeleziObjekatNaMapi()
    {
        var startTacka= GeoPoint(sharedViewModel.latituda.toDouble(),sharedViewModel.longituda.toDouble())
        val marker = Marker(map)
        marker.position = startTacka
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM) // Postavljanje tačke spajanja markera
        marker.title = sharedViewModel.izabranoMesto
        marker.subDescription = "Mesto je dodao:${sharedViewModel.ime}"
        map.overlays.add(marker)
        map.invalidate()

    }
    private fun setUpMap()
    {
        var startPoint:GeoPoint= GeoPoint(43.158495, 22.585555)
        map.controller.setZoom(15.0)

                //Kad izabere samo mapu
        if(locationViewModel.samoPregled==true)
        {
            setMyLocationOverlay()
            obeleziSveObjekteNaMapi()

        }
        else {
            //Za dodavanje objekata
            if (locationViewModel.dodajObjekat==true)
            {

                setMyLocationOverlay()
                obeleziSveObjekteNaMapi()

                setOnMapClickOverlay()


            }
            //Kad izabere Neki objekat iz liste objekata
            else if(locationViewModel.jedanObjekat==true)
            {
                obeleziObjekatNaMapi()
                setOnMapClickOverlay()

                setMyLocationOverlay()


            }
            else if(locationViewModel.komentarisiObjekat==true)
            {
                obeleziTudjeObjekte()
                dozvoliKlikNaTudjiObjekat()
                setMyLocationOverlay()
            }
            else
            {
                setMyLocationOverlay()
            }
        }
        map.controller.animateTo(startPoint)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
