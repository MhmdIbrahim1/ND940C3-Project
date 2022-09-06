package com.udacity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        file_name_textView.text =intent.getStringExtra(Constants.FILE_NAME)
        status_textView.text = intent.getStringExtra(Constants.STATUS)
        val status = intent.getBooleanExtra("status", true)
        if (status) {
            status_textView.text = "Success"

        }
        else {
            status_textView.text = "Fail"
        }
        ok_btn.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }



}
