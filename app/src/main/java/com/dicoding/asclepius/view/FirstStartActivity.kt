package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.SettingPreferences
import com.dicoding.asclepius.databinding.ActivityFirstStartBinding
import com.dicoding.asclepius.viewmodels.FirstStartFactory
import com.dicoding.asclepius.viewmodels.FirstStartViewModel

class FirstStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstStartBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFirstStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnProceed.setOnClickListener {
            saveUsername()
        }
    }

    private fun saveUsername() {
        val uname = binding.etUsername.text.toString()
        val pref = SettingPreferences.getInstance(dataStore)
        val viewModel = ViewModelProvider(this, FirstStartFactory(pref)).get(FirstStartViewModel::class.java)
        viewModel.setUsername(uname)
        Toast.makeText(this, "Selamat Datang, $uname", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, DashboardActivity::class.java))
    }
}