package com.tech.my_byk.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
//import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tech.my_byk.LoginActivity
import com.tech.my_byk.Place
import com.tech.my_byk.R
import com.tech.my_byk.adapters.PlaceAdapter
import com.tech.my_byk.databinding.FragmentHomeBinding
import java.util.*
import kotlin.collections.ArrayList
import android.widget.SearchView
import androidx.annotation.RequiresApi

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val databaseReference = FirebaseDatabase.getInstance().getReference("Places")
    private lateinit var adapter: PlaceAdapter
    private lateinit var list :ArrayList<Place>
    private lateinit var searchList :ArrayList<Place>
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
      //  searchList = arrayListOf<Place>()
        getLocationData()

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener
             {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchBar.clearFocus()

                    return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty())
                {
                    list.forEach { it->
                        if(it.location?.toLowerCase(Locale.getDefault())?.contains(searchText)!!)
                        {
                            searchList.add(it)
                        }

                    }

                    recyclerView.adapter!!.notifyDataSetChanged()


                }else
                {

                    searchList.clear()
                    searchList.addAll(list)
                    recyclerView.adapter!!.notifyDataSetChanged()

                }

                return false
            }


        })


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

                    searchList.addAll(list)
                    adapter= PlaceAdapter(searchList)
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}