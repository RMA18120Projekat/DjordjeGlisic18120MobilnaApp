package com.example.myplaces
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class RegisterFragment : Fragment() {
private val CAMERA_PERMISSION_REQUEST_CODE = 1001
lateinit var pass:EditText
lateinit var korisnickoIme:EditText
lateinit var ime:EditText
lateinit var prezime:EditText
lateinit var brojTelefona:EditText
lateinit var progress:ProgressBar
lateinit var dugme:Button
lateinit var database:DatabaseReference
lateinit var auth: FirebaseAuth
lateinit var imgUrl:String
private val REQUEST_IMAGE_CAPTURE = 1
private lateinit var storageRef: StorageReference
lateinit var user:User
private  val sharedViewModel:KorisnikViewModel by activityViewModels()

    private lateinit var openCameraButton: Button
private lateinit var imageView: ImageView
private lateinit var cekajSliku:ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_register,container,false)
        pass=view.findViewById(R.id.editTextNovaSifra)
         korisnickoIme=view.findViewById(R.id.editTextMejl)
         ime=view.findViewById(R.id.editTextIme)
         prezime=view.findViewById(R.id.editTextPrezime)
         brojTelefona=view.findViewById(R.id.editTextBrojTelefona)
         progress=view.findViewById(R.id.progressBar1)
         dugme=view.findViewById(R.id.buttonReg)
        auth=FirebaseAuth.getInstance()
        openCameraButton = view.findViewById(R.id.buttonPhoto)
        imageView = view.findViewById(R.id.imageView6)
        storageRef = FirebaseStorage.getInstance().reference
        cekajSliku=view.findViewById(R.id.cekajSlikuReg)

        openCameraButton.setOnClickListener{
            if (checkCameraPermission()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }

        dugme.setOnClickListener{
            progress.visibility=View.VISIBLE
            val korisnicko = korisnickoIme.text.toString()
            val sifra = pass.text.toString()
            val name=ime.text.toString()
            val surname=prezime.text.toString()
            val numberPhone=brojTelefona.text.toString()

            if (korisnicko.isNotEmpty() && sifra.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty() ) {
               auth.createUserWithEmailAndPassword(korisnicko,sifra).addOnCompleteListener{
                   if(it.isSuccessful)
                   {
                       progress.visibility=View.GONE
                       database=FirebaseDatabase.getInstance().getReference("Users")
                       user=User(korisnicko,sifra,name,surname,numberPhone.toLongOrNull(),sharedViewModel.img,ArrayList(),0)
                       val key = korisnicko.replace(".", "").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
                       database.child(key).setValue(user).addOnSuccessListener {
                           sharedViewModel.ime=korisnicko
                           korisnickoIme.text.clear()
                           prezime.text.clear()
                           pass.text.clear()
                           brojTelefona.text.clear()
                           ime.text.clear()
                            Toast.makeText(context,"Uspesna registracija, prijavite se na svoj nalog",Toast.LENGTH_LONG).show()

                       }.addOnFailureListener{
                           Toast.makeText(context,"Greska",Toast.LENGTH_SHORT).show()
                       }
                       findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                   }
                   else
                   {
                       progress.visibility=View.GONE
                       Toast.makeText(
                           requireContext(),
                        it.exception.toString(),
                           Toast.LENGTH_SHORT
                       ).show()

                   }
               }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Niste popunili svsa polja za registraciju.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view

    }
    private fun checkCameraPermission() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            // Create a reference to the image file in Firebase Storage
            val imagesRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
            imageView.visibility=View.GONE
            cekajSliku.visibility=View.VISIBLE
            // Convert the bitmap to bytes
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            // Upload the image to Firebase Storage
            val uploadTask = imagesRef.putBytes(imageData)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Image upload success
                    // Now you can get the download URL of the image and save it to the database
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save the URI to the database or use it as needed
                         imgUrl = uri.toString()
                      sharedViewModel.img=imgUrl
                        cekajSliku.visibility=View.GONE
                        imageView.visibility=View.VISIBLE
                        // Add the code to save the URL to the user's data in Firebase Database here
                    }.addOnFailureListener { exception ->
                        // Handle any errors that may occur while retrieving the download URL
                        Toast.makeText(requireContext(), "Failed to get download URL.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Image upload failed
                    val errorMessage = task.exception?.message
                    Toast.makeText(requireContext(), "Image upload failed. Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}