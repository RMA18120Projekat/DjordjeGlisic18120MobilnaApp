package com.example.myplaces

data class User(val korisnicko:String?=null,val sifra:String?=null,val ime:String?=null,val prezime:String?=null,val brojTelefona:Long?=null,var img:String?="",var mesta:ArrayList<Places> =ArrayList())
