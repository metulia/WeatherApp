package com.example.weatherapp.ui.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherListBinding
import com.example.weatherapp.domain.OnItemListClickListener
import com.example.weatherapp.domain.Weather
import com.example.weatherapp.ui.MainViewModel
import com.example.weatherapp.ui.details.DetailsFragment
import com.example.weatherapp.ui.details.DetailsFragment.Companion.KEY_BUNDLE_WEATHER
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    protected val binding get() = _binding!!

    private val adapter = WeatherListAdapter(this)

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    var isRussian = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer = { data: WeatherListFragmentRequestResult -> renderData(data) }
        binding.recyclerView.adapter = adapter
        viewModel.getData().observe(viewLifecycleOwner, observer)

        setupWlFloatingActionButton()

        viewModel.getWeatherRussia()
    }

    private fun setupWlFloatingActionButton() {
        with(binding.wlFloatingActionButton) {
            setOnClickListener() {
                isRussian = !isRussian
                if (isRussian) {
                    viewModel.getWeatherRussia()
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_rus
                        )
                    )
                } else {
                    viewModel.getWeatherWorld()
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_world
                        )
                    )
                }
            }
        }
    }

    private fun renderData(data: WeatherListFragmentRequestResult) {
        when (data) {
            is WeatherListFragmentRequestResult.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is WeatherListFragmentRequestResult.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is WeatherListFragmentRequestResult.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherListData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            })).addToBackStack("").commit()
    }
}