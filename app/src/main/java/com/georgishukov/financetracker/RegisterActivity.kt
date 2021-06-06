package com.georgishukov.financetracker

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.georgishukov.financetracker.database.UserDB
import com.georgishukov.financetracker.databinding.ActivityRegisterBinding
import com.georgishukov.financetracker.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

const val ARG_PUT_USER = "user"

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var user: User? = null

    private var isUpdate = false
    private var oldId: Long = 0L;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        user = bundle?.getParcelable(ARG_PUT_USER)

        if (user != null) {
            isUpdate = true
            oldId = user?.id!!
        }

        if (savedInstanceState == null) {
            val user = user
            if (user != null) {
                binding.registerUsernameEditText.setText(user.username)
                binding.registerPasswordEditText.setText(user.password)
            }
        }

        binding.registerButton.setOnClickListener {
            if (binding.registerUsernameEditText.text.toString().length > 5 && binding.registerPasswordEditText.text.toString().length > 5 && binding.registerPasswordEditText.text.toString().equals(binding.registerPasswordConfirmEditText.text.toString()))
                saveUser()
            else{
                Toast.makeText(this,"Username and password should be more than 5 symbols length and password should be same",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun userFromUI(): User {
        val id = Random(100000).nextLong()
        val newUser = User(
            id,
            binding.registerUsernameEditText.text.toString(),
            binding.registerPasswordEditText.text.toString()
        )

        return newUser
    }

    private fun saveUser() {
        val newUser = userFromUI()
        if (newUser != null) {
            putUser(newUser)
            setResult(Activity.RESULT_OK)
            finish()
        }

        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun putUser(newUser: User) {
        val values = ContentValues()
        values.put(UserDB.Columns.ID, newUser.id)
        values.put(UserDB.Columns.USERNAME, newUser.username)
        values.put(UserDB.Columns.PASSWORD, newUser.password)

        if (!isUpdate) {
            GlobalScope.launch {
                val uri =
                    application.contentResolver?.insert(
                        UserDB.CONTENT_URI,
                        values
                    )
            }
        } else {
            GlobalScope.launch {
                application.contentResolver?.update(
                    UserDB.buildUriFromId(oldId), values, null, null
                )
            }
        }
    }


}