package com.example.myplaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide

class InfoFragment : Fragment() {
    lateinit var ime:TextView
    lateinit var slika:ImageView
    lateinit var prezime:TextView
    lateinit var brojTelefona:TextView
    lateinit var email:TextView
    lateinit var bodovi:TextView
    private val user:KorisnikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_info,container,false)
        //INICIJALIZACIJA PROMENLJIVIH
        slika=view.findViewById(R.id.slikaInfo)
        ime=view.findViewById(R.id.infoIme)
        prezime=view.findViewById(R.id.infoPrezime)
        brojTelefona=view.findViewById(R.id.infoTelefon)
        email=view.findViewById(R.id.infoKorisnicko)
        bodovi=view.findViewById(R.id.infoBodovi)
        if(user.user.img!="")
        {
            Glide.with(requireContext())
                .load(user.user.img)
                .into(slika)
        }
        ime.text=user.user.ime
        prezime.text=user.user.prezime
        brojTelefona.text=user.user.brojTelefona.toString()
        email.text=user.user.korisnicko
        bodovi.text=100.toString()


        return view
    }





}