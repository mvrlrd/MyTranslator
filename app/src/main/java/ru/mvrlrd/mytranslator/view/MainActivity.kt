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
    lateinit var intent1: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

intent1 = Intent(this, DescriptionActivity::class.java)

        viewModel.liveTranslations.observe(this, Observer { translation ->
            if (translation.isNotEmpty()){
                translation[0].let {
                    intent1.putExtra("word", it.text)
                    intent1.putExtra("translation", it.meanings?.get(0)?.translation?.translation)
                    intent1.putExtra("url", it.meanings?.get(0)?.imageUrl)
                    startActivity(intent1)
                }
            }
        })
    }

    fun onClick(view: View){
        viewModel.loadData(searchWord())

//        val intent = Intent(this, DescriptionActivity::class.java).apply {
///           putExtra(EXTRA_MESSAGE, "jhhj")
//        }

    }

    override fun searchWord() : String {
        return searchWord.text.toString()
    }

    override fun showResult(word :String) {
        searchWord.setText(word)
    }
}
