package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.categories_fragment.*
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.adapters.LearningAdapter
import ru.mvrlrd.mytranslator.ui.fragments.categories.CategoriesViewModel
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback



private val TAG = "LearningFragment"


class LearningFragment : Fragment(), LearningAdapter.LearningAdapterListener {



    private val learningViewModel : LearningViewModel by inject()
    private lateinit var learningAdapter:LearningAdapter

    private lateinit var callback: ItemTouchHelper.Callback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        learningAdapter = LearningAdapter(this as LearningAdapter.LearningAdapterListener)



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learning, container, false)
    }

    override fun onResume() {
        super.onResume()
        learningViewModel.getCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

learningViewModel.liveLearningCategoriesList.observe(viewLifecycleOwner, Observer {
    cat ->  learningViewModel.getAllWordsOfCategory1(cat)

//    Log.e(TAG,cat.toString())


})

        learningViewModel.liveWordsList.observe(
            viewLifecycleOwner,
            Observer { words ->
                Log.e(TAG,words.toString())
                handleCategoryRecycler(words as MutableList<CardOfWord>)
            })
        //for the first loading
        if (learningViewModel.liveWordsList.value != null) {
            handleCategoryRecycler(learningViewModel.liveWordsList.value!!)
        }
    }

    private fun handleCategoryRecycler(allWords: List<CardOfWord>) {
        learning_recyclerview.apply {
            layoutManager =
                LinearLayoutManager(this.context)
            adapter = learningAdapter.apply { collection = allWords.shuffled() as MutableList<CardOfWord> }
        }




        callback =
            SimpleItemTouchHelperCallback(
                learning_recyclerview.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(learning_recyclerview)
    }


    override fun onItemClick(id: Long) {

    }

    override fun onItemSwiped(categoryId: Long) {

    }

    override fun onItemLongPressed(v: View, category: Category) {

    }
}