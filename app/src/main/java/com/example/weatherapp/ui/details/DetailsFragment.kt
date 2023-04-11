package com.example.weatherapp.ui.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.databinding.FragmentDetailsBinding
import com.example.weatherapp.ui.extention.view.loadSvg
import com.example.weatherapp.ui.extention.view.showSnackBar
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {

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

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData()
            .observe(viewLifecycleOwner, object : Observer<DetailsFragmentRequestResult> {
                override fun onChanged(t: DetailsFragmentRequestResult) {
                    renderData(t)
                }
            })

        if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable(KEY_BUNDLE_WEATHER, Weather::class.java)?.let {
                viewModel.getWeather(it.city)
            }
        } else {
            arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
                viewModel.getWeather(it.city)
            }
        }
    }

    fun renderData(detailsFragmentRequestResult: DetailsFragmentRequestResult) {
        when (detailsFragmentRequestResult) {
            is DetailsFragmentRequestResult.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    resources.getString(R.string.error), Snackbar.LENGTH_LONG
                )
            }
            is DetailsFragmentRequestResult.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.mainView.showSnackBar(
                    resources.getString(R.string.load),
                    Snackbar.LENGTH_LONG
                )
            }
            is DetailsFragmentRequestResult.Success -> {
                val weather = detailsFragmentRequestResult.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text =
                        "${weather.city.lat} ${weather.city.lon}"
                    //headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png") // coil
                    Picasso.get().load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                        .into(headerIcon)
                    weatherIcon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
                    mainView.showSnackBar(resources.getString(R.string.ok), Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    companion object {

        const val KEY_BUNDLE_WEATHER = "weather"
        const val YANDEX_API_KEY = "X-Yandex-API-Key"
        const val KEY_EXTRA_LAT = "lat"
        const val KEY_EXTRA_LON = "lon"
        const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
        const val YANDEX_ENDPOINT = "v2/informers?"

        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}