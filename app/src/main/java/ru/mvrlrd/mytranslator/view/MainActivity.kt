package ru.mvrlrd.mytranslator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.MainViewModel

class MainActivity : AppCompatActivity()
{
      private val viewModel : MainViewModel by inject()
    private lateinit var intentToDescription: Intent
    private lateinit var intentToHistory: Intent



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentToDescription = Intent(this, DescriptionActivity::class.java)
        intentToHistory = Intent(this, HistoryActivity::class.java)

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

    fun onClickToTranslate(view: View){
        viewModel.loadData(searchWord())

    }
     fun onClickToHistory(view: View){
         startActivity(intentToHistory)
    }

     fun searchWord() : String {
        return searchWord.text.toString()
    }


}
