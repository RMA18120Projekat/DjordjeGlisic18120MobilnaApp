package com.example.myplaces

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class DetaljniFragment : Fragment() {

    private  val sharedViewModel:KorisnikViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()

/////////////////////////////////////////////////////////////////////////////////
    private  var imgUrl:String=""
    private val REQUEST_IMAGE_CAPTURE = 1
    val GALLERY_PERMISSION_REQUEST_CODE = 1002
    val CAMERA_PERMISSION_REQUEST_CODE = 1001
/////////////////////////////////////////////////////////////////////////////
    private lateinit var slika:ImageView
    private lateinit var naziv:TextView
    private lateinit var opis:EditText
    private lateinit var ocena:EditText
    private lateinit var azuriraj: Button
    private lateinit var ucitaj:ProgressBar
    private lateinit var back:Button
    private lateinit var obrisi:Button
    private lateinit var latituda:EditText
    private lateinit var longituda:EditText
    private lateinit var staraLatituda:TextView
    private lateinit var staraLongituda:TextView
    private lateinit var svojiKomentari:Button
    //////////////////////////////////////
    ////////////////////////////////////////

    lateinit var teren: Spinner

    var terenIzabran:String="Fudbalski"
    /////////////////////////////////////////
    lateinit var sirinaObruca: Spinner
    lateinit var sirinaText:TextView
    private  var sirinaIzabrana:String="Normlna sirina"
    lateinit var osobinaObruca: Spinner
    lateinit var osobinaText:TextView
    var osobinaIzabrana:String="Normalan osecaj"
    lateinit var podlogaKosarka: Spinner
    lateinit var podlogaKText:TextView
    var podlogaKIzabrana:String="Guma"
    lateinit var mrezica: Spinner
    lateinit var mrezicaText:TextView
    var mrezicaIzabrana:String="Ima"
    lateinit var kosevi: Spinner
    lateinit var koseviText:TextView
    var koseviIzabrana:String="305cm"
    ///////////////////////////////////////
    lateinit var posecenost: Spinner
    var posecenostIzabrana:String="Mala"

    lateinit var dimenzije: Spinner
    var dimenzijeIzabrana:String="Male"
    //////////////////////////////////////
    lateinit var mreza: Spinner
    lateinit var mrezaText:TextView
    var mrezaIzabrana:String="Ima"
    lateinit var golovi: Spinner
    lateinit var goloviText:TextView
    var goloviIzabrana:String="Manji"
    lateinit var podlogaFudbal: Spinner
    lateinit var podlogaFText:TextView
    var podlogaFIzabrana:String="Trava"

    /////////////////////////////////////
    lateinit var pom:Places
    private lateinit var prosecanBrojLjudi:EditText
    private lateinit var rasvetaSpinner: Spinner
    private  var rasvetaIzabrana:String="Nema"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_detaljni,container,false)
        slika=view.findViewById(R.id.slikaObjektaU)

        //////////////////////////////////////////
        teren=view.findViewById(R.id.spinnerTerenU)
        sirinaObruca=view.findViewById(R.id.spinnerSirinaU)
        sirinaText=view.findViewById(R.id.textSirinaU)
        osobinaObruca=view.findViewById(R.id.spinnerOsobinaU)
        osobinaText=view.findViewById(R.id.textOsobinaU)
        podlogaKosarka=view.findViewById(R.id.spinnerPodlogaKosarkaU)
        podlogaKText=view.findViewById(R.id.textPodlogaKosarkaU)
        mrezica=view.findViewById(R.id.spinnerMrezicaU)
        mrezicaText=view.findViewById(R.id.textMrezicaU)
        mreza=view.findViewById(R.id.spinnerMrezaU)
        posecenost=view.findViewById(R.id.spinnerPosecenostU)
        dimenzije=view.findViewById(R.id.spinnerDimenzijeU)
        mrezaText=view.findViewById(R.id.textMrezaU)
        golovi=view.findViewById(R.id.spinnerGoloviU)
        goloviText=view.findViewById(R.id.textGoloviU)
        podlogaFudbal=view.findViewById(R.id.spinnerPodlogaFudbalU)
        podlogaFText=view.findViewById(R.id.textPodlogaFudbalU)
        kosevi=view.findViewById(R.id.spinnerKoseviU)
        koseviText=view.findViewById(R.id.textKoseviU)

        /////////////////////////////////////////////////
        naziv=view.findViewById(R.id.editTextNazivMestaU)
        opis=view.findViewById(R.id.editTextKomentarU)
        ocena=view.findViewById(R.id.editTextOcenaU)
        ucitaj=view.findViewById(R.id.progressMestaU)
        azuriraj=view.findViewById(R.id.buttonDodajMestoU)
        latituda=view.findViewById(R.id.Latituda)
        longituda=view.findViewById(R.id.Longituda)
        prosecanBrojLjudi=view.findViewById(R.id.prosecanBrojLjudiU)
        rasvetaSpinner=view.findViewById(R.id.spinnerRasvetaU)
        var openCameraButton:Button=view.findViewById(R.id.buttonDodajKameromU)
        var openGalleryButton:Button=view.findViewById(R.id.buttonDodajGalerijomU)
        staraLatituda=view.findViewById(R.id.latitudaStara)
        staraLongituda=view.findViewById(R.id.longitudaStara)
        svojiKomentari=view.findViewById(R.id.komentariSvogaMesta)



        //NA OTVARANJE STRANICE PREUZIMA SE IZ BAZE NEOPHODNO
        try {
            ucitaj.visibility=View.VISIBLE
            DataBase.databasePlaces.child(sharedViewModel.izabranoMesto).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {

                    naziv.text = snapshot.child("naziv").value.toString()
                    opis.setText( snapshot.child("komentar").value.toString())
                    ocena.setText(snapshot.child("ocena").value.toString())
                    staraLatituda.text=snapshot.child("latituda").value.toString()
                    staraLongituda.text=snapshot.child("longituda").value.toString()
                    prosecanBrojLjudi.setText(snapshot.child("prosecanBrojLjudi").value.toString())
                    sharedViewModel.latituda=staraLatituda.text.toString()
                    sharedViewModel.longituda=staraLongituda.text.toString()
                    imgUrl = snapshot.child("img").value.toString()
                    preuzmiSLiku()
                    //////////////////////////////////////////////////
                    val terenNiz=resources.getStringArray(R.array.teren)
                    val terenIndex = terenNiz.indexOf(snapshot.child("teren").value.toString())

                    if (terenIndex != -1) {
                        teren.setSelection(terenIndex)
                    }
                    val dimenzijeNiz=resources.getStringArray(R.array.dimenzije)
                    val dimenzijeIndex = dimenzijeNiz.indexOf(snapshot.child("dimenzije").value.toString())

                    if (dimenzijeIndex != -1) {
                        dimenzije.setSelection(dimenzijeIndex)
                    }
                    val rasvetaNiz=resources.getStringArray(R.array.rasveta)
                    val rasvetaIndex = rasvetaNiz.indexOf(snapshot.child("rasveta").value.toString())

                    if (rasvetaIndex != -1) {
                        rasvetaSpinner.setSelection(rasvetaIndex)
                    }

                    val posecenostNiz=resources.getStringArray(R.array.posecenost)
                    val posecenostIndex = posecenostNiz.indexOf(snapshot.child("posecenost").value.toString())

                    if (posecenostIndex != -1) {
                        posecenost.setSelection(posecenostIndex)
                    }
                    if(snapshot.child("teren").value.toString()=="Fudbalski")
                    {
                        mreza.visibility=View.VISIBLE
                        golovi.visibility=View.VISIBLE
                        podlogaFudbal.visibility=View.VISIBLE
                        mrezaText.visibility=View.VISIBLE
                        goloviText.visibility=View.VISIBLE
                        podlogaFText.visibility=View.VISIBLE
                        sirinaObruca.visibility=View.GONE
                        sirinaText.visibility=View.GONE
                        osobinaObruca.visibility=View.GONE
                        osobinaText.visibility=View.GONE
                        podlogaKosarka.visibility=View.GONE
                        podlogaKText.visibility=View.GONE
                        mrezica.visibility=View.GONE
                        mrezicaText.visibility=View.GONE
                        kosevi.visibility=View.GONE
                        koseviText.visibility=View.GONE
                        val mrezaNiz=resources.getStringArray(R.array.mreza)
                        val mrezaIndex = mrezaNiz.indexOf(snapshot.child("mreza").value.toString())

                        if (mrezaIndex != -1) {
                            mreza.setSelection(mrezaIndex)
                        }
                        val goloviNiz=resources.getStringArray(R.array.golovi)
                        val goloviIndex = goloviNiz.indexOf(snapshot.child("golovi").value.toString())

                        if (goloviIndex != -1) {
                            golovi.setSelection(goloviIndex)
                        }
                        val podlogaNiz=resources.getStringArray(R.array.podlogaF)
                        val podlogaIndex = podlogaNiz.indexOf(snapshot.child("podlogaFudbal").value.toString())

                        if (podlogaIndex != -1) {
                            podlogaFudbal.setSelection(podlogaIndex)
                        }


                    }
                    if(snapshot.child("teren").value.toString()=="Kosarkaski")
                    {
                        mreza.visibility=View.GONE
                        golovi.visibility=View.GONE
                        podlogaFudbal.visibility=View.GONE
                        mrezaText.visibility=View.GONE
                        goloviText.visibility=View.GONE
                        podlogaFText.visibility=View.GONE
                        sirinaObruca.visibility=View.VISIBLE
                        sirinaText.visibility=View.VISIBLE
                        osobinaObruca.visibility=View.VISIBLE
                        osobinaText.visibility=View.VISIBLE
                        podlogaKosarka.visibility=View.VISIBLE
                        podlogaKText.visibility=View.VISIBLE
                        mrezica.visibility=View.VISIBLE
                        mrezicaText.visibility=View.VISIBLE
                        kosevi.visibility=View.VISIBLE
                        koseviText.visibility=View.VISIBLE
                        val sirinaNiz=resources.getStringArray(R.array.sirinaObruca)
                        val sirinaIndex = sirinaNiz.indexOf(snapshot.child("sirina").value.toString())

                        if (sirinaIndex != -1) {
                            sirinaObruca.setSelection(sirinaIndex)
                        }
                        val osobinaNiz=resources.getStringArray(R.array.osobinaObruca)
                        val osobinaIndex = osobinaNiz.indexOf(snapshot.child("osobina").value.toString())

                        if (osobinaIndex != -1) {
                            osobinaObruca.setSelection(osobinaIndex)
                        }
                        val podlogaKNiz=resources.getStringArray(R.array.podlogaK)
                        val podlogaKIndex = podlogaKNiz.indexOf(snapshot.child("podlogaKosarka").value.toString())

                        if (podlogaKIndex != -1) {
                            podlogaKosarka.setSelection(podlogaKIndex)
                        }
                        val mrezicaNiz=resources.getStringArray(R.array.mrezica)
                        val mrezicaIndex = mrezicaNiz.indexOf(snapshot.child("mrezica").value.toString())

                        if (mrezicaIndex != -1) {
                            mrezica.setSelection(mrezicaIndex)
                        }
                        val koseviNiz=resources.getStringArray(R.array.Kosevi)
                        val koseviIndex = koseviNiz.indexOf(snapshot.child("visinaKosa").value.toString())

                        if (koseviIndex != -1) {
                            kosevi.setSelection(koseviIndex)
                        }

                    }
                    ucitaj.visibility=View.GONE
                }
            }.addOnFailureListener { exception ->
                // Handle the exception here
                Log.e(ContentValues.TAG, "Error getting data from Firebase: ${exception.message}")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error accessing Firebase: ${e.message}")
        }
        svojiKomentari.setOnClickListener{
            locationViewModel.setName(sharedViewModel.izabranoMesto)
            findNavController().navigate(R.id.action_detaljniFragment2_to_komentariMestaFragment)
        }
        back=view.findViewById(R.id.buttonNazadU)
        back.setOnClickListener{
            findNavController().navigate(R.id.action_detaljniFragment2_to_mojaMestaFragment)

        }
        ////////////////////////////////////////////////////////////////////////////////////////
        teren.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                terenIzabran=adapterView?.getItemAtPosition(positon).toString()
                Toast.makeText(context,"${terenIzabran}",Toast.LENGTH_SHORT).show()
                if(terenIzabran=="Fudbalski")
                {
                    mreza.visibility=View.VISIBLE
                    golovi.visibility=View.VISIBLE
                    podlogaFudbal.visibility=View.VISIBLE
                    mrezaText.visibility=View.VISIBLE
                    goloviText.visibility=View.VISIBLE
                    podlogaFText.visibility=View.VISIBLE
                    sirinaObruca.visibility=View.GONE
                    sirinaText.visibility=View.GONE
                    osobinaObruca.visibility=View.GONE
                    osobinaText.visibility=View.GONE
                    podlogaKosarka.visibility=View.GONE
                    podlogaKText.visibility=View.GONE
                    mrezica.visibility=View.GONE
                    mrezicaText.visibility=View.GONE
                    kosevi.visibility=View.GONE
                    koseviText.visibility=View.GONE

                }
                if(terenIzabran=="Kosarkaski")
                {
                    mreza.visibility=View.GONE
                    golovi.visibility=View.GONE
                    podlogaFudbal.visibility=View.GONE
                    mrezaText.visibility=View.GONE
                    goloviText.visibility=View.GONE
                    podlogaFText.visibility=View.GONE
                    sirinaObruca.visibility=View.VISIBLE
                    sirinaText.visibility=View.VISIBLE
                    osobinaObruca.visibility=View.VISIBLE
                    osobinaText.visibility=View.VISIBLE
                    podlogaKosarka.visibility=View.VISIBLE
                    podlogaKText.visibility=View.VISIBLE
                    mrezica.visibility=View.VISIBLE
                    mrezicaText.visibility=View.VISIBLE
                    kosevi.visibility=View.VISIBLE
                    koseviText.visibility=View.VISIBLE


                }

            }
        }
        sirinaObruca.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                sirinaIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        osobinaObruca.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                osobinaIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        podlogaKosarka.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                podlogaKIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        mrezica.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                mrezicaIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        posecenost.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                posecenostIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        dimenzije.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                dimenzijeIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        mreza.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                mrezaIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        golovi.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                goloviIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        podlogaFudbal.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                podlogaFIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        kosevi.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                koseviIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }









