package ru.mvrlrd.mytranslator.ui.fragments.words

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.words_in_category_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.WordsAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback

private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "WordsInCategoryFragment"
private const val CHOOSE_FILE_REQUEST_CODE = 111

class WordsListFragment : Fragment(), OnItemClickListener {

    private lateinit var wordsAdapter: WordsAdapter
    private val newWord: NewWordDialog by inject()
    private val wordsListViewModel: WordsListViewModel by inject()
    private val vibrator: Vibrator by inject()
    private lateinit var callback: ItemTouchHelper.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.get("categoryId")
        id?.let {
            wordsListViewModel.getAllWordsOfCategory(it as Long)
            wordsListViewModel.categoryId = id as Long
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        newWord.setTargetFragment(
            this,
            TARGET_FRAGMENT_REQUEST_CODE
        )
        wordsListViewModel.liveCards.observe(viewLifecycleOwner, Observer { wordList ->
            handleRecycler(wordList)
        })
        val root = inflater.inflate(R.layout.words_in_category_fragment, container, false)
        root.findViewById<FloatingActionButton>(R.id.gotoAddNewWordFab).setOnClickListener {
            newWord.show(parentFragmentManager, "addNewWordDialog")
//            categoriesViewModel.clearCategories()
        }
        root.findViewById<FloatingActionButton>(R.id.addListOfWordsFab).setOnClickListener {
            val intent = Intent()
                .setType("text/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select file"), 111)
        }
        wordsAdapter = WordsAdapter(this as OnItemClickListener)
        return root
    }

    private fun handleRecycler(wordList: List<Card>) {
        words_recycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = wordsAdapter.apply { collection = wordList as MutableList<Card> }
        }
        callback =
            SimpleItemTouchHelperCallback(
                words_recycler.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(words_recycler)
        words_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    gotoAddNewWordFab.hide()
                    addListOfWordsFab.hide()
                } else if (dy < 0) {
                    gotoAddNewWordFab.show()
                    addListOfWordsFab.show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "resultCode = $requestCode doesn't equal to Activity.Result_OK")
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            data?.getStringExtra(EXTRA_GREETING_MESSAGE)?.let {
                wordsListViewModel.saveWordToDb(it)
            }
        } else          // Selected a file to load
            if ((requestCode == CHOOSE_FILE_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
                val selectedFilename = data?.data //The uri with the location of the file
                if (selectedFilename != null) {
                    context?.contentResolver?.openInputStream(selectedFilename)?.bufferedReader()
                        ?.forEachLine {
                            wordsListViewModel.saveWordToDb(mapperAllStringsToOne(it))
                        }
                } else {
                    val msg = "Null filename data received!"
                    val toast = Toast.makeText(this.context, msg, Toast.LENGTH_LONG)
                    toast.show()
                }
            }
    }

    private fun mapperAllStringsToOne(oneString: String): String {
        val arr = oneString.split(";")
        val str2 = StringBuilder()
        if (arr.size == 2) {
            str2.append("{\"id\":\"0\",")
            str2.append("\"text\":\"${arr[0].changeSymbol()}\",")
            str2.append("\"translation\":\"${arr[1].changeSymbol()}\",")
            str2.append("\"image_url\":\"_\",")
            str2.append("\"transcription\":\"_\",")
            str2.append("\"partOfSpeech\":\"_\",")
            str2.append("\"prefix\":\"_\"}")
        }
        return str2.toString()
    }

    private fun String.changeSymbol(): String =
        this.replace("\"", "\\\"")

    override fun onItemClick(categoryId: Long) {
        Log.e(TAG, "onShortPressed")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(wordId: Long) {
        wordsListViewModel.deleteWordFromCategory(wordId)
        vibrate(vibrator)
    }

    override fun onItemLongPressed(categoryId: Long) {
        Log.e(TAG, "onLongPressed")
    }
}