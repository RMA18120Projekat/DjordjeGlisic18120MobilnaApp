package com.example.myplaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.w3c.dom.Text


class KomentarOcenaFragment : Fragment() {
private  lateinit var nazad:Button
private lateinit var posalji:Button
private lateinit var latituda:TextView
private lateinit var longituda:TextView
private lateinit var naziv:TextView
private lateinit var otvoriMape:Button
private lateinit var komentar:EditText
private lateinit var ocena:EditText
private lateinit var progress:ProgressBar
val locationViewModel:LocationViewModel by activityViewModels()
val sharedViewModel:KorisnikViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_komentar_ocena, container, false)
        //INICIJALIZACIJA PROMENLJIVIH KOMPONENTAMA
        nazad=view.findViewById(R.id.komentarNazad)
        posalji=view.findViewById(R.id.komentarPosalji)
        komentar=view.findViewById(R.id.editKomentar)
        ocena=view.findViewById(R.id.komentarOcena)
        latituda=view.findViewById(R.id.komentarLatituda)
        longituda=view.findViewById(R.id.konetarLongituda)
        naziv=view.findViewById(R.id.komentarNaziv)
        otvoriMape=view.findViewById(R.id.buttonOpenMap)
        progress=view.findViewById(R.id.upisKomentaraPB)
        //SUBSKRAJBOVANJE NA PROMENE KOORDINATA
        val lonObserver= Observer<String>{newValue->
            longituda.text=newValue.toString()
            sharedViewModel.longituda=longituda.text.toString()


        }
        locationViewModel.longitude.observe(viewLifecycleOwner,lonObserver)
        val latiObserver= Observer<String>{newValue->
            latituda.setText(newValue.toString())
            sharedViewModel.latituda=latituda.text.toString()

        }
        locationViewModel.latitude.observe(viewLifecycleOwner,latiObserver)
        val nazivObserver= Observer<String> {newValue->
            naziv.text=newValue.toString()
        }
        locationViewModel.nazivMesta.observe(viewLifecycleOwner,nazivObserver)
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
        posalji.setOnClickListener{
            if(naziv.text.toString().isEmpty()||ocena.text.toString().isEmpty()||komentar.text.toString().isEmpty())
            {
                Toast.makeText(context,"Niste popunili sva polja",Toast.LENGTH_SHORT).show()

            }
            else
            {
                var koment:Comments= Comments(sharedViewModel.id,sharedViewModel.ime,naziv.text.toString(),ocena.text.toString().toInt(),komentar.text.toString(),0,0)
                progress.visibility=View.VISIBLE
                DataBase.databaseComments.child(sharedViewModel.id.toString()).setValue(koment).addOnCompleteListener{
                    Toast.makeText(context,"Uspesno ste dodali komentar",Toast.LENGTH_SHORT).show()
                    progress.visibility=View.GONE
                    sharedViewModel.id=sharedViewModel.id+1
                    DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "")
                        .replace("$", "").replace("[", "").replace("]", "")
                    ).get().addOnSuccessListener { snapshotU->
                        if(snapshotU.exists())
                        {
                            sharedViewModel.user=User(snapshotU.child("korisnicko").value.toString(),snapshotU.child("sifra").value.toString(),snapshotU.child("ime").value.toString(),snapshotU.child("prezime").value.toString(),snapshotU.child("brojTelefona").value.toString().toLongOrNull(),snapshotU.child("img").value.toString(),ArrayList(),snapshotU.child("bodovi").value.toString().toIntOrNull())
                            sharedViewModel.user.bodovi=sharedViewModel.user.bodovi?.plus(5)
                            DataBase.databaseUsers.child(sharedViewModel.ime.replace(".", "").replace("#", "")
                                .replace("$", "").replace("[", "").replace("]", "")).setValue(sharedViewModel.user).addOnSuccessListener {
                                Toast.makeText(context,"Dobili ste jos 5 bodova",Toast.LENGTH_SHORT).show()

                                }.addOnFailureListener {
                                Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                            }
                        }
                    }.addOnFailureListener { Toast.makeText(context,"Greksa",Toast.LENGTH_LONG).show() }
                }.addOnFailureListener{
                    Toast.makeText(context,"Greska",Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }


}