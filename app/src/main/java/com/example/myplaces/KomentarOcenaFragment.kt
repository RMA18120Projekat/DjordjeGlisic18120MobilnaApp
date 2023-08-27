package com.example.myplaces

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.osmdroid.util.GeoPoint
import org.w3c.dom.Text
import java.util.Calendar


class KomentarOcenaFragment : Fragment() {
private  lateinit var nazad:Button
private lateinit var posalji:Button
private lateinit var sviKomentari:Button
private lateinit var otvoriMape:Button
private lateinit var komentar:EditText
private lateinit var ocena:EditText
private lateinit var progress:ProgressBar
private lateinit var progresBaza:ProgressBar
private lateinit var progresSlika:ProgressBar
val locationViewModel:LocationViewModel by activityViewModels()
val sharedViewModel:KorisnikViewModel by activityViewModels()
//KOMPONENTE KOJE PRIKAZUJU PODATKE ZA TEREN UCITANE IZ BAZE
private lateinit var teren:TextView
private lateinit var ocenaTerena:TextView
private lateinit var autor:TextView
private lateinit var latituda:TextView
private lateinit var longituda:TextView
private lateinit var naziv:TextView
private lateinit var slika:ImageView
private lateinit var obrucS:TextView
private lateinit var obrucO:TextView
private lateinit var podlogaK:TextView
private lateinit var posecenost:TextView
private lateinit var dimenzije:TextView
private lateinit var mrezica:TextView
private lateinit var mreza:TextView
private lateinit var golovi:TextView
private lateinit var podlogaF:TextView
private lateinit var visina:TextView
//TEXTVIEW ZA KOMPONENTE KOJE SE INICIJALIZUJU IZ BAZE
private lateinit var textTeren:TextView
private lateinit var textOcena:TextView
private lateinit var textAutor:TextView
private lateinit var textSirina:TextView
private lateinit var textOsobina:TextView
private lateinit var textPK:TextView
private lateinit var textPosecenost:TextView
private lateinit var textDimenzije:TextView
private lateinit var textMrezica:TextView
private lateinit var textMreza:TextView
private lateinit var textGolovi:TextView
private lateinit var textPF:TextView
private lateinit var textVisina:TextView
private lateinit var buttonInfo:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_komentar_ocena, container, false)
        //INICIJALIZACIJA PROMENLJIVIH KOMPONENTAMA
        nazad=view.findViewById(R.id.komentarNazad)
        posalji=view.findViewById(R.id.komentarPosalji)
        sviKomentari=view.findViewById(R.id.buttonSviKomentari)
        komentar=view.findViewById(R.id.editKomentar)
        ocena=view.findViewById(R.id.komentarOcena)
        latituda=view.findViewById(R.id.komentarLatituda)
        longituda=view.findViewById(R.id.konetarLongituda)
        naziv=view.findViewById(R.id.komentarNaziv)
        otvoriMape=view.findViewById(R.id.buttonOpenMap)
        progress=view.findViewById(R.id.upisKomentaraPB)
        progresBaza=view.findViewById(R.id.komentarProgres)
        buttonInfo=view.findViewById(R.id.buttonInfo)
        progresSlika=view.findViewById(R.id.slikaProgres)

        //SUBSKRAJBOVANJE NA PROMENE KOORDINATA
        val bazaObserver=Observer<Boolean>{newValue->
            buttonInfo.isEnabled=newValue
            sviKomentari.isEnabled=newValue
        }
        locationViewModel.baza.observe(viewLifecycleOwner,bazaObserver)
        val lonObserver= Observer<String>{newValue->
            longituda.text=newValue.toString()
            sharedViewModel.longituda=longituda.text.toString()


        }
        locationViewModel.longitudaKoment.observe(viewLifecycleOwner,lonObserver)
        val latiObserver= Observer<String>{newValue->
            latituda.setText(newValue.toString())
            sharedViewModel.latituda=latituda.text.toString()

        }
        locationViewModel.latitudaKoment.observe(viewLifecycleOwner,latiObserver)
        val nazivObserver= Observer<String> {newValue->
            naziv.text=newValue.toString()
            //locationViewModel.nazivMesta=newValue.toString()

        }
        locationViewModel.nazivMesta.observe(viewLifecycleOwner,nazivObserver)
        //INICIJALIZACIJA KOMPONENATA ZA PRIKAZ IZ BAZE
        teren=view.findViewById(R.id.komentarTeren)
        teren.visibility=View.GONE
        ocenaTerena=view.findViewById(R.id.komentarOcenaB)
        ocenaTerena.visibility=View.GONE
        autor=view.findViewById(R.id.komentarAutor)
        autor.visibility=View.GONE
        slika=view.findViewById(R.id.komentSLika)
        slika.visibility=View.GONE
        obrucS=view.findViewById(R.id.komentarOsobina)

        obrucS.visibility=View.GONE
        obrucO=view.findViewById(R.id.komentarSirina)
        obrucO.visibility=View.GONE
        podlogaK=view.findViewById(R.id.komentarPodlogaK)
        podlogaK.visibility=View.GONE
        mrezica=view.findViewById(R.id.komentarMrezica)
        mrezica.visibility=View.GONE
        dimenzije=view.findViewById(R.id.komentarDimenzije)
        posecenost=view.findViewById(R.id.komentarPosecenost)
        mreza=view.findViewById(R.id.komentarMreza)
        mreza.visibility=View.GONE
        golovi=view.findViewById(R.id.komentarGolovi)
        golovi.visibility=View.GONE
        podlogaF=view.findViewById(R.id.komentarPodlogaF)
        podlogaF.visibility=View.GONE
        visina=view.findViewById(R.id.komentarVisina)
        visina.visibility=View.GONE
        //INICIJALIZACIJA TEXTVIEW
        textTeren=view.findViewById(R.id.TextTeren)
        textTeren.visibility=View.GONE
        textOcena=view.findViewById(R.id.TextOcena)
        textOcena.visibility=View.GONE
        textAutor=view.findViewById(R.id.TextAutor)
        textAutor.visibility=View.GONE
        textOsobina=view.findViewById(R.id.TextOsobina)
        textOsobina.visibility=View.GONE
        textSirina=view.findViewById(R.id.TextSirina)
        textSirina.visibility=View.GONE
        textPF=view.findViewById(R.id.TextPodlogaF)
        textPF.visibility=View.GONE
        textPosecenost=view.findViewById(R.id.TextPosecenost)
        textPosecenost.visibility=View.GONE
        textDimenzije=view.findViewById(R.id.TextDimenzije)
        textDimenzije.visibility=View.GONE
        textMrezica=view.findViewById(R.id.TextMrezica)
        textMrezica.visibility=View.GONE
        textMreza=view.findViewById(R.id.TextMreza)
        textMreza.visibility=View.GONE
        textGolovi=view.findViewById(R.id.TextGolovi)
        textGolovi.visibility=View.GONE
        textPK=view.findViewById(R.id.TextPodloga)
        textPK.visibility=View.GONE
        textVisina=view.findViewById(R.id.TextVisina)
        textVisina.visibility=View.GONE
        DataBase.databasePlaces.child(naziv.text.toString()).get().addOnCompleteListener{task->
            if(task.isSuccessful==true)
            {
                var snapShot=task.result
                if(snapShot.exists()==false)
                {
                    naziv.text=""
                    latituda.text=""
                    longituda.text=""
                    posalji.isEnabled=false
                    sviKomentari.isEnabled=false
                    buttonInfo.isEnabled=false

                }
            }
        }











        otvoriMape.setOnClickListener{
            locationViewModel.dodajObjekat=false
            locationViewModel.samoPregled=false
            locationViewModel.jedanObjekat=false
            locationViewModel.komentarisiObjekat=true
            findNavController().navigate(R.id.action_komentarOcenaFragment_to_mapFragment)


        }
        nazad.setOnClickListener{
            findNavController().navigate(R.id.action_komentarOcenaFragment_to_homeFragment)
        }
        buttonInfo.setOnClickListener{
            try {
                progresBaza.visibility=View.VISIBLE
                DataBase.databasePlaces.child(naziv.text.toString().replace(".", "").replace("#", "")
                    .replace("$", "").replace("[", "").replace("]", "")).get().addOnSuccessListener {dataShot->
                    progresBaza.visibility=View.GONE

                    if(dataShot.exists())
                    {
                        progresSlika.visibility=View.VISIBLE
                        val imageName=dataShot.child("img").value.toString()
                        if(imageName!="")
                        {
                            Glide.with(requireContext())
                                .load(imageName)
                                .into(slika)
                            progresSlika.visibility=View.GONE
                            slika.visibility=View.VISIBLE
                        }

                        teren.text=dataShot.child("teren").value.toString()
                        teren.visibility=View.VISIBLE
                        textTeren.visibility=View.VISIBLE
                        ocenaTerena.text=dataShot.child("ocena").value.toString()
                        ocenaTerena.visibility=View.VISIBLE
                        textOcena.visibility=View.VISIBLE
                        autor.text=dataShot.child("autor").value.toString()
                        autor.visibility=View.VISIBLE
                        textAutor.visibility=View.VISIBLE
                        posecenost.text=dataShot.child("posecenost").value.toString()
                        posecenost.visibility=View.VISIBLE
                        textPosecenost.visibility=View.VISIBLE
                        dimenzije.text=dataShot.child("dimenzije").value.toString()
                        dimenzije.visibility=View.VISIBLE
                        textDimenzije.visibility=View.VISIBLE
                        if(teren.text=="Fudbalski")
                        {
                            mreza.visibility=View.VISIBLE
                            mreza.text=dataShot.child("mreza").value.toString()
                            textMreza.visibility=View.VISIBLE
                            golovi.visibility=View.VISIBLE
                            golovi.text=dataShot.child("golovi").value.toString()
                            textGolovi.visibility=View.VISIBLE
                            podlogaF.visibility=View.VISIBLE
                            podlogaF.text=dataShot.child("podlogaFudbal").value.toString()
                            textPF.visibility=View.VISIBLE




                        }
                        else if(teren.text=="Kosarkaski")
                        {
                            obrucO.visibility=View.VISIBLE
                            obrucO.text=dataShot.child("osobinaObruca").value.toString()
                            textOsobina.visibility=View.VISIBLE
                            obrucS.visibility=View.VISIBLE
                            obrucS.text=dataShot.child("sirinaObruca").value.toString()
                            textSirina.visibility=View.VISIBLE
                            podlogaK.visibility=View.VISIBLE
                            podlogaK.text=dataShot.child("podlogaKosarka").value.toString()
                            textPK.visibility=View.VISIBLE
                            mrezica.visibility=View.VISIBLE
                            mrezica.text=dataShot.child("mrezica").value.toString()
                            textMrezica.visibility=View.VISIBLE
                            visina.visibility=View.VISIBLE
                            visina.text=dataShot.child("visinaKosa").value.toString()
                            textVisina.visibility=View.VISIBLE


                        }


                    }

                }.addOnFailureListener {exception->
                    progresBaza.visibility=View.GONE
                    Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show()

                }

            }
            catch (excption:Exception)
            {
                Toast.makeText(context,excption.toString(),Toast.LENGTH_LONG).show()

            }
        }




        posalji.setOnClickListener{
            var postoji=true
            DataBase.databasePlaces.child(naziv.text.toString()).get().addOnCompleteListener{task->
                if(task.isSuccessful==true)
                {
                    var snapShot=task.result
                    if(snapShot.exists()==false)
                    {
                        postoji=false
                    }
                }
            }
            if(postoji==true){
            if(naziv.text.toString().isEmpty()||ocena.text.toString().isEmpty()||ocena.text.toString().toInt()>10||ocena.text.toString().toInt()<1||komentar.text.toString().isEmpty())
            {
                Toast.makeText(context,"Niste popunili sva polja ili niste dali adekvatnu ocenu",Toast.LENGTH_SHORT).show()

            }
            else
            {
                    progress.visibility = View.VISIBLE
                    var id =
                        naziv.text.toString() + ocena.text.toString() + komentar.text.toString() + sharedViewModel.ime.toString()
                    id = id.replace(".", "").replace("#", "").replace("$", "").replace("[", "")
                        .replace("]", "").replace("@", "")
                    var instanca = Calendar.getInstance()
                    var mesec = instanca.get(Calendar.MONTH).toInt() + 1
                    var datum = instanca.get(Calendar.DAY_OF_MONTH)
                        .toString() + "." + mesec.toString() + "." + instanca.get(Calendar.YEAR)
                    var vreme = instanca.get(Calendar.HOUR_OF_DAY).toString() + ":" + instanca.get(
                        Calendar.MINUTE
                    )
                    var koment: Comments = Comments(
                        id,
                        sharedViewModel.ime,
                        naziv.text.toString(),
                        ocena.text.toString().toInt(),
                        komentar.text.toString(),
                        datum,
                        vreme,
                        0,
                        0
                    )
                    DataBase.databaseComments.child(id).setValue(koment).addOnCompleteListener {
                        Toast.makeText(context, "Uspesno ste dodali komentar", Toast.LENGTH_SHORT)
                            .show()
                        progress.visibility = View.GONE
                        DataBase.databaseUsers.child(
                            sharedViewModel.ime.replace(".", "").replace("#", "")
                                .replace("$", "").replace("[", "").replace("]", "")
                        ).get().addOnSuccessListener { snapshotU ->
                            if (snapshotU.exists()) {
                                sharedViewModel.user = User(
                                    snapshotU.child("korisnicko").value.toString(),
                                    snapshotU.child("sifra").value.toString(),
                                    snapshotU.child("ime").value.toString(),
                                    snapshotU.child("prezime").value.toString(),
                                    snapshotU.child("brojTelefona").value.toString().toLongOrNull(),
                                    snapshotU.child("img").value.toString(),
                                    ArrayList(),
                                    snapshotU.child("bodovi").value.toString().toIntOrNull()
                                )
                                sharedViewModel.user.bodovi = sharedViewModel.user.bodovi?.plus(5)
                                DataBase.databaseUsers.child(
                                    sharedViewModel.ime.replace(".", "").replace("#", "")
                                        .replace("$", "").replace("[", "").replace("]", "")
                                ).setValue(sharedViewModel.user).addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Dobili ste jos 5 bodova",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    ocena.text.clear()
                                    komentar.text.clear()
                                    DataBase.databasePlaces.child(naziv.text.toString()).get()
                                        .addOnSuccessListener { sna ->
                                            if (sna.exists()) {
                                                var mesto: Places = Places(
                                                    sna.child("naziv").value.toString(),
                                                    sna.child("komentar").value.toString(),
                                                    sna.child("ocena").value.toString().toInt(),
                                                    sna.child("autor").value.toString(),
                                                    sna.child("longituda").value.toString(),
                                                    sna.child("latituda").value.toString(),
                                                    sna.child("teren").value.toString(),
                                                    sna.child("sirinaObruca").value.toString(),
                                                    sna.child("osobinaObruca").value.toString(),
                                                    sna.child("podlogaKosarka").value.toString(),
                                                    sna.child("visinaKosa").value.toString(),
                                                    sna.child("mrezica").value.toString(),
                                                    sna.child("posecenost").value.toString(),
                                                    sna.child("dimenzije").value.toString(),
                                                    sna.child("rasveta").value.toString(),
                                                    sna.child("prosecanBrojLjudi").value.toString()
                                                        .toIntOrNull(),
                                                    sna.child("mreza").value.toString(),
                                                    sna.child("golovi").value.toString(),
                                                    sna.child("podlogaFudbal").value.toString(),
                                                    sna.child("img").value.toString(),
                                                    sna.child("datumVreme").value.toString()
                                                )
                                                var tacanMesec = instanca.get(Calendar.MONTH) + 1
                                                var datum = instanca.get(Calendar.DAY_OF_MONTH)
                                                    .toString() + "." + tacanMesec.toString() + "." + instanca.get(
                                                    Calendar.YEAR
                                                )
                                                var vreme = instanca.get(Calendar.HOUR_OF_DAY)
                                                    .toString() + ":" + instanca.get(Calendar.MINUTE)
                                                var datumVreme = datum + " u " + vreme
                                                mesto.datumInterakcije = datumVreme
                                                DataBase.databasePlaces.child(naziv.text.toString())
                                                    .setValue(mesto).addOnSuccessListener {

                                                }
                                            }

                                        }
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Greska", Toast.LENGTH_LONG).show()
                                }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Greksa",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Greska", Toast.LENGTH_LONG).show()
                    }
                }

             }
            else{
                Toast.makeText(context,"Izabrano mesto vise nije u bazi (izbrisano je)",Toast.LENGTH_LONG).show()

            }
        }
        sviKomentari.setOnClickListener{
            sharedViewModel.izabranoMesto=naziv.text.toString()
            findNavController().navigate(R.id.action_komentarOcenaFragment_to_komentariMestaFragment)
        }
        return view
    }


}