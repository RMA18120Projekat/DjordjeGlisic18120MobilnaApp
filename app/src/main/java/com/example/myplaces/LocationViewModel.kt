package com.example.myplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {

    private val _nazivMesta=MutableLiveData<String>("")
    private val _baza=MutableLiveData<Boolean>(false)
    private var svojaLati:Double=0.0
    private var svojaLongi:Double=0.0
    fun setSvojeKoordinate(lati:Double,longi:Double)
    {
        svojaLati=lati
        svojaLongi=longi

    }
    fun getSvojaLati():Double{return svojaLati}
    fun getSvojaLongi():Double{return svojaLongi}
    val nazivMesta:LiveData<String>
    get()=_nazivMesta
    val baza:LiveData<Boolean>
        get()=_baza
    private val _longitude=MutableLiveData<String>("")
    val longitude:LiveData<String>
        get()=_longitude
    private val _latitude=MutableLiveData<String>("")
    val latitude:LiveData<String>
        get()=_latitude
    var dodajObjekat:Boolean=false
    var samoPregled:Boolean=false
    var jedanObjekat:Boolean=false
    var komentarisiObjekat:Boolean=false
    private val _latitudaKoment=MutableLiveData<String>("")
    val latitudaKoment:LiveData<String>
        get()=_latitudaKoment
    private val _longitudaKoment=MutableLiveData<String>("")
    val longitudaKoment:LiveData<String>
        get()=_longitudaKoment


    // i sa neki od nji bude true zavisno od dugme na koje se klikne
    fun setLocation(lon:String,lat:String)
    {
        _longitude.value=lon
        _latitude.value=lat

    }
    fun setLocationAndName(lon:String,lat:String,NazivMesta:String,base:Boolean)
    {
        _longitude.value=lon
        _latitude.value=lat
        _nazivMesta.value=NazivMesta
        _baza.value=base

    }
    fun setLocationAndNameKoment(lon:String,lat:String,NazivMesta:String,base:Boolean)
    {
        _longitudaKoment.value=lon
        _latitudaKoment.value=lat
        _nazivMesta.value=NazivMesta
        _baza.value=base

    }
    fun setLocationKoment(lon:String,lat:String)
    {
        _longitudaKoment.value=lon
        _latitudaKoment.value=lat

    }
    fun setName(name:String)
    {
        _nazivMesta.value=name
    }

}