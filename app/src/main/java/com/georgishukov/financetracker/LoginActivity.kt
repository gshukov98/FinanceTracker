package com.georgishukov.financetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.georgishukov.financetracker.database.AppDatabase
import com.georgishukov.financetracker.databinding.ActivityLoginBinding
import com.georgishukov.financetracker.model.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        var users: ArrayList<User> = ArrayList()

        val db = AppDatabase(this)
        val usersList: List<User>

        usersList = db.getUsers()

        binding.loginButton.setOnClickListener(View.OnClickListener {

            for (user in usersList){
                if(user.username.equals(binding.loginUsernameEditText.text.toString().trim())&&user.password.equals(binding.loginPasswordEditText.text.toString().trim())){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ID",user.id)
                    startActivity(intent)
                }
            }

            Toast.makeText(this,"Wrong username or password!", Toast.LENGTH_SHORT).show()

        })

        binding.registerTextView.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
    }

}