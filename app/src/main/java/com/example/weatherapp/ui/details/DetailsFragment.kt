package com.example.weatherapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.data.repository.OnServerResponse
import com.example.weatherapp.data.repository.WeatherDTO
import com.example.weatherapp.data.repository.WeatherLoader
import com.example.weatherapp.databinding.FragmentDetailsBinding
import com.example.weatherapp.domain.Weather
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            WeatherLoader(this@DetailsFragment).loadWeather(it.city.lat, it.city.lon)
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

    private fun View.showSnackBar(
        text: String,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).show()
    }

    companion object {

        const val KEY_BUNDLE_WEATHER = "key"

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