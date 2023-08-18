package com.example.myplaces

import android.content.ContentValues
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class SvojiKomentariFragment : Fragment() {

val sharedViewModel:KorisnikViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_svoji_komentari, container, false)

        var glavni=view.findViewById<LinearLayout>(R.id.osnova)
        var layout=LinearLayout(context)
        glavni.addView(layout)
        for(clan in sharedViewModel.getNizKomentara())
        {



        layout.orientation=LinearLayout.VERTICAL
        val layoutParamsLayout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
            layout.layoutParams= layoutParamsLayout
            layout.layoutParams = layoutParamsLayout
            var mestoText=TextView(context)
            mestoText.layoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mestoText.text="Naziv mesta"
            mestoText.textSize=25f
            mestoText.setTypeface(null,Typeface.BOLD)

            var mesto=TextView(context)
            mesto.layoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mesto.text=clan.mesto.toString()
            mesto.textSize=25f


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
            var obrisi=Button(context)
            var layoutParamsButton=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            obrisi.layoutParams=layoutParamsButton
            obrisi.hint=clan.id
            obrisi.text="Obrisi"
            obrisi.setOnClickListener{
                    DataBase.databaseComments.child(obrisi.hint.toString()).removeValue().addOnSuccessListener {
                   findNavController().navigate(R.id.action_svojiKomentariFragment_to_homeFragment)
                    }.addOnFailureListener { exception->Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show() }

            }
            layout.addView(mestoText)
            layout.addView(mesto)
            layout.addView(autorText)
            layout.addView(autor)
            layout.addView(ocenaText)
            layout.addView(ocena)
            layout.addView(komentarText)
            layout.addView(komentar)
            layout.addView(obrisi)
            layout.setBackgroundColor(Color.parseColor("#C5BCC6"))


        }











        return view

    }



}