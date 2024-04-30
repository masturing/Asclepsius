package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityUcropBinding
import com.yalantis.ucrop.UCrop
import java.lang.StringBuilder

class UCropActivity : AppCompatActivity() {
    private var sourceUri : String? = null
    private var destinationUri : String? = null
    private var uri: Uri? = null
    private lateinit var binding : ActivityUcropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUcropBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sourceUri = intent.getStringExtra("sourceUri")
        uri = Uri.parse(sourceUri)
        destinationUri = StringBuilder().append("file://").append(this.cacheDir).append("/").append(uri?.lastPathSegment).toString()
        var options : UCrop.Options = UCrop.Options()
        UCrop.of(uri!!, Uri.parse(destinationUri))
            .withMaxResultSize(680, 680)
            .withOptions(options)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            Log.d("UCrop", "Crop success: $resultUri")
            val intent = Intent()
            intent.putExtra("resultUri", resultUri.toString())
            setResult(RESULT_OK, intent)
            finish()
        } else if(requestCode == 69) {
            finish()
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("UCrop", "Crop error: $cropError")
        }
    }
}