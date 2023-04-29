package com.tech.my_byk.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tech.my_byk.MainActivity
import com.tech.my_byk.Place
import com.tech.my_byk.R

class PlaceAdapter(val list :ArrayList<Place>): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    var places :List<Place> =list

    class PlaceViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val textViewLocation : TextView = itemView.findViewById(R.id.tvLocation)
        val textViewNoOfCycles : TextView = itemView.findViewById(R.id.tvCycleNo)
        val cardview : CardView =itemView.findViewById(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.places_design,parent,false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {

        var current :Place = list[position]

        holder.textViewLocation.text = current.location
        holder.textViewNoOfCycles.text = current.Number.toString()



    }


}