package ru.mvrlrd.mytranslator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.MainViewModel

class HistoryActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        viewModel.loadHistory()

        viewModel.liveHistory.observe(this, Observer { translation ->

                translation.let {
                    recycler_view.apply {
                        layoutManager = LinearLayoutManager(this@HistoryActivity)
                        adapter = MyAdapter(translation)
                    }
                }

        })

    }

    fun clearHistory(view : View){
        viewModel.clearHistory()
        viewModel.loadHistory()
    }
}
