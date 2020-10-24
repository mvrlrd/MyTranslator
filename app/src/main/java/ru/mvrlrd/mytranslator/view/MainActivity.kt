package ru.mvrlrd.mytranslator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.currentScope
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presenter.ISearchWord
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ISearchWord {
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

        viewModel.liveHistory.observe(this, Observer { translation ->
            if (translation.isNotEmpty()) {
                translation.let {
                    intentToHistory.putExtra("word", it)
                    println("$it ttttttttttttttttttttttt")
                    startActivity(intentToHistory)
                }
            }
        })

    }

    fun onClick(view: View){
        viewModel.loadData(searchWord())
    }
     fun onClick2(view: View){
         viewModel.loadHistory()

    }

    override fun searchWord() : String {
        return searchWord.text.toString()
    }

    override fun showResult(word :String) {
        searchWord.setText(word)
    }
}
