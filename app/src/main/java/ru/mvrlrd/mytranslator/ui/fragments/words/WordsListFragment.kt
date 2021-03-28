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
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.categories_fragment.*
import kotlinx.android.synthetic.main.words_in_category_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.WordsAdapter
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback


private const val ARG_PARAM1 = "param"
private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "WordsInCategoryFragment"


class WordsListFragment : Fragment(), OnItemClickListener {

    private lateinit var wordsAdapter: WordsAdapter
    private val newWord: NewWordDialog by inject()
    private val wordsListViewModel: WordsListViewModel by inject()

    private val vibrator: Vibrator by inject()
    private lateinit var callback: ItemTouchHelper.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        val id = arguments?.get("categoryId")
        id?.let {
            wordsListViewModel.categoryId = id as Long
            wordsListViewModel.getAllWordsOfCategory(it as Long) }


        val root = inflater.inflate(R.layout.words_in_category_fragment, container, false)

        root.findViewById<FloatingActionButton>(R.id.gotoAddNewWordFab).setOnClickListener {
            newWord.show(parentFragmentManager, "addNewWordDialog")
//            categoriesViewModel.clearCategories()
        }
        wordsAdapter = WordsAdapter(this as OnItemClickListener)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        wordsListViewModel.liveWordList.observe(viewLifecycleOwner, Observer { wordList ->
            id?.let { handleRecycler(wordList) }
        })



    }

    private fun handleRecycler(wordList: List<CardOfWord>) {
        words_recycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = wordsAdapter.apply { collection = wordList as MutableList<CardOfWord> }
        }
        callback =
            SimpleItemTouchHelperCallback(
                words_recycler.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(words_recycler)

        words_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) gotoAddNewWordFab.hide()
                else if (dy < 0) gotoAddNewWordFab.show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            WordsListFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
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
        }
    }

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