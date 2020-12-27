package ru.mvrlrd.mytranslator.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE


import kotlinx.android.synthetic.main.activity_description.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.MainViewModel


import androidx.lifecycle.Observer


import android.content.Intent
import android.net.Uri

import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.TextView
import coil.api.load

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class DescriptionActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        wordTextView.text = intent.getStringExtra("word")
        translationTextView.text = intent.getStringExtra("translation")
        descriptionImageView.load("https:${intent.getStringExtra("url")}")


    }
}
