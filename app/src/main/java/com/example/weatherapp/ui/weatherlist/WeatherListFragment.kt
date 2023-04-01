package com.example.weatherapp.ui.weatherlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.data.repository.Weather
import com.example.weatherapp.databinding.FragmentWeatherListBinding
import com.example.weatherapp.domain.OnItemListClickListener
import com.example.weatherapp.ui.details.DetailsFragment
import com.example.weatherapp.ui.details.DetailsFragment.Companion.KEY_BUNDLE_WEATHER
import com.example.weatherapp.ui.experiments.WorkWithContentProviderFragment
import com.example.weatherapp.ui.extention.view.showSnackBar
import com.example.weatherapp.ui.main.MainViewModel
import com.google.android.material.snackbar.Snackbar

class WeatherListFragment : Fragment(), OnItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!

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

        setupWlFloatingActionButtonCities()
        setupWlFloatingActionButtonLocation()

        viewModel.getWeatherRussia()
    }

    private fun setupWlFloatingActionButtonCities() {
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

    private fun setupWlFloatingActionButtonLocation() {
        binding.wlLocationFloatingActionButton.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun getLocation() {
        TODO("Not yet implemented")
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_message))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access))
            { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

    }

    private fun mRequestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE_LOCATION
        )
    }

    private fun renderData(data: WeatherListFragmentRequestResult) {
        when (data) {
            is WeatherListFragmentRequestResult.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.root.showSnackBar("Не получилось ${data.error}", Snackbar.LENGTH_LONG)
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

        private const val REQUEST_CODE_LOCATION = 998

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