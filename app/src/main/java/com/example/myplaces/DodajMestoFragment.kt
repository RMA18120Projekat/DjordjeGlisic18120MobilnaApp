package com.example.myplaces

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
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
import android.widget.ArrayAdapter
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.util.Calendar


class DodajMestoFragment : Fragment() {
 private  var imgUrl:String=""
    private val REQUEST_IMAGE_CAPTURE = 1
    val GALLERY_PERMISSION_REQUEST_CODE = 1002
    val CAMERA_PERMISSION_REQUEST_CODE = 1001



    private  val sharedViewModel:KorisnikViewModel by activityViewModels()

    lateinit var nazivMesta:EditText
    lateinit var opisMesta:EditText
    lateinit var ocenaMesta:EditText
    lateinit var progress:ProgressBar
    lateinit var potvrdi:Button
    lateinit var otkazi:Button
    lateinit var mesto:Places
    lateinit var latituda:EditText
    lateinit var longituda:EditText
    lateinit var set:Button
    lateinit var slika:ImageView
    ////////////////////////////////////////
    lateinit var teren:Spinner
    lateinit var cekajSliku:ProgressBar

    var terenIzabran:String="Fudbalski"
    /////////////////////////////////////////
    lateinit var sirinaObruca:Spinner
    lateinit var sirinaText:TextView
    private  var sirinaIzabrana:String="Normlna sirina"
    lateinit var osobinaObruca:Spinner
    lateinit var osobinaText:TextView
    var osobinaIzabrana:String="Normalan osecaj"
    lateinit var podlogaKosarka:Spinner
    lateinit var podlogaKText:TextView
    var podlogaKIzabrana:String="Guma"
    lateinit var mrezica:Spinner
    lateinit var mrezicaText:TextView
    var mrezicaIzabrana:String="Ima"
    lateinit var kosevi:Spinner
    lateinit var koseviText:TextView
    var koseviIzabrana:String="305cm"
    ///////////////////////////////////////
    lateinit var posecenost:Spinner
    var posecenostIzabrana:String="Mala"

