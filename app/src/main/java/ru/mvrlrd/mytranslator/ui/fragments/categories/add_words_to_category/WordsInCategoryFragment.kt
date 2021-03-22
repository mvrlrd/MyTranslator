package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.words_in_category_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category

import ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.adding_word.AddNewWordDialogFragment
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.recycler.WordsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "WordsInCategoryFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [WordsInCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordsInCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Long? = null


    private val wordsAdapter : WordsAdapter by inject()
    private val addNewWordFragment : AddNewWordDialogFragment by inject()
    private val wordsInCategoryViewModel: WordsInCategoryViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)

        }
        param1?.let { wordsInCategoryViewModel.getAllWordsOfCategory(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        addNewWordFragment.setTargetFragment(
            this,
          TARGET_FRAGMENT_REQUEST_CODE
        )
wordsInCategoryViewModel.categoryId = param1!!
        val root = inflater.inflate(R.layout.words_in_category_fragment, container, false)

        root.findViewById<FloatingActionButton>(R.id.gotoAddNewWordFab).setOnClickListener {
            val bundle = Bundle()
        bundle.putLong("id", param1!!)
        addNewWordFragment.arguments = bundle
            addNewWordFragment.show(parentFragmentManager, "addNewWordDialog")

//            categoriesViewModel.clearCategories()
        }





        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        wordsInCategoryViewModel.liveId.observe(viewLifecycleOwner, Observer { wordId ->
//            param1?.let { wordsInCategoryViewModel.saveWordToCategory(it,wordId) }
//        })

        wordsInCategoryViewModel.liveWordList.observe(viewLifecycleOwner, Observer { wordList ->
            param1?.let { handleRecycler(wordList) }
        })



    }

    private fun handleRecycler(wordList: List<CardOfWord>){
        words_recycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = wordsAdapter.apply { collection = wordList as MutableList<CardOfWord> }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WordsInCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Long) =
            WordsInCategoryFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onPause() {
        super.onPause()
        Log.e(
            TAG,
            "onPause"
        )


    }

    override fun onStart() {
        super.onStart()
        Log.e(
            TAG,
            "onStart"
        )
    }

    override fun onResume() {
        super.onResume()
//        handleRecycler(wordsInCategoryViewModel.liveWordList.value)
//        wordsInCategoryViewModel.getAllWordsOfCategory(param1!!)
        Log.e(
            TAG,
            "onResume"
        )
    }

    override fun onStop() {
        super.onStop()
        Log.e(
            TAG,
            "onStop"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(
            TAG,
            "onDestroy"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Log.e(
                TAG,
                "resultCode = $requestCode doesn't equal to Activity.Result_OK"
            )
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            data?.getStringExtra(EXTRA_GREETING_MESSAGE)?.let {
///////////////////получать нужную инфу из диалога где вводятся новые слова(с переводом) сохранять эти слова в базу и привязывать через кроссреф к текущей категории)


                it.let{
                    CardOfWord(
                        0,
                        it,
                        "",
                        "",
                        "",
                        "",
                        ""
                    ).let {
                        wordsInCategoryViewModel.saveWordToDb(it)
                    }
                }



            }
        }
    }


}