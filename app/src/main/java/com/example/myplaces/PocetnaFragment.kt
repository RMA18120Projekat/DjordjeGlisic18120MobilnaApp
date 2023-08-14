package com.example.myplaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class PocetnaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_pocetna,container,false)
        val button: Button =view.findViewById(R.id.buttonKreni)
        button.setOnClickListener{
            findNavController().navigate(R.id.action_pocetnaFragment_to_izborFragment)
        }
        return view

    }


}