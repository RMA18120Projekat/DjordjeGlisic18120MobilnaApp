package com.example.myplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {


    private val _longitude=MutableLiveData<String>("")
    val longitude:LiveData<String>
        get()=_longitude
    private val _latitude=MutableLiveData<String>("")
    val latitude:LiveData<String>
        get()=_latitude
    var setLocation:Boolean=false
    var dodajObjekat:Boolean=false
    var samoPregled:Boolean=false
    var jedanObjekat:Boolean=false
    // i sa neki od nji bude true zavisno od dugme na koje se klikne
    fun setLocation(lon:String,lat:String)
    {
        _longitude.value=lon
        _latitude.value=lat
        setLocation=false
    }
}