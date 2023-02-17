package ru.netology.nmedia.activity

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.*

import ru.netology.nmedia.databinding.ActivityAvatarLoadBinding
import ru.netology.nmedia.databinding.CardPostBinding
import java.io.ByteArrayOutputStream


class AvatarLoadActivity : AppCompatActivity() {
    private val urls = listOf("netology.jpg", "sber.jpg", "tcs.jpg", "404.png")
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAvatarLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.load.setOnClickListener {
            if (index == urls.size) {
                index = 0
            }

            val url = "http://10.0.2.2:9999/avatars/${urls[index++]}"
            binding.image.load(url)
        }

val avatar=binding.image
        intent= Intent(this,CardPostBinding::class.java).also {
//            it.putExtra("image",avatar)

        }
    }
    fun ImageView.load(
        url: String,
        @DrawableRes placeholder: Int = ru.netology.nmedia.R.drawable.ic_baseline_upload_file_48,
        @DrawableRes fallback: Int = ru.netology.nmedia.R.drawable.ic_baseline_error_outline_48,
        timeOutMs: Int = 10_000
    ) {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .timeout(timeOutMs)
            .placeholder(placeholder)
            .error(fallback)

            .into(this)
    }
}