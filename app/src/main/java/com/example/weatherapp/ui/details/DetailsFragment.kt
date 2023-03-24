package com.example.weatherapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.databinding.FragmentDetailsBinding
import com.example.weatherapp.ui.extention.view.showSnackBar
import com.google.android.material.snackbar.Snackbar

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData()
            .observe(viewLifecycleOwner, object : Observer<DetailsFragmentRequestResult> {
                override fun onChanged(t: DetailsFragmentRequestResult) {
                    renderData(t)
                }
            })

        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)
        }
    }

    fun renderData(detailsFragmentRequestResult: DetailsFragmentRequestResult) {
        when (detailsFragmentRequestResult) {
            is DetailsFragmentRequestResult.Error -> binding.loadingLayout.visibility =
                View.GONE
            is DetailsFragmentRequestResult.Loading -> binding.loadingLayout.visibility =
                View.VISIBLE
            is DetailsFragmentRequestResult.Success -> {
                val weather = detailsFragmentRequestResult.weather
                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text =
                        "${weather.city.lat} ${weather.city.lon}"
                    mainView.showSnackBar("Получилось", Snackbar.LENGTH_LONG)
                }
            }
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
}