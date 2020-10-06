package ru.mvrlrd.mytranslator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.ISearchWord
import ru.mvrlrd.mytranslator.presenter.MainPresenter

class MainActivity : AppCompatActivity(), ISearchWord {
    private lateinit var presenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter =  MainPresenter(this)
    }

    fun onClick(view: View){
        presenter.getTranslation()
    }

    override fun searchWord() : String {
        return searchWord.text.toString()
    }

    override fun showResult(s: String) {
        searchWord.setText(s)
    }
}
