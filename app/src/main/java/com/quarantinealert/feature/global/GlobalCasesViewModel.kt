package com.quarantinealert.feature.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import com.quarantinealert.model.Country
import com.quarantinealert.model.TotalCases
import com.quarantinealert.repository.CovidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalCasesViewModel(private val covidRepository: CovidRepository) : ViewModel() {

    var showTotalCasesLiveData = MutableLiveData<TotalCases>()

    var getCountriesLiveData = MutableLiveData<MutableList<Country>>()

    var getLastUpdateLiveData = MutableLiveData<String>()

    fun getTotalCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalCases = covidRepository.getTotalCases()
                val countryCases = covidRepository.getCountriesByConfirmedCases()
                val lastUpdate = covidRepository.getLastUpdate()
                launch(Dispatchers.Main) {
                    showTotalCasesLiveData.postValue(totalCases)
                    getLastUpdateLiveData.postValue(lastUpdate)
                    getCountriesLiveData.postValue(countryCases)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }
}