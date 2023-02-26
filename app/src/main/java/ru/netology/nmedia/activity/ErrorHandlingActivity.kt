package ru.netology.nmedia.activity

import android.app.Notification.EXTRA_TEXT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityErrorHandlingBinding

class ErrorHandlingActivity : AppCompatActivity() {
    lateinit var binding: ActivityErrorHandlingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityErrorHandlingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.text=intent.getStringExtra("EXTRA_TEXT", )


    }
}