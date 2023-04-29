package com.tech.my_byk.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.my_byk.Place
import com.tech.my_byk.R
import com.tech.my_byk.adapters.PlaceAdapter
import com.tech.my_byk.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val databaseReference = FirebaseDatabase.getInstance().getReference("Places")
    private lateinit var adapter: PlaceAdapter
    private lateinit var list :ArrayList<Place>
    private lateinit var recyclerView:RecyclerView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//
        recyclerView =root.findViewById(R.id.placeRecyclerView)
       recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.setHasFixedSize(true)

       // list = ArrayList<Place>()

        list = arrayListOf<Place>()
        getLocationData()




//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        return root
    }

    private fun getLocationData() {
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists())
                {
                    for(places in snapshot.children)
                    {
                       val locationData =places.getValue(Place::class.java)
                        list.add(locationData!!)
                    }
                    val placeAdapter = PlaceAdapter(list)
                    recyclerView.adapter = placeAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}