package com.dicoding.asclepius.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.datasource.History
import com.dicoding.asclepius.helper.BitmapConverter
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop.*

import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private var cropImage : ActivityResultLauncher<String>? = null
    private var resultPrediction : String? = null
    private var resultConfident : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cropImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            val intent = Intent(this.applicationContext, UCropActivity::class.java)
            intent.putExtra("sourceUri", it.toString())
            startActivityForResult(intent, REQUEST_CROP)
        }

        with(binding){
            galleryButton.setOnClickListener {
                startGallery()
            }
            analyzeButton.setOnClickListener {
                analyzeImage()
            }
        }
    }

    private fun startGallery() {
        cropImage?.launch("image/*")
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = data?.getStringExtra("resultUri")
            currentImageUri = Uri.parse(resultUri)
            showImage()
            showToast("Image cropped successfully, click analyze to get the result")
        } else if(requestCode == REQUEST_CROP && resultCode == RESULT_CANCELED) {
            showToast("Image invalid, please use another image")
        }
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(currentImageUri)
    }

    private fun saveDataToLocal() {
        val favoriteData: History = History(application)
        currentImageUri?.let {
            val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(it))
            val history = HistoryEntity(
                id = SystemClock.currentThreadTimeMillis().toInt(),
                prediction = resultPrediction,
                confident = resultConfident,
                date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()).toString(),
                image = BitmapConverter.converterBitmapToString(bitmap)
            )
            favoriteData.insert(history)
            showToast("Data saved")
        }
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onResult(results: List<Classifications>, inferenceTime: Long) {
                    runOnUiThread {
                        results.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }
                                resultPrediction =
                                    sortedCategories.firstOrNull()?.label
                                resultConfident = sortedCategories.firstOrNull()?.let {
                                     NumberFormat.getPercentInstance()
                                         .format(it.score).trim()
                                }
                                saveDataToLocal()
                                moveToResult()
                            } else {
                                showToast("No result")
                            }
                        }
                    }
                }

                override fun onError(error: Throwable) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        )
        currentImageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.KEY_RESULT_PREDICTION, resultPrediction)
        intent.putExtra(ResultActivity.KEY_RESULT_CONFIDENT, resultConfident)
        intent.putExtra(ResultActivity.KEY_IMAGE_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.KEY_TYPE, 0)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}