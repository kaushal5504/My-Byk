package com.tech.my_byk.ui.rideHistory

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tech.my_byk.R

class RideHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = RideHistoryFragment()
    }

    private lateinit var viewModel: RideHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ride_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RideHistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}