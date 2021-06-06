package com.georgishukov.financetracker

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.georgishukov.financetracker.databinding.ActivityAddCostBinding

private lateinit var binding: ActivityAddCostBinding

class AddCostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var list_of_items = arrayOf(
        "Food & Drinks",
        "Shopping",
        "Housing",
        "Transportation",
        "Vehicle",
        "Life & Entertainment",
        "Communication",
        "Investments",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        val displayHomeAsUpEnabled = actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.typeOfCostSpinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.typeOfCostSpinner!!.setAdapter(arrayAdapter)
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
        Toast.makeText(applicationContext,"position " + position,Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

}