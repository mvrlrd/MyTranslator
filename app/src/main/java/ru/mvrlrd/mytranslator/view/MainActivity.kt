package ru.mvrlrd.mytranslator.view

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.App
import ru.mvrlrd.mytranslator.presenter.ISearchWord
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ISearchWord {
    @Inject
     lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent.inject(this)

        viewModel.liveTranslations.observe(this, Observer { translation ->
            showResult(translation)
        })
    }

    fun onClick(view: View){
        viewModel.getTranslation(searchWord())
    }

    override fun searchWord() : String {
        return searchWord.text.toString()
    }

    override fun showResult(word :String) {
        searchWord.setText(word)
    }
}
