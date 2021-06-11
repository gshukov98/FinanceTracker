package com.georgishukov.financetracker

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.georgishukov.financetracker.model.Cost
import java.lang.String
import java.util.*

/**
 * Created by Georgi Shukov on 11.6.2021 г..
 */
class CostsAdapter: RecyclerView.Adapter<CostsAdapter.ViewHolder>
    {
        val TAG = "CostsAdapter"
        var costsRecyclerView: RecyclerView? = null
        var context: Context? = null
        var costsList: List<Cost>

        constructor(_context: Context, _costsList: List<Cost>) {
            context = _context
            costsList = _costsList

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.d(TAG, "onCreateViewHolder: started")
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.costs_list_layout, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val cost: Cost = costsList!![position]
            holder.typeTV.setText(cost.type.toString())
            holder.priceTV.setText(String.valueOf(cost.price))
            holder.timestampTV.setText(cost.timestamp.toString())
        }

        override fun getItemCount(): Int {
            return costsList!!.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var typeTV: TextView
            var priceTV: TextView
            var timestampTV: TextView
            override fun onClick(v: View) {}

            init {
                typeTV = itemView.findViewById(R.id.tv_type_costs_list)
                priceTV = itemView.findViewById(R.id.tv_price_costs_list)
                timestampTV = itemView.findViewById(R.id.tv_timestamp_costs_list)
            }
        }
}