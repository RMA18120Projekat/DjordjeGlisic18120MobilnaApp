package com.example.myplaces

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

class PretraziObjekatFragment : Fragment() {
    private  val sharedViewModel:KorisnikViewModel by activityViewModels()
    private val locationViewModel:LocationViewModel by activityViewModels()
    lateinit var nazivMesta: EditText
    lateinit var komentarMesta: EditText
    lateinit var ocenaMesta: EditText
    lateinit var progress: ProgressBar
    lateinit var pretrazi: Button
    lateinit var mesto:Places
    ////////////////////////////////////////
    lateinit var teren: Spinner

    var terenIzabran:String="Fudbalski"
    /////////////////////////////////////////
    lateinit var sirinaObruca: Spinner
    lateinit var sirinaText: TextView
    private  var sirinaIzabrana:String="Normlna sirina"
    lateinit var osobinaObruca: Spinner
    lateinit var osobinaText: TextView
    var osobinaIzabrana:String="Normalan osecaj"
    lateinit var podlogaKosarka: Spinner
    lateinit var podlogaKText: TextView
    var podlogaKIzabrana:String="Guma"
    lateinit var mrezica: Spinner
    lateinit var mrezicaText: TextView
    var mrezicaIzabrana:String="Ima"
    lateinit var kosevi: Spinner
    lateinit var koseviText: TextView
    var koseviIzabrana:String="305cm"
    ///////////////////////////////////////
    lateinit var posecenost: Spinner
    var posecenostIzabrana:String="Mala"
    lateinit var radijus:EditText
    lateinit var dimenzije: Spinner
    var dimenzijeIzabrana:String="Male"
    //////////////////////////////////////
    lateinit var mreza: Spinner
    lateinit var mrezaText: TextView
    var mrezaIzabrana:String="Ima"
    lateinit var golovi: Spinner
    lateinit var goloviText: TextView
    var goloviIzabrana:String="Manji"
    lateinit var podlogaFudbal: Spinner
    lateinit var podlogaFText: TextView
    var podlogaFIzabrana:String="Trava"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_pretrazi_objekat, container, false)
        nazivMesta=view.findViewById(R.id.FilterNazivMesta)
        komentarMesta=view.findViewById(R.id.FilterKomentar)
        ocenaMesta=view.findViewById(R.id.FilterOcena)
        radijus=view.findViewById(R.id.FilterRadijus)
        pretrazi=view.findViewById(R.id.buttonFilterPretrazi)
        teren=view.findViewById(R.id.spinnerFilterTeren)
        sirinaObruca=view.findViewById(R.id.spinnerFilterSirina)
        sirinaText=view.findViewById(R.id.FilterSirina)
        osobinaObruca=view.findViewById(R.id.spinnerFilterOsobina)
        osobinaText=view.findViewById(R.id.FilterOsobina)
        podlogaKosarka=view.findViewById(R.id.spinnerFilterPodlogaKosarka)
        podlogaKText=view.findViewById(R.id.FilterPodlogaKosarka)
        mrezica=view.findViewById(R.id.spinnerFilterMrezica)
        mrezicaText=view.findViewById(R.id.FilterMrezica)
        mreza=view.findViewById(R.id.spinnerFilterMreza)
        posecenost=view.findViewById(R.id.spinnerFilterPosecenost)
        dimenzije=view.findViewById(R.id.spinnerFilterDimenzije)
        mrezaText=view.findViewById(R.id.FilterMreza)
        golovi=view.findViewById(R.id.spinnerFilterGolovi)
        goloviText=view.findViewById(R.id.FilterGolovi)
        podlogaFudbal=view.findViewById(R.id.spinnerFilterPodlogaFudbal)
        podlogaFText=view.findViewById(R.id.FilterPodlogaFudbal)
        kosevi=view.findViewById(R.id.spinnerFilterKosevi)
        koseviText=view.findViewById(R.id.FilterKosevi)
        val tableLayout = view.findViewById<TableLayout>(R.id.tabelaFilter) // Pristup TableLayout-u
        tableLayout.visibility=View.GONE
        teren.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                terenIzabran=adapterView?.getItemAtPosition(positon).toString()
                Toast.makeText(context,"${terenIzabran}", Toast.LENGTH_SHORT).show()
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
        pretrazi.setOnClickListener{
            tableLayout.visibility=View.VISIBLE
            for(mesto in sharedViewModel.getMyPlaces()) {

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
                    mesto.naziv,
                    mesto.autor,
                    mesto.komentar,
                    mesto.ocena.toString(),
                    mesto.latituda,
                    mesto.longituda,
                    mesto.teren,
                    mesto.sirinaObruca,
                    mesto.osobinaObruca,
                    mesto.podlogaKosarka,
                    mesto.mrezica,
                    mesto.visinaKosa,
                    mesto.mreza,
                    mesto.golovi,
                    mesto.podlogaFudbal,
                    mesto.posecenost,
                    mesto.dimenzije


                )

                for (textValue in textArray) {
                    val textView = TextView(context)
                    textView.layoutParams = textParams
                    textView.text = textValue
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                    row.addView(textView)
                }

                tableLayout.addView(row)
            }


        }











        return view
    }
    fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }



}