////////////////////////////////////////////////////////////////
        val lonObserver= Observer<String>{newValue->
            longituda.setText(newValue.toString())
            sharedViewModel.longituda=longituda.text.toString()


        }
        locationViewModel.longitude.observe(viewLifecycleOwner,lonObserver)
        val latiObserver= Observer<String>{newValue->
            latituda.setText(newValue.toString())
            sharedViewModel.latituda=latituda.text.toString()

        }
        locationViewModel.latitude.observe(viewLifecycleOwner,latiObserver)
        var set:Button=view.findViewById(R.id.buttonSetU)
        set.setOnClickListener{
            sharedViewModel.latituda=staraLatituda.text.toString()
            sharedViewModel.longituda=staraLongituda.text.toString()
            locationViewModel.samoPregled=false
            locationViewModel.dodajObjekat=false
            locationViewModel.jedanObjekat=true

            findNavController().navigate(R.id.action_detaljniFragment2_to_mapFragment)
        }
        var nizKometara = ArrayList<Comments>()
        val nizObserver=Observer<ArrayList<Comments>>{newValue->
            nizKometara=newValue


        }
        sharedViewModel.comments.observe(viewLifecycleOwner,nizObserver)
        ///////////////////////////////////////////////////////////////////////////////
        azuriraj.setOnClickListener{
            var Opis=opis.text.toString()
            val Ocena=ocena.text.toString()
            if((ocena.text.toString().isNotEmpty()&&ocena.text.toString().toInt()>=5&&ocena.text.toString().toInt()<=10)||(ocena.text.toString().isNotEmpty()&&ocena.text.toString().toInt()<5&&ocena.text.toString().toInt()>=1&&opis.text.toString().isNotEmpty())) {

                ucitaj.visibility = View.VISIBLE
                val key = naziv.text.toString().replace(".", "").replace("#", "").replace("$", "")
                    .replace("[", "").replace("]", "")
                if(Opis.isEmpty())
                {
                    Opis=""
                }
                val mesto = if(terenIzabran=="Fudbalski") {
                    Places(naziv.text.toString(),opis.text.toString(),ocena.text.toString().toIntOrNull(),sharedViewModel.ime,longituda.text.toString(), latituda.text.toString(),terenIzabran,"","","","","",posecenostIzabrana,dimenzijeIzabrana,rasvetaIzabrana,prosecanBrojLjudi.text.toString().toIntOrNull(),mrezaIzabrana,goloviIzabrana,podlogaFIzabrana,imgUrl)

                } else {
                    Places(naziv.text.toString(),opis.text.toString(),ocena.text.toString().toIntOrNull(),sharedViewModel.ime,longituda.text.toString(), latituda.text.toString(),terenIzabran,sirinaIzabrana,osobinaIzabrana,podlogaKIzabrana,koseviIzabrana,mrezicaIzabrana,posecenostIzabrana,dimenzijeIzabrana,rasvetaIzabrana,prosecanBrojLjudi.text.toString().toIntOrNull(),"","","",imgUrl)
                }
                DataBase.databasePlaces.child(key).setValue(mesto).addOnSuccessListener {

                    ucitaj.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Uspesno ste azurirali mesto ${mesto.naziv}",
                        Toast.LENGTH_SHORT
                    ).show()

                    DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "").replace("$", "")
                        .replace("[", "").replace("]", "")).get().addOnSuccessListener { snapshot->
                        if(snapshot.exists())
                        {
                            sharedViewModel.user=User(snapshot.child("korisnicko").value.toString(),snapshot.child("sifra").value.toString(),snapshot.child("ime").value.toString(),snapshot.child("prezime").value.toString(),snapshot.child("brojTelefona").value.toString().toLongOrNull(),snapshot.child("img").value.toString(),ArrayList(),snapshot.child("bodovi").value.toString().toInt())
                            if(sharedViewModel.user.bodovi!=null)
                            {
                                sharedViewModel.user.bodovi=sharedViewModel.user.bodovi?.plus(2)

                            }
                            DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "")
                                .replace("$", "").replace("[", "").replace("]", "")).setValue(sharedViewModel.user).addOnSuccessListener {
                                Toast.makeText(context,"Dobili ste jos 2 boda",Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }.addOnFailureListener {
                                Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                            }
                        }

                    }.addOnFailureListener {
                        Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                    }
                    //findNavController().navigate(R.id.action_detaljniFragment2_to_mojaMestaFragment)


                }.addOnFailureListener {
                    Toast.makeText(context, "Greska", Toast.LENGTH_SHORT)
                }
            }
            else
            {
                Toast.makeText(context,"Popunite  sva polja  ako ste dali ocenu manju od 5 obrazlozite.Ocena se daje od 1-10 za ocenu od 5-10 ne treba obrazlozenje", Toast.LENGTH_SHORT).show()
            }

        }
        obrisi=view.findViewById(R.id.buttonObrisiU)
        obrisi.setOnClickListener{
            DataBase.databasePlaces.child(naziv.text.toString()).removeValue().addOnSuccessListener {
                Toast.makeText(context,"Uspesno ste obrisali mesto ${naziv.text.toString()}",Toast.LENGTH_LONG).show()
               azuriraj.visibility=View.GONE
                obrisi.visibility=View.GONE
                set.visibility=View.GONE
                openCameraButton.visibility=View.GONE
                openGalleryButton.visibility=View.GONE
                sharedViewModel.latituda=""
                sharedViewModel.longituda=""
                for(komentar in nizKometara)
                {
                    Toast.makeText(context,"${komentar.mesto}",Toast.LENGTH_SHORT).show()
                    if(komentar.mesto==naziv.text.toString())
                    {
                        DataBase.databaseComments.child(komentar.id.toString()).removeValue().addOnSuccessListener {

                        }
                            .addOnFailureListener { exception->Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show() }
                    }
                }



                DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "").replace("$", "")
                    .replace("[", "").replace("]", "")).get().addOnSuccessListener { snapshot->
                    if(snapshot.exists())
                    {
                        sharedViewModel.user=User(snapshot.child("korisnicko").value.toString(),snapshot.child("sifra").value.toString(),snapshot.child("ime").value.toString(),snapshot.child("prezime").value.toString(),snapshot.child("brojTelefona").value.toString().toLongOrNull(),snapshot.child("img").value.toString(),ArrayList(),snapshot.child("bodovi").value.toString().toInt())
                        if(sharedViewModel.user.bodovi!=null)
                        {
                            sharedViewModel.user.bodovi=sharedViewModel.user.bodovi?.minus(10)

                        }
                        DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "")
                            .replace("$", "").replace("[", "").replace("]", "")).setValue(sharedViewModel.user).addOnSuccessListener {
                            Toast.makeText(context,"Izgubili  ste 10 boda",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                        }
                    }

                }.addOnFailureListener {
                    Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{
                Toast.makeText(context,"Greska",Toast.LENGTH_SHORT).show()
            }



        }

        ///////////////////////////////////////////////////////////////////////////////

        openCameraButton.setOnClickListener{
            if (checkCameraPermission()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } else {
                // Ako dozvola nije odobrena, zahtevajte je
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }

        openGalleryButton.setOnClickListener{
            if (checkGalleryPermission()) {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY_PERMISSION_REQUEST_CODE)
            } else {
                // Ako dozvola nije odobrena, zahtevajte je
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_PERMISSION_REQUEST_CODE
                )
            }
        }




        return view
    }

    //FUNKCIJE ZA DOZVOLE
    private fun checkCameraPermission() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun checkGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    //FUNKCIJA KOJA ODREDJUJE STA CE DA SE RADI KADA SE VRATIMO U ACTIVIY APLIKACIJE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //ZA SETOVANJE IMAGE VIEW-A
            slika.setImageBitmap(imageBitmap)
            posaljiSlikuUFireStoragePreuzmiURLiPosaljiURealtimeDatabase(imageBitmap)
        }
        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Ovde obrada rezultata odabira slike iz galerije
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                // Očitavanje slike iz URI i postavljanje u ImageView
                val imageBitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    selectedImageUri
                )
                slika.setImageBitmap(imageBitmap)

                // Otpremanje slike na Firebase Storage
                posaljiSlikuUFireStoragePreuzmiURLiPosaljiURealtimeDatabase(imageBitmap)

            }
        }
    }
