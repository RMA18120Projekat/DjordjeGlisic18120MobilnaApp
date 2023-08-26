package com.example.myplaces

import android.content.ContentValues
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class KomentariMestaFragment : Fragment() {
    val sharedViewModel:KorisnikViewModel by activityViewModels()
    var nizKomentara:ArrayList<Comments> = ArrayList()
    private var nizOsoba= ArrayList<KorisnikKomentarP>()
    private val locationViewModel:LocationViewModel by activityViewModels()
    private lateinit var glavni:LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_komentari_mesta, container, false)
         glavni=view.findViewById<LinearLayout>(R.id.glavni)
        crtajFormuKomentara()
        val nizObserver= Observer<ArrayList<Comments>>{ newValue->
            nizKomentara=newValue

            crtajFormuKomentara()
        }
        sharedViewModel.comments.observe(viewLifecycleOwner,nizObserver)
        val nizOsobaObserver=Observer<ArrayList<KorisnikKomentarP>>{newValue->
            nizOsoba=newValue
            crtajFormuKomentara()


        }
        sharedViewModel.osobePodrzale.observe(viewLifecycleOwner,nizOsobaObserver)




        return view
    }
    private fun crtajFormuKomentara()
    {
        glavni.removeAllViews()

        var kolko=0
        val layout=LinearLayout(context)
        glavni.addView(layout)
        for(clan in nizKomentara)
        {
            if(clan.mesto.toString()==locationViewModel.nazivMesta.value.toString())
            {
                kolko++
                layout.orientation=LinearLayout.VERTICAL
                val layoutParamsLayout = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layout.layoutParams= layoutParamsLayout
                layout.layoutParams = layoutParamsLayout
                var autorText=TextView(context)
                autorText.layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                autorText.text="Autor"
                autorText.textSize=25f
                autorText.setTypeface(null,Typeface.BOLD)
                var autor=TextView(context)
                autor.layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                autor.text=clan.autor
                autor.textSize=25f
                var ocenaText=TextView(context)
                ocenaText.layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                ocenaText.text="Ocena"
                ocenaText.textSize=25f
                ocenaText.setTypeface(null,Typeface.BOLD)
                var ocena=TextView(context)
                ocena.layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                ocena.text=clan.ocena.toString()
                ocena.textSize=25f
                var komentarText=TextView(context)
                komentarText.layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                komentarText.text="Komentar"
                komentarText.textSize=25f
                komentarText.setTypeface(null,Typeface.BOLD)
                var komentar=TextView(context)
                var layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                komentar.layoutParams=layoutParams
                komentar.text=clan.komentar.toString()
                komentar.textSize=20f
                var datumVreme=TextView(context)
                datumVreme.layoutParams=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                datumVreme.text=clan.datum+" u "+clan.vreme
                datumVreme.textSize=20f
                var odvajac=Button(context)
                var layoutParamsDugme=LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT

                )
                var horizontalni=LinearLayout(context)
                horizontalni.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                horizontalni.orientation=LinearLayout.HORIZONTAL
                var pozitivni=Button(context)
                var negativni=Button(context)
                var layoutPara1=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                var layoutPara2=LinearLayout.LayoutParams(98.dpToPx(),68.dpToPx())
                pozitivni.layoutParams=layoutPara2
                negativni.layoutParams=layoutPara2
                pozitivni.text="+"
                var pozitivniText=TextView(context)
                pozitivniText.textSize=30f
                pozitivniText.text=clan.pozitivni.toString()
                layoutPara1.setMargins(0, 0, 128.dpToPx(), 0)
                pozitivniText.layoutParams=layoutPara1
                pozitivni.setPadding(16.dpToPx(),16.dpToPx(),16.dpToPx(),16.dpToPx())
                negativni.setPadding(16.dpToPx(),16.dpToPx(),16.dpToPx(),16.dpToPx())
                pozitivni.textSize=30f
                negativni.text="-"
                negativni.textSize=30f
                var negativniText=TextView(context)
                negativniText.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                negativniText.textSize=30f
                negativniText.text=clan.negativni.toString()
                pozitivni.setTextColor(resources.getColor(R.color.green))
                negativni.setTextColor(resources.getColor(R.color.red))
                pozitivniText.setTextColor(resources.getColor(R.color.green))
                negativniText.setTextColor(resources.getColor(R.color.red))
                horizontalni.addView(pozitivni)
                horizontalni.addView(pozitivniText)
                horizontalni.addView(negativniText)
                horizontalni.addView(negativni)
                odvajac.layoutParams=layoutParamsDugme
                odvajac.text=""
                pozitivni.hint=clan.id
                for(provera in nizOsoba)
                {
                    if(provera.idKorisnika==sharedViewModel.ime&&provera.idKomentara==clan.id&&provera.podrzao==true)
                    {
                        pozitivni.setBackgroundColor(resources.getColor(R.color.lightGreen))

                    }
                    else if(provera.idKorisnika==sharedViewModel.ime&&provera.idKomentara==clan.id&&provera.podrzao==false)
                    {
                        negativni.setBackgroundColor(resources.getColor(R.color.lightRed))

                    }
                }
                pozitivni.setOnClickListener{

                    DataBase.databaseComments.child(pozitivni.hint.toString()).get().addOnSuccessListener {snapshot->
                        if(snapshot.exists())
                        {
                            var komentarBaza=Comments(snapshot.child("id").value.toString(),snapshot.child("autor").value.toString(),snapshot.child("mesto").value.toString(),snapshot.child("ocena").value.toString().toInt(),snapshot.child("komentar").value.toString(),snapshot.child("datum").value.toString(),snapshot.child("vreme").value.toString(),snapshot.child("pozitivni").value.toString().toInt(),snapshot.child("negativni").value.toString().toInt())
                            var neutralan=false
                            var menja=false
                            var idKomentara=""
                            for(podrzao in nizOsoba)
                            {
                                //VEC JE TAJ KORISNIK ZA TAJ KOMENTAR STAVIO POZITIVAN
                                if(podrzao.idKomentara==pozitivni.hint&&podrzao.idKorisnika==sharedViewModel.ime&&podrzao.podrzao==true)
                                {
                                    neutralan=true
                                    menja=false
                                    idKomentara=podrzao.id
                                    break



                                }
                                //VEC JE TAJ KORISNIK ZA TAJ KOMENTAR STAVIO NEGATIVAN
                               else if(podrzao.idKorisnika==sharedViewModel.ime&&podrzao.idKomentara==pozitivni.hint&&podrzao.podrzao==false)
                                {
                                    //ODUZMI PRETHODNO DODAT NEGATIVNI I OBRISI GA IZ BAZE TAJ 1 TO 1 VEZU
                                    neutralan=false
                                    menja=true
                                    idKomentara=podrzao.id
                                    break



                                }


                            }
                            if(neutralan==false&&menja==false)
                            {
                                komentarBaza.pozitivni = komentarBaza.pozitivni.plus(1)
                                DataBase.databaseComments.child(komentarBaza.id)
                                    .setValue(komentarBaza).addOnSuccessListener {
                                        var id = System.currentTimeMillis().toString()
                                        var pozitivan = KorisnikKomentarP(
                                            id,
                                            sharedViewModel.ime,
                                            pozitivni.hint.toString(),
                                            true
                                        )
                                        DataBase.dataBaseOneToOne.child(id).setValue(pozitivan)
                                            .addOnSuccessListener {
                                                DataBase.databaseUsers.child(
                                                    sharedViewModel.ime.replace(".", "")
                                                        .replace("#", "")
                                                        .replace("$", "").replace("[", "")
                                                        .replace("]", "")
                                                ).get().addOnSuccessListener { snapshotU ->
                                                    if (snapshotU.exists()) {
                                                        sharedViewModel.user = User(
                                                            snapshotU.child("korisnicko").value.toString(),
                                                            snapshotU.child("sifra").value.toString(),
                                                            snapshotU.child("ime").value.toString(),
                                                            snapshotU.child("prezime").value.toString(),
                                                            snapshotU.child("brojTelefona").value.toString()
                                                                .toLongOrNull(),
                                                            snapshotU.child("img").value.toString(),
                                                            ArrayList(),
                                                            snapshotU.child("bodovi").value.toString()
                                                                .toIntOrNull()
                                                        )
                                                        sharedViewModel.user.bodovi =
                                                            sharedViewModel.user.bodovi?.plus(2)
                                                        DataBase.databaseUsers.child(
                                                            sharedViewModel.ime.replace(".", "")
                                                                .replace("#", "")
                                                                .replace("$", "").replace("[", "")
                                                                .replace("]", "")
                                                        ).setValue(sharedViewModel.user)
                                                            .addOnSuccessListener {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Dobili ste 2 boda ",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()

                                                            }

                                                    }
                                                }
                                            }
                                    }


                            }
                           else if(neutralan==true)
                            {
                                komentarBaza.pozitivni=komentarBaza.pozitivni.minus(1)
                                //AKO JE PRETHODNO TAJ KOMENTAR PODRZAO INDIKATOR JE TRUE

                                DataBase.dataBaseOneToOne.child(idKomentara).removeValue().addOnSuccessListener {
                                    DataBase.databaseComments.child(komentarBaza.id).setValue(komentarBaza).addOnSuccessListener {
                                        DataBase.databaseUsers.child(
                                            sharedViewModel.ime.replace(".", "")
                                                .replace("#", "")
                                                .replace("$", "").replace("[", "")
                                                .replace("]", "")
                                        ).get().addOnSuccessListener { snapshotU ->
                                            if (snapshotU.exists()) {
                                                sharedViewModel.user = User(
                                                    snapshotU.child("korisnicko").value.toString(),
                                                    snapshotU.child("sifra").value.toString(),
                                                    snapshotU.child("ime").value.toString(),
                                                    snapshotU.child("prezime").value.toString(),
                                                    snapshotU.child("brojTelefona").value.toString()
                                                        .toLongOrNull(),
                                                    snapshotU.child("img").value.toString(),
                                                    ArrayList(),
                                                    snapshotU.child("bodovi").value.toString()
                                                        .toIntOrNull()
                                                )
                                                sharedViewModel.user.bodovi =
                                                    sharedViewModel.user.bodovi?.minus(2)
                                                DataBase.databaseUsers.child(
                                                    sharedViewModel.ime.replace(".", "")
                                                        .replace("#", "")
                                                        .replace("$", "").replace("[", "")
                                                        .replace("]", "")
                                                ).setValue(sharedViewModel.user)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Izgubili ste 2 boda",
                                                            Toast.LENGTH_SHORT
                                                        ).show()

                                                    }

                                            }
                                        }
                                    }


                                }

                            }
                            else if(menja==true)
                            {
                                komentarBaza.negativni=komentarBaza.negativni.minus(1)
                                komentarBaza.pozitivni=komentarBaza.pozitivni.plus(1)
                                DataBase.dataBaseOneToOne.child(idKomentara).removeValue().addOnSuccessListener {DataBase.databaseComments.child(komentarBaza.id).setValue(komentarBaza).addOnSuccessListener {
                                    var id = System.currentTimeMillis().toString()
                                    var pozitivan = KorisnikKomentarP(
                                        id,
                                        sharedViewModel.ime,
                                        pozitivni.hint.toString(),
                                        true
                                    )
                                    DataBase.dataBaseOneToOne.child(id).setValue(pozitivan)
                                        .addOnSuccessListener {



                                        }

                                } }

                            }

                        }

                    }
                }
                negativni.hint=clan.id
                negativni.setOnClickListener{

                    DataBase.databaseComments.child(negativni.hint.toString()).get().addOnSuccessListener {snapshot->
                        if(snapshot.exists())
                        {
                            var neutralan=false
                            var menja=false
                            var komentarId=""
                            var komentarBaza=Comments(snapshot.child("id").value.toString(),snapshot.child("autor").value.toString(),snapshot.child("mesto").value.toString(),snapshot.child("ocena").value.toString().toInt(),snapshot.child("komentar").value.toString(),snapshot.child("datum").value.toString(),snapshot.child("vreme").value.toString(),snapshot.child("pozitivni").value.toString().toInt(),snapshot.child("negativni").value.toString().toInt())
                            for(podrzao in nizOsoba)
                            {
                                //VEC JE TAJ KORISNIK ZA TAJ KOMENTAR STAVIO NEGATIVAN
                                if(podrzao.idKomentara==pozitivni.hint&&podrzao.idKorisnika==sharedViewModel.ime&&podrzao.podrzao==false)
                                {
                                    neutralan=true
                                    menja=false
                                    komentarId=podrzao.id
                                    break



                                }
                               else if(podrzao.idKorisnika==sharedViewModel.ime&&podrzao.idKomentara==negativni.hint&&podrzao.podrzao==true)
                                {
                                    neutralan=false
                                    menja=true
                                    komentarId=podrzao.id
                                    break

                                }


                            }
                            if(neutralan==false&&menja==false) {
                                komentarBaza.negativni = komentarBaza.negativni.plus(1)

                                DataBase.databaseComments.child(komentarBaza.id)
                                    .setValue(komentarBaza).addOnSuccessListener {
                                        var id = System.currentTimeMillis().toString()
                                        var negativan = KorisnikKomentarP(
                                            id,
                                            sharedViewModel.ime,
                                            negativni.hint.toString(),
                                            false
                                        )
                                        DataBase.dataBaseOneToOne.child(id).setValue(negativan)
                                            .addOnSuccessListener {
                                                DataBase.databaseUsers.child(
                                                    sharedViewModel.ime.replace(".", "")
                                                        .replace("#", "")
                                                        .replace("$", "").replace("[", "")
                                                        .replace("]", "")
                                                ).get().addOnSuccessListener { snapshotU ->
                                                    if (snapshotU.exists()) {
                                                        sharedViewModel.user = User(
                                                            snapshotU.child("korisnicko").value.toString(),
                                                            snapshotU.child("sifra").value.toString(),
                                                            snapshotU.child("ime").value.toString(),
                                                            snapshotU.child("prezime").value.toString(),
                                                            snapshotU.child("brojTelefona").value.toString()
                                                                .toLongOrNull(),
                                                            snapshotU.child("img").value.toString(),
                                                            ArrayList(),
                                                            snapshotU.child("bodovi").value.toString()
                                                                .toIntOrNull()
                                                        )
                                                        sharedViewModel.user.bodovi =
                                                            sharedViewModel.user.bodovi?.plus(2)
                                                        DataBase.databaseUsers.child(
                                                            sharedViewModel.ime.replace(".", "")
                                                                .replace("#", "")
                                                                .replace("$", "").replace("[", "")
                                                                .replace("]", "")
                                                        ).setValue(sharedViewModel.user)
                                                            .addOnSuccessListener {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Dobili  ste 2 boda",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()

                                                            }

                                                    }
                                                }
                                            }


                                    }
                            }
                            else if(neutralan==true)
                            {
                                komentarBaza.negativni=komentarBaza.negativni.minus(1)
                                DataBase.dataBaseOneToOne.child(komentarId).removeValue().addOnSuccessListener{
                                    DataBase.databaseComments.child(komentarBaza.id).setValue(komentarBaza).addOnSuccessListener {
                                        DataBase.databaseUsers.child(
                                            sharedViewModel.ime.replace(".", "")
                                                .replace("#", "")
                                                .replace("$", "").replace("[", "")
                                                .replace("]", "")
                                        ).get().addOnSuccessListener { snapshotU ->
                                            if (snapshotU.exists()) {
                                                sharedViewModel.user = User(
                                                    snapshotU.child("korisnicko").value.toString(),
                                                    snapshotU.child("sifra").value.toString(),
                                                    snapshotU.child("ime").value.toString(),
                                                    snapshotU.child("prezime").value.toString(),
                                                    snapshotU.child("brojTelefona").value.toString()
                                                        .toLongOrNull(),
                                                    snapshotU.child("img").value.toString(),
                                                    ArrayList(),
                                                    snapshotU.child("bodovi").value.toString()
                                                        .toIntOrNull()
                                                )
                                                sharedViewModel.user.bodovi =
                                                    sharedViewModel.user.bodovi?.minus(2)
                                                DataBase.databaseUsers.child(
                                                    sharedViewModel.ime.replace(".", "")
                                                        .replace("#", "")
                                                        .replace("$", "").replace("[", "")
                                                        .replace("]", "")
                                                ).setValue(sharedViewModel.user)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Izgubili ste 2 boda",
                                                            Toast.LENGTH_SHORT
                                                        ).show()

                                                    }

                                            }
                                        }
                                    }

                                }

                            }
                            else if(menja==true)
                            {
                                komentarBaza.pozitivni=komentarBaza.pozitivni.minus(1)
                                komentarBaza.negativni=komentarBaza.negativni.plus(1)
                                DataBase.dataBaseOneToOne.child(komentarId).removeValue().addOnSuccessListener {
                                    var id=System.currentTimeMillis()
                                    var novi=KorisnikKomentarP(id.toString(),sharedViewModel.ime,negativni.hint.toString(),false)
                                    DataBase.dataBaseOneToOne.child(id.toString()).setValue(novi).addOnSuccessListener { DataBase.databaseComments.child(komentarBaza.id).setValue(komentarBaza).addOnSuccessListener {
                                        
                                    } }
                                }




                            }

                        }

                    }

                }
                layout.addView(autorText)
                layout.addView(autor)
                layout.addView(ocenaText)
                layout.addView(ocena)
                layout.addView(komentarText)
                layout.addView(komentar)
                layout.addView(datumVreme)
                layout.addView(horizontalni)
                layout.addView(odvajac)
                layout.setBackgroundColor(Color.parseColor("#C5BCC6"))



            }


        }
        if(kolko==0)
        {
            var poruka=TextView(context)
            poruka.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            poruka.textAlignment=TextView.TEXT_ALIGNMENT_CENTER
            poruka.textSize=40f
            poruka.gravity=LinearLayout.TEXT_ALIGNMENT_CENTER
            poruka.text="Za mesto ${locationViewModel.nazivMesta.value.toString()} jos uvek nije napisan ni jedan komentar"
            glavni.addView(poruka)
        }

    }
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }


}