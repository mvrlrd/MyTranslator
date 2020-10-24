package ru.mvrlrd.mytranslator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.MainViewModel

class HistoryActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyTextView.text = intent.getStringExtra("word")
    }
}
