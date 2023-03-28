package com.example.weatherapp.ui.weatherhistorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentWeatherHistoryListBinding
import com.example.weatherapp.ui.extention.view.showSnackBar
import com.example.weatherapp.ui.weatherlist.WeatherListFragmentRequestResult
import com.google.android.material.snackbar.Snackbar

class WeatherHistoryListFragment : Fragment(){

    private var _binding: FragmentWeatherHistoryListBinding? = null
    private val binding get() = _binding!!

    private val adapter = WeatherHistoryListAdapter()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherHistoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: WeatherHistoryViewModel by lazy {
        ViewModelProvider(this)[WeatherHistoryViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer = { data: WeatherListFragmentRequestResult -> renderData(data) }
        binding.recyclerView.adapter = adapter
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getAll()
    }

    private fun renderData(data: WeatherListFragmentRequestResult) {
        when (data) {
            is WeatherListFragmentRequestResult.Error -> {
                //binding.loadingLayout.visibility = View.GONE
                binding.root.showSnackBar("Не получилось ${data.error}", Snackbar.LENGTH_LONG)
            }
            is WeatherListFragmentRequestResult.Loading -> {
                //binding.loadingLayout.visibility = View.VISIBLE
            }
            is WeatherListFragmentRequestResult.Success -> {
                //binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherListData)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherHistoryListFragment()
    }
}