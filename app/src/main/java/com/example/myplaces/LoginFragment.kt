package com.example.myplaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    lateinit var buttonLog:Button
    lateinit var email: EditText
    lateinit var pass:EditText
     lateinit var auth:FirebaseAuth
    lateinit var progrss:ProgressBar
    private  val sharedViewModel:KorisnikViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_login,container,false)
     buttonLog=view.findViewById(R.id.buttonLogovanje)
        email=view.findViewById(R.id.editTextTextEmailAddress)
        pass=view.findViewById(R.id.editTextTextPassword)

        progrss=view.findViewById(R.id.progressBar2)
        auth=FirebaseAuth.getInstance()
        buttonLog.setOnClickListener{
               progrss.visibility=View.VISIBLE
                val mejl = email.text.toString()
                val sifra = pass.text.toString()

                if (mejl.isNotEmpty() && sifra.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(mejl,sifra).addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            progrss.visibility=View.GONE
                            sharedViewModel.ime=mejl
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                        }
                        else
                        {
                            progrss.visibility=View.GONE
                            Toast.makeText(context,it.exception.toString(),Toast.LENGTH_SHORT)

                        }
                    }.addOnFailureListener {exception->
                        progrss.visibility=View.GONE
                        Toast.makeText(context,exception.toString(),Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Molim Vas unesite i email i sifru.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        return view
    }




}