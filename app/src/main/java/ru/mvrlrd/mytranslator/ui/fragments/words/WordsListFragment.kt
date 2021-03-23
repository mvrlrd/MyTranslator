package ru.mvrlrd.mytranslator.ui.fragments.words

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.words_in_category_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.WordsAdapter


private const val ARG_PARAM1 = "param"
private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "WordsInCategoryFragment"


class WordsListFragment : Fragment() {
    private var param: Long? = null
    private val wordsAdapter: WordsAdapter by inject()
    private val newWord: NewWordDialog by inject()
    private val wordsListViewModel: WordsListViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getLong(ARG_PARAM1)
        }
        param?.let { wordsListViewModel.getAllWordsOfCategory(it) }
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
        wordsListViewModel.categoryId = param!!
        val root = inflater.inflate(R.layout.words_in_category_fragment, container, false)
        root.findViewById<FloatingActionButton>(R.id.gotoAddNewWordFab).setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("id", param!!)
            newWord.arguments = bundle
            newWord.show(parentFragmentManager, "addNewWordDialog")
//            categoriesViewModel.clearCategories()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wordsListViewModel.liveWordList.observe(viewLifecycleOwner, Observer { wordList ->
            param?.let { handleRecycler(wordList) }
        })
    }

    private fun handleRecycler(wordList: List<CardOfWord>) {
        words_recycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = wordsAdapter.apply { collection = wordList as MutableList<CardOfWord> }
        }
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
}