package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CardStackAdapter

private val TAG = "LearningFragment"

class LearningFragment : Fragment() {

    private lateinit var manager: CardStackLayoutManager
    lateinit var csadapter: CardStackAdapter
    private val learningViewModel: LearningViewModel by inject()

    private var pos : Int? = null
    private var swipeDirection: Direction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        csadapter = CardStackAdapter()
        manager = CardStackLayoutManager(this.context,
//            CardStackListener.DEFAULT
            csadapter as CardStackListener)
            manager.setCanScrollVertical(false)
            manager.setSwipeThreshold(0.8f)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_learning, container, false)
    }









    override fun onResume() {
        super.onResume()
        learningViewModel.getCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learningViewModel.liveLearningCategoriesList.observe(viewLifecycleOwner, Observer { cat ->
            learningViewModel.getAllWordsOfCategory1(cat)
        })

        learningViewModel.liveWordsList.observe(
            viewLifecycleOwner,
            Observer { words ->
                Log.e(TAG, words.toString())
                handleCategoryRecycler(words as MutableList<CardOfWord>)
            })
        //for the first loading
        if (learningViewModel.liveWordsList.value != null) {
            handleCategoryRecycler(learningViewModel.liveWordsList.value!!)
        }
    }

    private fun handleCategoryRecycler(allWords: List<CardOfWord>) {
        card_stack_view.apply {
            layoutManager = manager
            adapter =
                csadapter.apply { collection = allWords.shuffled() as MutableList<CardOfWord> }
        }
    }
}