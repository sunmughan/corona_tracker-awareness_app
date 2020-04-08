package com.quarantinealert.repository

import com.quarantinealert.model.Country
import com.quarantinealert.model.CountryLocation
import com.quarantinealert.model.TotalCases

interface CovidRepository {

    suspend fun saveCovidData(countriesLocation: MutableList<CountryLocation>)

    suspend fun getCountries(): MutableList<Country>

    suspend fun getCountriesByConfirmedCases(): MutableList<Country>

    suspend fun getCountriesFromName(name: String): MutableList<Country>

    suspend fun getTotalCases(): TotalCases

    suspend fun getLastUpdate(): String
}