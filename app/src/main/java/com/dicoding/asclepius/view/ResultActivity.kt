package com.dicoding.asclepius.view
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.datasource.Article
import com.dicoding.asclepius.helper.BitmapConverter
import com.dicoding.asclepius.helper.NotifBarHelper
import com.dicoding.asclepius.viewmodels.ResultFactory
import com.dicoding.asclepius.viewmodels.ResultViewModel


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var type : Int? = null
    private var articles: ArrayList<Article> = arrayListOf()
    private val adapter: NewsAdapter by lazy {
        NewsAdapter(articles)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val resultPrediction = intent.getStringExtra(KEY_RESULT_PREDICTION)
        val resultConfident = intent.getStringExtra(KEY_RESULT_CONFIDENT)
        val imageUri = intent.getStringExtra(KEY_IMAGE_URI)
        type = intent.getIntExtra(KEY_TYPE, 0)
        setResultData(resultPrediction!!, resultConfident!!, imageUri!!)

        NotifBarHelper(window, this).setTransparent()
    }


    private fun setResultData(pred: String, conf: String, uri: String){
        with(binding){
            resultText.text = resources.getString(R.string.have_a_s_s, pred, conf)
            if(type == 0) {
                resultImage.setImageURI(Uri.parse(uri))
            } else {
                val bitmap = BitmapConverter.converterStringToBitmap(uri)
                resultImage.setImageBitmap(bitmap)
            }

            if(conf.split("%").first().toInt() > 50 && pred == "Cancer") {
                imgIndicator.setImageDrawable(resources.getDrawable(R.drawable.ic_warning))
            }

            val searchTerm = if(pred == "Cancer") "cancer" else "daily"

            val resultViewModel: ResultViewModel by viewModels {
                ResultFactory(searchTerm)
            }
            resultViewModel.news.observe(this@ResultActivity) { news ->
                if (news != null) {
                    news.articles?.let { articles.addAll(it) }
                    showRecyclerList()
                }
            }
            checkProgress(resultViewModel)
        }
    }

    private fun checkProgress(viewModel: ResultViewModel){
        viewModel.isLoading.observe(this) {
            showProgressBar(it)
        }
    }

    private fun showProgressBar(bool: Boolean){
        binding.progessBar.visibility = if (bool) View.VISIBLE else View.GONE
    }

    private fun showRecyclerList() {
        with(binding){
            val layoutManager = LinearLayoutManager(this@ResultActivity)
            this.rvArticle.layoutManager = layoutManager
            this.rvArticle.adapter = adapter
            adapter.setOnItemCLickCallback(object : NewsAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Article) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                    startActivity(intent)
                }
            })
        }
    }

    companion object {
        const val KEY_TYPE = "type"
        const val KEY_RESULT_PREDICTION = "resultPrediction"
        const val KEY_RESULT_CONFIDENT = "resultConfident"
        const val KEY_IMAGE_URI = "imageUri"
    }


}