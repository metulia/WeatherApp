package com.example.weatherapp.ui.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.data.weather_dto.WeatherDTO
import com.example.weatherapp.databinding.FragmentDetailsBinding
import com.example.weatherapp.domain.OnServerResponse
import com.example.weatherapp.domain.Weather
import com.example.weatherapp.ui.extention.view.showSnackBar
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
                    ?.let { it1 -> onResponse(it1) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var currentCityName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            receiver, IntentFilter(
                KEY_WEATHER_WAVE
            )
        )
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            //WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
            requireActivity().startService(
                Intent(
                    requireContext(),
                    DetailsService::class.java
                ).apply {
                    putExtra(KEY_EXTRA_LAT, it.city.lat)
                    putExtra(KEY_EXTRA_LON, it.city.lon)
                })
        }
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityName.text = currentCityName
            temperatureValue.text = weather.fact.temp.toString()
            feelsLikeValue.text = weather.fact.feels_like.toString()
            cityCoordinates.text =
                "${weather.info.lat} ${weather.info.lon}"
            mainView.showSnackBar("Получилось", Snackbar.LENGTH_LONG)
        }
    }

    companion object {

        const val KEY_BUNDLE_WEATHER = "weather"

        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}