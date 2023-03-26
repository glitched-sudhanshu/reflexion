package com.example.reflexionai.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.reflexionai.databinding.ActivityUserLoginBinding
import com.example.reflexionai.models.entities.Profile
import com.example.reflexionai.models.entities.UserProfile
import com.google.gson.Gson

class UserLogin : AppCompatActivity() {

    private lateinit var mBinding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var profileJson = sharedPreferences.getString("profile", null)


        if (profileJson == null) {
            mBinding.btnRegister.setOnClickListener{
                val name = mBinding.tilTxtName.text.toString()
                val phone = mBinding.tilTxtPhone.text.toString()
                val address = mBinding.tilTxtAddress.text.toString()
                val email = mBinding.tilTxtEmail.text.toString()
                val profile = Profile(name, phone, address, email)
                val gson = Gson()
                val profileJson = gson.toJson(profile)
                val editor = sharedPreferences.edit()
                editor.putString("profile", profileJson)
                editor.apply()
                UserProfile.profile = profile
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        } else {
            val gson = Gson()
            val profile = gson.fromJson(profileJson, Profile::class.java)
            UserProfile.profile = profile
            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}