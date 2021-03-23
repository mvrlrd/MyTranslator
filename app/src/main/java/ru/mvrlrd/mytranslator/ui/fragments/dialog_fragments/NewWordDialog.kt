package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.adding_word_fragment.*
import kotlinx.android.synthetic.main.categories_fragment.*
import kotlinx.android.synthetic.main.item_word.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.adapters.TranslationAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback
import java.lang.StringBuilder

class NewWordDialog : DialogFragment(), IconsAdapter.IconAdapterListener, OnItemClickListener {

    private lateinit var translationAdapter: TranslationAdapter
    private val newWordViewModel: NewWordViewModel by inject()
//    lateinit var transTextField: TextInputEditText
// val liveWord2: LiveData<String> = MutableLiveData<String>()


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
        val textField: TextInputEditText = root.findViewById(R.id.newWordEditText)



//        newWordViewModel.loadDataFromWeb("true")

        root.findViewById<FloatingActionButton>(R.id.addNewWordFab)
            .setOnClickListener {
                val str = StringBuilder()
                str.append(textField.text.toString())
                textField.text?.clear()
                sendResult(str.toString())
                dismiss()
            }
//         transTextField = root.findViewById(R.id.newWordsTranscriptionEditText)

val text : EditText = root.findViewById(R.id.newWordEditText)

        text.addTextChangedListener{
            if (!it.isNullOrEmpty()) newWordViewModel.loadDataFromWeb(it.toString())
        }


        translationAdapter = TranslationAdapter(this as OnItemClickListener)
        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newWordViewModel.liveTranslationsList.observe(viewLifecycleOwner, Observer { listOfMeaning ->
            if (!listOfMeaning.isNullOrEmpty()){
                listOfMeaning[0].let {it.transcription.let {
                    newWordsTranscriptionEditText.setText("[$it]")
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
            layoutManager =
//                LinearLayoutManager(this.context)
                GridLayoutManager(this.context, 3)
            adapter = translationAdapter.apply { collection = translationList as MutableList<String> }
        }
//        callback =
//            SimpleItemTouchHelperCallback(
//                translations_recycler.adapter as ItemTouchHelperAdapter
//            )
//        ItemTouchHelper(callback).attachToRecyclerView(translations_recycler)


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

    override fun onIconClicked(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(categoryId: Long) {
        TODO("Not yet implemented")
    }

    override fun onItemSwiped(categoryId: Long) {
        TODO("Not yet implemented")
    }

    override fun onItemLongPressed(categoryId: Long) {
        TODO("Not yet implemented")
    }
}