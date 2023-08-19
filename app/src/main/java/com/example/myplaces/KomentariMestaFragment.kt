package com.example.myplaces

import android.content.ContentValues
import android.content.res.Resources
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
import androidx.core.view.marginBottom
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class KomentariMestaFragment : Fragment() {
    val sharedViewModel:KorisnikViewModel by activityViewModels()
    var nizKomentara:ArrayList<Comments> = ArrayList()
    private val locationViewModel:LocationViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_komentari_mesta, container, false)
        val layout=LinearLayout(context)
        val glavni=view.findViewById<LinearLayout>(R.id.glavni)

        glavni.addView(layout)
        for(clan in sharedViewModel.getNizKomentara())
        {
            if(clan.mesto.toString()==locationViewModel.nazivMesta.value.toString())
            {
                Toast.makeText(context,"Uso sam u if",Toast.LENGTH_SHORT).show()
                nizKomentara.add(clan)
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
                odvajac.layoutParams=layoutParamsDugme
                odvajac.text=""
                layout.addView(autorText)
                layout.addView(autor)
                layout.addView(ocenaText)
                layout.addView(ocena)
                layout.addView(komentarText)
                layout.addView(komentar)
                layout.addView(datumVreme)
                layout.addView(odvajac)
                layout.setBackgroundColor(Color.parseColor("#C5BCC6"))



            }


        }



        return view
    }


}