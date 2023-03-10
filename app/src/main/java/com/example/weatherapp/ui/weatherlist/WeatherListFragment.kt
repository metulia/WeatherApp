package com.example.weatherapp.ui.weatherlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherListBinding
import com.example.weatherapp.domain.AppState
import com.example.weatherapp.domain.MainViewModel
import com.example.weatherapp.ui.details.DetailsFragment
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment : Fragment() {


    private var _binding: FragmentWeatherListBinding? = null
    protected val binding get() = _binding!!

    val adapter = WeatherListAdapter()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    var isRussian = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data)
            }
        }

        binding.recyclerView.adapter = adapter

        viewModel.getData().observe(viewLifecycleOwner, observer)

        binding.wlFloatingActionButton.setOnClickListener() {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherRussia()
                binding.wlFloatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_rus
                    )
                )
            } else {
                viewModel.getWeatherWorld()
                binding.wlFloatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_world
                    )
                )
            }
        }
        viewModel.getWeatherRussia()
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                //binding.message.text = "Не получилось ${data.error}"
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherListData)

                /*
                binding.cityName.text = data.weatherData.city.name
                binding.temperatureValue.text = data.weatherData.temperature.toString()
                binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
                binding.cityCoordinates.text =
                    "$data.weatherData.city.lat $data.weatherData.city.lon"
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()*/
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }
}
