package com.example.myplaces

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


class RangListaFragment : Fragment() {

    private lateinit var tableLayout:TableLayout
    private val sharedViewModel:KorisnikViewModel by activityViewModels()
    private var nizKorisnika:ArrayList<User> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_rang_lista, container, false)
        tableLayout=view.findViewById(R.id.tabelaRang)
        crtajTabelu()
        val nizObserver= Observer<ArrayList<User>>{ newValue->
            nizKorisnika=newValue

            crtajTabelu()
        }
        sharedViewModel.users.observe(viewLifecycleOwner,nizObserver)

        return view

    }
    private fun crtajTabelu()
    {
        tableLayout.removeAllViews()
        val tableRow = TableRow(context)
        tableRow.setBackgroundColor(Color.parseColor("#51B435"))
        tableRow.setPadding(10.dpToPx(), 10.dpToPx(), 10.dpToPx(), 10.dpToPx())
        tableRow.gravity = Gravity.CENTER
        // Lista sa sadržajem za TextView elemente
        val labels = listOf(
            "Ime", "Prezime", "Korisnicko ime", "Broj telefona",
            "Ukuapan broj bodova"
        )

        // Dodavanje TextView elemenata u TableRow
        for (label in labels) {
            val textView = TextView(context)
            textView.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            textView.text = label
            textView.textSize = 20f
            textView.setTypeface(null, Typeface.BOLD)
            textView.setPadding(0, 0, 30.dpToPx(), 0)
            textView.gravity = Gravity.CENTER
            tableRow.addView(textView)
        }

        // Dodavanje TableRow u TableLayout

        tableLayout.addView(tableRow)
        for(i in 0 until nizKorisnika.size)
        {
            for(j in i+1 until nizKorisnika.size)
            {
                if(nizKorisnika[i].bodovi!!.toInt()<nizKorisnika[j].bodovi!!.toInt())
                {
                    var pom=nizKorisnika[i]
                    nizKorisnika[i]=nizKorisnika[j]
                    nizKorisnika[j]=pom
                }
            }

        }
        for (user in nizKorisnika) {
            val row = TableRow(context) // Kreiranje TableRow-a
            val rowParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            row.layoutParams = rowParams
            row.setBackgroundColor(Color.parseColor("#F0F7F7"))
            row.setPadding(5.dpToPx(), 5.dpToPx(), 5.dpToPx(), 5.dpToPx())

            val textParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            textParams.marginEnd = 30.dpToPx()

            val textArray = arrayOf(
                user.ime,
                user.prezime,
                user.korisnicko,
                user.brojTelefona.toString(),
                user.bodovi.toString(),

            )

            for (textValue in textArray) {
                val textView = TextView(context)
                textView.layoutParams = textParams
                textView.text = textValue
                textView.textAlignment=LinearLayout.TEXT_ALIGNMENT_CENTER
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                row.addView(textView)
            }
            if(user.korisnicko==sharedViewModel.user.korisnicko)
            {
                row.setBackgroundColor(Color.parseColor("#ADABA5"))

            }

            tableLayout.addView(row)


        }

    }
    fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }


}