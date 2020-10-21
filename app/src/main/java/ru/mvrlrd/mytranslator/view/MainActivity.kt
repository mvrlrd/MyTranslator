package ru.mvrlrd.mytranslator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.ISearchWord
import ru.mvrlrd.mytranslator.presenter.MainViewModel

class MainActivity : AppCompatActivity(), ISearchWord {
      private val viewModel : MainViewModel by inject()
    private lateinit var intentToDescription: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentToDescription = Intent(this, DescriptionActivity::class.java)

        viewModel.liveTranslations.observe(this, Observer { translation ->
            if (translation.isNotEmpty()) {
                translation[0].let {
                    intentToDescription.putExtra("word", it.text)
                    intentToDescription.putExtra("translation", it.meanings?.get(0)?.translation?.translation)
                    intentToDescription.putExtra("url", it.meanings?.get(0)?.imageUrl)
                    startActivity(intentToDescription)
                }
            }
        })
    }

    fun onClick(view: View){
        viewModel.loadData(searchWord())
    }

    override fun searchWord() : String {
        return searchWord.text.toString()
    }

    override fun showResult(word :String) {
        searchWord.setText(word)
    }
}
