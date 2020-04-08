package com.quarantinealert.feature.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import com.quarantinealert.R
import com.quarantinealert.feature.base.BaseViewModel
import com.quarantinealert.model.Country
import com.quarantinealert.repository.CovidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application, private val covidRepository: CovidRepository) :
    BaseViewModel(application) {

    var searchCountriesLiveData = MutableLiveData<MutableList<Country>>()

    fun search(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countries = covidRepository.getCountriesFromName(q)
                launch(Dispatchers.Main) {
                    searchCountriesLiveData.postValue(countries)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }

    fun getContries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countries = covidRepository.getCountries()
                countries.reverse()
                val locationNow =
                    Country(context.getString(R.string.your_location), 0, 0, 0, 0.0, 0.0)
                countries.add(locationNow)
                countries.reverse()
                launch(Dispatchers.Main) {
                    searchCountriesLiveData.postValue(countries)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }
}