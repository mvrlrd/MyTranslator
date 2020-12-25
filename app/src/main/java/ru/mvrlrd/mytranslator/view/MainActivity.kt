package ru.mvrlrd.mytranslator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.service.extensions.observeData
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter

class MainActivity : AppCompatActivity(){

    lateinit var translationAdapter :  TranslationAdapter
    private val viewModel: MainViewModel by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        translationAdapter = TranslationAdapter()

        initializeView()
        viewModel.liveTranslations.observe(this, Observer { meanings ->
            handleTranslationList(meanings)

        })


    }


    fun onClickGo(view: View){
        viewModel.loadData(searchedWord_TextView.text.toString())

    }
    private fun handleTranslationList(list: List<MeaningModelForRecycler>) {
        translationAdapter.collection = list
    }

    private fun initializeView() {
      translation_recyclerview.layoutManager = LinearLayoutManager(this)
       translation_recyclerview.adapter = translationAdapter
    }

}
