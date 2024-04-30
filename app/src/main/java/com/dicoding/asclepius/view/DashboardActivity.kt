package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.database.SettingPreferences
import com.dicoding.asclepius.databinding.ActivityDashboardBinding
import com.dicoding.asclepius.helper.NotifBarHelper
import com.dicoding.asclepius.viewmodels.DashboardFactory
import com.dicoding.asclepius.viewmodels.DashboardViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var history: ArrayList<HistoryEntity> = arrayListOf()
    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter(history)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(binding) {
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val collapsedPercentage = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
                if (collapsedPercentage >= 0.9f) {
                    collapsingToolbar.isTitleEnabled = true
                    collapsingToolbar.title = "Dashboard"
                } else {
                    collapsingToolbar.isTitleEnabled = false
                }
            })
            btnScan.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, MainActivity::class.java))
            }

        }
        initData()
        showRecyclerList()
        NotifBarHelper(window, this).setToColor(resources.getColor(R.color.primary))
    }

    private fun initData() {
        val pref = SettingPreferences.getInstance(dataStore)
        val viewModel: DashboardViewModel  by viewModels {
            DashboardFactory(pref, application)
        }
        viewModel.getUsername().observe(this) {
            if (it != null) {
                binding.tvTitleHeader.text = it
            }
        }
        viewModel.getHistoryList().observe(this@DashboardActivity) { favList ->
            if (favList != null) {
                adapter.addData(favList)
            }
            if (favList.isEmpty()) {
                showNoDataSaved(true)
            } else {
                showNoDataSaved(false)
            }
        }
    }

    private fun showNoDataSaved(b: Boolean) {
        binding.tvNoData.visibility =  if (b) View.VISIBLE else View.GONE
    }

    private fun showRecyclerList() {
        with(binding){
            val layoutManager = LinearLayoutManager(this@DashboardActivity)
            this.rvHistory.layoutManager = layoutManager
            this.rvHistory.adapter = adapter
            adapter.setOnItemCLickCallback(object : HistoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: HistoryEntity) {
                    val intent = Intent(this@DashboardActivity, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.KEY_IMAGE_URI, data.image)
                    intent.putExtra(ResultActivity.KEY_RESULT_PREDICTION, data.prediction)
                    intent.putExtra(ResultActivity.KEY_RESULT_CONFIDENT, data.confident)
                    intent.putExtra(ResultActivity.KEY_TYPE, 1)
                    startActivity(intent)
                }
            })
        }
    }


}