    lateinit var dimenzije:Spinner
    var dimenzijeIzabrana:String="Male"
    //////////////////////////////////////
    lateinit var mreza:Spinner
    lateinit var mrezaText:TextView
    var mrezaIzabrana:String="Ima"
    lateinit var golovi:Spinner
    lateinit var goloviText:TextView
    var goloviIzabrana:String="Manji"
    lateinit var podlogaFudbal:Spinner
    lateinit var podlogaFText:TextView
    var podlogaFIzabrana:String="Trava"
    ////////////////////////////////
    private val locationViewModel:LocationViewModel by activityViewModels()
    private lateinit var prosecanBrojLjudi:EditText
    private lateinit var rasvetaSpinner: Spinner
    private  var rasvetaIzabrana:String="Nema"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_dodaj_mesto,container,false)
        slika=view.findViewById(R.id.slikaObjekta)
        nazivMesta=view.findViewById(R.id.editTextNazivMesta)
        opisMesta=view.findViewById(R.id.editTextKomentar)
        ocenaMesta=view.findViewById(R.id.editTextOcena)
        progress=view.findViewById(R.id.progressMesta)
        cekajSliku=view.findViewById(R.id.cekajSliku)
        potvrdi=view.findViewById(R.id.buttonDodajMesto)
        otkazi=view.findViewById(R.id.buttonOtkaziDodavanje)
        latituda=view.findViewById(R.id.editLatituda)
        longituda=view.findViewById(R.id.editLongituda)
        teren=view.findViewById(R.id.spinnerTeren)
        sirinaObruca=view.findViewById(R.id.spinnerSirina)
        sirinaText=view.findViewById(R.id.textSirina)
        osobinaObruca=view.findViewById(R.id.spinnerOsobina)
        osobinaText=view.findViewById(R.id.textOsobina)
        podlogaKosarka=view.findViewById(R.id.spinnerPodlogaKosarka)
        podlogaKText=view.findViewById(R.id.textPodlogaKosarka)
        mrezica=view.findViewById(R.id.spinnerMrezica)
        mrezicaText=view.findViewById(R.id.textMrezica)
        mreza=view.findViewById(R.id.spinnerMreza)
        posecenost=view.findViewById(R.id.spinnerPosecenost)
        dimenzije=view.findViewById(R.id.spinnerDimenzije)
        mrezaText=view.findViewById(R.id.textMreza)
        golovi=view.findViewById(R.id.spinnerGolovi)
        goloviText=view.findViewById(R.id.textGolovi)
        podlogaFudbal=view.findViewById(R.id.spinnerPodlogaFudbal)
        podlogaFText=view.findViewById(R.id.textPodlogaFudbal)
        kosevi=view.findViewById(R.id.spinnerKosevi)
        koseviText=view.findViewById(R.id.textKosevi)
        prosecanBrojLjudi=view.findViewById(R.id.prosecanBrojLjudi)
        rasvetaSpinner=view.findViewById(R.id.spinnerRasveta)

        ////////////////////////////////////////////////////////////////////////////////////////
        teren.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
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
        sirinaObruca.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        osobinaObruca.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        podlogaKosarka.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        mrezica.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        posecenost.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        dimenzije.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        rasvetaSpinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                positon: Int,
                id: Long
            ) {
                rasvetaIzabrana = adapterView?.getItemAtPosition(positon).toString()
            }
        }
        mreza.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        golovi.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        podlogaFudbal.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        kosevi.onItemSelectedListener=object :AdapterView.OnItemSelectedListener {
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
        set=view.findViewById(R.id.buttonSet)
        set.setOnClickListener{
            locationViewModel.dodajObjekat=true
            locationViewModel.jedanObjekat=false
            locationViewModel.samoPregled=false

            findNavController().navigate(R.id.action_dodajMestoFragment_to_mapFragment)

        }
        ///////////////////////////////////////////////////////////////////////////////
        potvrdi.setOnClickListener {

                val nazivPom = nazivMesta.text.toString()
                var opisPom = opisMesta.text.toString()
                val ocenaPom = ocenaMesta.text.toString()
                if( (nazivPom.isNotEmpty() && ocenaPom.isNotEmpty()&&ocenaPom.toInt()>=5&&ocenaPom.toInt()<=10)||(nazivPom.isNotEmpty()&&ocenaPom.isNotEmpty()&&ocenaPom.toInt()<5&&ocenaPom.toInt()>=1&&opisPom.isNotEmpty()) ){
                    var instanca= Calendar.getInstance()
                    var datum=instanca.get(Calendar.DAY_OF_MONTH).toString()+"."+instanca.get(
                        Calendar.MONTH).toString()+"."+instanca.get(Calendar.YEAR)
                    var vreme=instanca.get(Calendar.HOUR_OF_DAY).toString()+":"+instanca.get(
                        Calendar.MINUTE)
                    var datumVreme=datum+" u "+vreme
                    progress.visibility = View.VISIBLE
                    if(opisMesta.text.toString().isEmpty())
                    {
                        opisPom=""
                    }
                    if (terenIzabran == "Fudbalski") {

                        mesto = Places(
                            nazivMesta.text.toString(),
                            opisPom,
                            ocenaMesta.text.toString().toIntOrNull(),
                            sharedViewModel.ime,
                            longituda.text.toString(),
                            latituda.text.toString(),
                            terenIzabran,
                            "",
                            "",
                            "",
                            "",
                            "",
                            posecenostIzabrana,
                            dimenzijeIzabrana,
                            rasvetaIzabrana,
                            prosecanBrojLjudi.text.toString().toIntOrNull(),
                            mrezaIzabrana,
                            goloviIzabrana,
                            podlogaFIzabrana,
                            imgUrl,
                            datumVreme
                        )

                    } else {

                        mesto = Places(
                            nazivMesta.text.toString(),
                            opisPom,
                            ocenaMesta.text.toString().toIntOrNull(),
                            sharedViewModel.ime,
                            longituda.text.toString(),
                            latituda.text.toString(),
                            terenIzabran,
                            sirinaIzabrana,
                            osobinaIzabrana,
                            podlogaKIzabrana,
                            koseviIzabrana,
                            mrezicaIzabrana,
                            posecenostIzabrana,
                            dimenzijeIzabrana,
                            rasvetaIzabrana,
                            prosecanBrojLjudi.text.toString().toIntOrNull(),
                            "",
                            "",
                            "",
                            imgUrl,
                            datumVreme
                        )
                    }


                    val key = nazivMesta.text.toString().replace(".", "").replace("#", "")
                        .replace("$", "").replace("[", "").replace("]", "")

                    DataBase.databasePlaces.child(key).setValue(mesto)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progress.visibility = View.GONE
                                Toast.makeText(
                                    context,
                                    "Uspesno ste dodali mesto ${nazivMesta.text.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                nazivMesta.text.clear()
                                opisMesta.text.clear()
                                ocenaMesta.text.clear()
                                longituda.text.clear()
                                latituda.text.clear()
                                DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "")
                                    .replace("$", "").replace("[", "").replace("]", "")).get().addOnSuccessListener { snapshot->
                                    if(snapshot.exists())
                                    {
                                        sharedViewModel.user=User(snapshot.child("korisnicko").value.toString(),snapshot.child("sifra").value.toString(),snapshot.child("ime").value.toString(),snapshot.child("prezime").value.toString(),snapshot.child("brojTelefona").value.toString().toLongOrNull(),snapshot.child("img").value.toString(),ArrayList(),snapshot.child("bodovi").value.toString().toIntOrNull())
                                        sharedViewModel.user.bodovi=sharedViewModel.user.bodovi?.plus(10)
                                        DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "")
                                            .replace("$", "").replace("[", "").replace("]", "")).setValue(sharedViewModel.user).addOnSuccessListener {
                                                Toast.makeText(context,"Dobili ste jos 10 bodova",Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                                        }
                                    }

                                }.addOnFailureListener{
                                    Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                                }

                            } else {
                                Toast.makeText(context, "Greska", Toast.LENGTH_LONG).show()
                            }


                        }

                } else {

                    Toast.makeText(context, "Niste popunili sva polja, ako ste dali ocenu manju od 6 morate napisati komentar sta se treba ispraviti.Ocena je od 1-10", Toast.LENGTH_SHORT).show()
                }


        }
        otkazi.setOnClickListener{
            findNavController().navigate(R.id.action_dodajMestoFragment_to_homeFragment)
        }
        var openCameraButton:Button=view.findViewById(R.id.buttonDodajKamerom)
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
        var openGalleryButton:Button=view.findViewById(R.id.buttonDodajGalerijom)

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

    private fun posaljiSlikuUFireStoragePreuzmiURLiPosaljiURealtimeDatabase(imageBitmap:Bitmap)
    {
        val imagesRef = DataBase.storageRef.child("images/${System.currentTimeMillis()}.jpg")
        slika.visibility=View.GONE
        cekajSliku.visibility=View.VISIBLE
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
                    cekajSliku.visibility=View.GONE
                    slika.visibility=View.VISIBLE
                    Toast.makeText(context,"Slika je zapamcena",Toast.LENGTH_SHORT).show()
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