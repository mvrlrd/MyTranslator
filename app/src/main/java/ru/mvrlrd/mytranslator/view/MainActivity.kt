package ru.mvrlrd.mytranslator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.ISearchWord
import ru.mvrlrd.mytranslator.presenter.MainViewModel


class MainActivity : AppCompatActivity(), ISearchWord {
      private val viewModel : MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        viewModel.liveTranslations.observe(this, Observer { translation ->
            showResult(translation)
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
