package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.adding_word_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.adapters.TranslationAdapter

import kotlin.text.StringBuilder

private val TAG ="NewWordDialog"
class NewWordDialog : DialogFragment(), TranslationAdapter.OnClickTranslationListener {

    private lateinit var translationAdapter: TranslationAdapter
    private val newWordViewModel: NewWordViewModel by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.adding_word_fragment, container, false)

        root.findViewById<FloatingActionButton>(R.id.addNewWordFab)
            .setOnClickListener {
                sendResult(mapperAllStringsToOne())
                dismiss()
            }


        val text: EditText = root.findViewById(R.id.newWordEditText)
        text.addTextChangedListener {
            if (!it.isNullOrEmpty()) newWordViewModel.loadDataFromWeb(it.toString())
        }


        translationAdapter = TranslationAdapter(this as TranslationAdapter.OnClickTranslationListener)
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newWordViewModel.liveTranslationsList.observe(
            viewLifecycleOwner,
            Observer { listOfMeaning ->
                if (!listOfMeaning.isNullOrEmpty()) {
                    listOfMeaning[0].let { meaningModelForRecycler ->
                        meaningModelForRecycler.transcription.let {
                            newWordsTranscriptionEditText.setText("[$it]")
                        }
                        meaningModelForRecycler.translation.let {
                            newWordsTranslationEditText.setText(it)
                        }
                    }
                }
                val list = mutableListOf<String>()
                listOfMeaning.map {
                    it.translation?.let { it1 -> list.add(it1) }
                }
                handleCategoryRecycler(list)
            })
    }

    private fun handleCategoryRecycler(translationList: List<String>) {
        translations_recycler.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = translationAdapter.apply { collection = translationList as MutableList<String> }
        }
//        callback =
//            SimpleItemTouchHelperCallback(
//                translations_recycler.adapter as ItemTouchHelperAdapter
//            )
//        ItemTouchHelper(callback).attachToRecyclerView(translations_recycler)
    }
    private fun mapperAllStringsToOne():String{
        val str2 = StringBuilder()

        str2.append("{\"id\":\"0\",")
        str2.append("\"text\":\"${newWordEditText.text.toString()}\",")
        str2.append("\"translation\":\"${newWordsTranslationEditText.text.toString()}\",")
        str2.append("\"image_url\":\"www.ru!\",")
        str2.append("\"transcription\":\"${newWordsTranscriptionEditText.text.toString()}\",")
        str2.append("\"partOfSpeech\":\"noun!\",")
        str2.append("\"prefix\":\"the!\"}")

        return str2.toString()
    }

    private fun sendResult(str: String) {
        targetFragment ?: return
        val intent = Intent().putExtra("message", str)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun onPause() {
        super.onPause()
        newWordViewModel.clearLiveTranslationList()
        newWordsTranscriptionEditText.text?.clear()
        newWordsTranslationEditText.text?.clear()
        newWordEditText.text?.clear()
    }




    override fun onClickItem(translation: String) {
//        val text = newWordsTranslationEditText.text.toString()
                newWordsTranslationEditText.setText(translation.toString())
        Log.e(TAG, "on clicked $translation")
            }


}