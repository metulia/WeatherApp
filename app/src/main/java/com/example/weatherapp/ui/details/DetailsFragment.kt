package com.example.weatherapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    protected val binding get() = _binding!!

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    /*private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                //binding.message.text = "Не получилось ${data.error}"
                Snackbar.make(binding.mainView, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.cityName.text = data.weatherData.city.name
                binding.temperatureValue.text = data.weatherData.temperature.toString()
                binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
                binding.cityCoordinates.text =
                    "$data.weatherData.city.lat $data.weatherData.city.lon"
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
     */

    companion object {
        @JvmStatic
        fun newInstance() = DetailsFragment()
    }

}