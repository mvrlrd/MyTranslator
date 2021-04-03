package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CardStackAdapter


private val TAG = "LearningFragment"

class LearningFragment : Fragment(), CardStackListener {

    private lateinit var manager: CardStackLayoutManager
    lateinit var csadapter: CardStackAdapter
    private val learningViewModel: LearningViewModel by inject()
    private var swipeDirection: Direction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = CardStackLayoutManager(this.context,
//            CardStackListener.DEFAULT
            this)
            manager.setCanScrollVertical(false)
            manager.setSwipeThreshold(0.8f)
        csadapter = CardStackAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_learning, container, false)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

        Log.e(TAG, "onCardDragging")
    }

    override fun onCardSwiped(direction: Direction?) {
        swipeDirection = direction
        when(swipeDirection){
            Direction.Left -> {Log.e(TAG, "onCardSwiped to the left")}
            Direction.Right -> {Log.e(TAG, "onCardSwiped to the right")}
            else -> {Log.e(TAG, "onCardSwiped up or down")}
        }

    }

    override fun onCardRewound() {
        Log.e(TAG, "onCardRewound")
    }

    override fun onCardCanceled() {
        Log.e(TAG, "onCardCanceled")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        Log.e(TAG, "onCardAppeared   ${csadapter.collection[position].text}")
    }

    override fun onCardDisappeared(view: View?, position: Int) {

        Log.e(TAG, "onCardDisappeared ${csadapter.collection[position].text}")
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