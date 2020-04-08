package com.quarantinealert.repository

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.google.android.gms.maps.model.LatLng
import com.quarantinealert.R
import com.quarantinealert.data.database.AppDatabase
import com.quarantinealert.data.net.RestApi
import com.quarantinealert.data.session.SessionManager
import com.quarantinealert.exception.LocationNotFoundException
import com.quarantinealert.exception.NetworkException
import com.quarantinealert.model.Country
import com.quarantinealert.model.CountryLocation
import com.quarantinealert.model.TotalCases
import com.quarantinealert.util.NetworkUtil
import java.lang.Exception

class CovidRepositoryImpl(
    private val context: Context,
    private val restApi: RestApi,
    private val database: AppDatabase,
    private val sessionManager: SessionManager
) : CovidRepository {

    override suspend fun saveCovidData(countriesLocation: MutableList<CountryLocation>) {
        if (NetworkUtil.isOnline(context)) {

            val currentResponse = restApi.getCurrent()
            val data = currentResponse.data

            data.forEach {
                try {
                    val latLnt = getLatLngFromCountry(countriesLocation, it.location)
                    it.latitude = latLnt.latitude
                    it.longitude = latLnt.longitude
                } catch (ex: Exception) {
                    Crashlytics.logException(LocationNotFoundException("Location ${it.location} not found"))
                }
            }
            database.countryDao().deleteAll()
            database.countryDao().insertAll(data)
            sessionManager.lastDateUpdate = currentResponse.dt

            val totalResponse = restApi.getTotal()
            val totalCases = totalResponse.data
            totalCases.id = totalCases.javaClass.simpleName
            database.totalCasesDao().insert(totalCases)
        } else {
            if (database.countryDao().getCountry().isEmpty()) {
                throw NetworkException(context.getString(R.string.network_error_message))
            }
        }
    }

    private fun getLatLngFromCountry(
        countriesLocation: MutableList<CountryLocation>,
        countryName: String
    ): LatLng {
        val listFilter = countriesLocation.filter { it.name == countryName }
        val country = listFilter.first()
        return LatLng(country.latitude, country.longitude)
    }

    override suspend fun getCountries(): MutableList<Country> {
        return database.countryDao().getCountry()
    }

    override suspend fun getCountriesByConfirmedCases(): MutableList<Country> {
        return database.countryDao().getCountryByConfirmedCases()
    }

    override suspend fun getCountriesFromName(name: String): MutableList<Country> {
        return database.countryDao().getCountry(name)
    }

    override suspend fun getTotalCases(): TotalCases {
        return database.totalCasesDao().getTotalCases()
    }

    override suspend fun getLastUpdate(): String {
        return sessionManager.lastDateUpdate
    }
}