//FUNKCIJA ZA UPIS U BAZU PA PREUZIMANJE URL SLIKE SA STORIGA I CUVANJE U LOKALNU PROMENLJIVU KOJA CE SLUZITI ZA UZIMANJE PODATKA
//O ATRIBUTU Places.Img:String
private fun preuzmiSLiku() {


    if (imgUrl != "") {
        Glide.with(requireContext())
            .load(imgUrl)
            .into(slika)
    }
}
    private fun posaljiSlikuUFireStoragePreuzmiURLiPosaljiURealtimeDatabase(imageBitmap:Bitmap)
{
    val imagesRef = DataBase.storageRef.child("images/${System.currentTimeMillis()}.jpg")

    // Convert the bitmap to bytes
    val baos = ByteArrayOutputStream()
    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageData = baos.toByteArray()

    // Upload the image to Firebase Storage
    val uploadTask = imagesRef.putBytes(imageData)
    uploadTask.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Image upload success
            // Now you can get the download URL of the image and save it to the database
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                // Save the URI to the database or use it as needed
                imgUrl = uri.toString()
                preuzmiSLiku()
                // Add the code to save the URL to the user's data in Firebase Database here
            }.addOnFailureListener { exception ->
                // Handle any errors that may occur while retrieving the download URL
                Toast.makeText(
                    requireContext(),
                    "Failed to get download URL.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Image upload failed
            val errorMessage = task.exception?.message
            Toast.makeText(
                requireContext(),
                "Image upload failed. Error: $errorMessage",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}




}