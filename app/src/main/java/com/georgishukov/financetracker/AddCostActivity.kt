package com.georgishukov.financetracker

import android.R
import android.app.Activity
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.georgishukov.financetracker.database.AppDatabase
import com.georgishukov.financetracker.database.CostDB
import com.georgishukov.financetracker.database.RelationsDB
import com.georgishukov.financetracker.databinding.ActivityAddCostBinding
import com.georgishukov.financetracker.model.Cost
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import kotlin.random.Random


const val ARG_PUT_COST = "cost"

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

    private lateinit var binding: ActivityAddCostBinding

    private var cost: Cost? = null

    private var isUpdate = false
    private var oldId: Long = 0L

    private var id: Long = 0L

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase(this)

        val actionBar = supportActionBar
        val displayHomeAsUpEnabled = actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.typeOfCostSpinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.typeOfCostSpinner!!.setAdapter(arrayAdapter)

        val bundle = intent.extras
        id = intent.getLongExtra("ID", -1)
        cost = bundle?.getParcelable(ARG_PUT_COST)

        if (cost != null) {
            isUpdate = true
            oldId = cost?.id!!
        }

        if (savedInstanceState == null) {
            val cost = cost
            if (cost != null) {
                binding.addPriceEditText.setText(cost.price.toString())
                binding.addDescriptionEditText.setText(cost.description)
            }
        }

        binding.addCostButton.setOnClickListener {
            if (!binding.typeOfCostSpinner.selectedItem.toString()
                    .isEmpty() && !binding.addPriceEditText.text.isEmpty()
            ) {
                //saveCost()
                val id = Random(1).nextLong(100000-1)
                val dstr1 = binding.addPriceEditText.text.toString()
                val doubleVal: Double = dstr1.toDouble()
                val currentDateTime = LocalDate.now()

                val date = convertToDateViaSqlDate(currentDateTime)

                val newCost = Cost(
                    id,
                    binding.typeOfCostSpinner.selectedItem.toString(),
                    binding.addDescriptionEditText.text.toString(),
                    doubleVal,
                    date
                )
                db.addCost(newCost)
                finish()
            } else {
                Toast.makeText(this, "Required fields are empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun costFromUI(): Cost {
        val id = Random(1).nextLong(100000-1)
        val dstr1 = binding.addPriceEditText.text.toString()
        val doubleVal: Double = dstr1.toDouble()
        val currentDateTime = LocalDate.now()

        val date = convertToDateViaSqlDate(currentDateTime)

        val newCost = Cost(
            id,
            binding.typeOfCostSpinner.selectedItem.toString(),
            binding.addDescriptionEditText.text.toString(),
            doubleVal,
            date
        )

        return newCost
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveCost() {
        val newCost = costFromUI()
        if (newCost != null) {
            putCost(newCost)
            setResult(Activity.RESULT_OK)
            finish()
        }

        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun putCost(newCost: Cost) {
        val values = ContentValues()
        values.put(CostDB.Columns.ID, newCost.id)
        values.put(CostDB.Columns.TYPE, newCost.type)
        values.put(CostDB.Columns.DESCRIPTION, newCost.description)
        values.put(CostDB.Columns.PRICE, newCost.price)
        values.put(CostDB.Columns.TIMESTAMP, newCost.timestamp.toString())


        if (!isUpdate) {
            GlobalScope.launch {
                val uri =
                    application.contentResolver?.insert(
                        CostDB.CONTENT_URI,
                        values
                    )
            }
        } else {
            GlobalScope.launch {
                application.contentResolver?.update(
                    CostDB.buildUriFromId(oldId), values, null, null
                )
            }
        }
    }


    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDateViaSqlDate(dateToConvert: LocalDate?): Date {
        return java.sql.Date.valueOf(dateToConvert.toString())
    }

}