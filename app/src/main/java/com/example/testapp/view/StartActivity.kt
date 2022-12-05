package com.example.testapp.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.util.rangeTo
import com.example.testapp.R
import com.example.testapp.databinding.ActivityStartBinding
import java.lang.NumberFormatException

const val AGE_MINIMUM_LINE = 18
const val AGE_MAXIMUM_LINE = 99
const val AGE_DEFAULT_SHARED_VALUE = 171

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager =  getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

        enterAgeDataShared(getSharedInput())

        binding.ivLogo.setImageResource(R.drawable.ic_launcher_background)
        binding.btnEnterAge.setOnClickListener {
            enterAgeData()
        }
    }

    private fun enterAgeData() {
        val ageInput: Int
        val correctAgeIntent = Intent(this@StartActivity,WebViewActivity::class.java)
        val incorrectAgeIntent = Intent(this@StartActivity,DetailsActivity::class.java)

        try {
            ageInput = binding.etAgeInput.text.toString().toInt()
            if (ageInput in AGE_MINIMUM_LINE until AGE_MAXIMUM_LINE){
                startActivity(correctAgeIntent)
                saveSharedInput(ageInput)
            }
            else if(ageInput < AGE_MINIMUM_LINE){
                incorrectAgeIntent.putExtra("Error","Отказано в доступе")
                startActivity(incorrectAgeIntent)
                saveSharedInput(ageInput)
            }
            else {
                Toast.makeText(this,"Хорошая попытка, пожалуйста введите ваш реальный возраст",Toast.LENGTH_SHORT).show()
            }
        } catch (e:NumberFormatException){}
    }

    private fun enterAgeDataShared(sharedAge:Int) {
        val correctAgeIntent = Intent(this@StartActivity,WebViewActivity::class.java)
        val incorrectAgeIntent = Intent(this@StartActivity,DetailsActivity::class.java)

        try {

            if (sharedAge in AGE_MINIMUM_LINE until AGE_MAXIMUM_LINE){
                startActivity(correctAgeIntent)
                saveSharedInput(sharedAge)
            }
            else if(sharedAge < AGE_MINIMUM_LINE){
                incorrectAgeIntent.putExtra("Error","Отказано в доступе")
                startActivity(incorrectAgeIntent)
                saveSharedInput(sharedAge)
            }
            else {}

        } catch (e:NumberFormatException){}
    }

    private fun saveSharedInput(ageInput:Int){
        val sharedPref = this.getSharedPreferences(R.string.shared_age.toString(),Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(R.string.shared_age.toString(), ageInput)
            apply()
        }
    }

    private fun getSharedInput(): Int {
        val sharedPref = this.getSharedPreferences(R.string.shared_age.toString(),Context.MODE_PRIVATE)
        return sharedPref.getInt(R.string.shared_age.toString(), AGE_DEFAULT_SHARED_VALUE)
    }

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            val intent = Intent(this@StartActivity,DetailsActivity::class.java)
            intent.putExtra("Error","Отсутствует интернет соединение, пожалуйста, проверьте и перезапустите приложение")
            startActivity(intent)
        }
    }
}