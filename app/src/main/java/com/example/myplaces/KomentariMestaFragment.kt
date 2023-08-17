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
        try
        {
            DataBase.databaseComments.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    nizKomentara.clear()
                    val layout=LinearLayout(context)
                    for(commentSnapshot in snapshot.children)
                    {
                        val comment=commentSnapshot.getValue(Comments::class.java)
                        comment?.let {
                            if(it.mesto.toString()==locationViewModel.nazivMesta.value.toString())
                            {
                                nizKomentara.add(it)
                                var glavni=view.findViewById<LinearLayout>(R.id.glavni)
                                layout.orientation=LinearLayout.VERTICAL
                                layout.layoutParams=LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                layout.setBackgroundColor(Color.parseColor("#C5BCC6"))
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
                                autor.text=it.autor
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
                                ocena.text=it.ocena.toString()
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
                                komentar.text=it.komentar.toString()
                                komentar.textSize=20f
                                var drugiLayout=LinearLayout(context)
                                drugiLayout.layoutParams=LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT

                                )
                                drugiLayout.orientation=LinearLayout.HORIZONTAL
                                var plus=Button(context)
                                var layoutParams1 = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                var plusN=TextView(context)
                                var LayoutplusN=LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                    ,LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                plusN.text=it.pozitivni.toString()
                                LayoutplusN.marginEnd=(170 * resources.displayMetrics.density).toInt()
                                plusN.layoutParams=LayoutplusN
                                plus.layoutParams= layoutParams1
                                plus.text = "+"
                                plus.isEnabled=true
                                var minus=Button(context)
                                var layoutParams2 = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                minus.layoutParams= layoutParams2
                                minus.text = "-"
                                minus.isEnabled=true
                                var minusN=TextView(context)
                                minusN.layoutParams=LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                    ,LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                minusN.text=it.negativni.toString()
                                drugiLayout.addView(plus)
                                drugiLayout.addView(plusN)
                                drugiLayout.addView(minusN)
                                drugiLayout.addView(minus)
                                glavni.addView(autorText)
                                glavni.addView(autor)
                                glavni.addView(ocenaText)
                                glavni.addView(ocena)
                                glavni.addView(komentarText)
                                glavni.addView(komentar)
                                glavni.addView(drugiLayout)
                                plus.setOnClickListener{
                                    plus.isEnabled=false
                                    minus.isEnabled=false
                                    var comentar:Comments= Comments(it.id.toString(),autor.text.toString(),locationViewModel.nazivMesta.value.toString(),ocena.text.toString().toInt(), komentar.text.toString(),plusN.text.toString().toInt(),minusN.text.toString().toInt())
                                    comentar.pozitivni=comentar.pozitivni+1
                                    DataBase.databaseComments.child(it.id.toString()).setValue(comentar).addOnSuccessListener {
                                        Toast.makeText(context,"Podrzali ste komentar",Toast.LENGTH_SHORT).show()
                                    }
                                }
                                minus.setOnClickListener{
                                    plus.isEnabled=false
                                    minus.isEnabled=false
                                    var comentar:Comments= Comments(it.id.toString(),autor.text.toString(),locationViewModel.nazivMesta.value.toString(),ocena.text.toString().toInt(), komentar.text.toString(),plusN.text.toString().toInt(),minusN.text.toString().toInt())
                                    comentar.negativni=comentar.negativni-1
                                    DataBase.databaseComments.child(it.id.toString()).setValue(comentar).addOnSuccessListener {
                                        Toast.makeText(context,"Podrzali ste komentar",Toast.LENGTH_SHORT).show()
                                    }
                                }


                            }


                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(ContentValues.TAG, "Error fetching Places data: ${error.message}")
                }
            })



        }
        catch (e: Exception)
        {
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show()
        }

        return view
    }


}