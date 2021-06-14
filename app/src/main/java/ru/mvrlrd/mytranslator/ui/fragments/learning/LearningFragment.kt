package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CardStackAdapter

private const val TAG = "LearningFragment"

class LearningFragment : Fragment(), LearningProcess {
    private lateinit var manager: CardStackLayoutManager
    lateinit var csadapter: CardStackAdapter
    private val learningViewModel: LearningViewModel by inject()
    private var pos: Int? = null
    private var swipeDirection: Direction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        csadapter = CardStackAdapter(this as LearningProcess)
        manager = CardStackLayoutManager(
            this.context,
//            CardStackListener.DEFAULT
            csadapter as CardStackListener
        )
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
        learningViewModel.liveCategoriesForLearning.observe(viewLifecycleOwner, Observer { cat ->
            learningViewModel.getAllWordsOfCategory1(cat)
        })

        learningViewModel.liveCardsOfCategory.observe(
            viewLifecycleOwner,
            Observer { words ->
                Log.e(TAG, words.toString())
                handleCategoryRecycler(words as MutableList<Card>)
            })
        //for the first loading
        if (learningViewModel.liveCardsOfCategory.value != null) {
            handleCategoryRecycler(learningViewModel.liveCardsOfCategory.value!!)
        }
    }

    private fun handleCategoryRecycler(allWords: List<Card>) {
        card_stack_view.apply {
            layoutManager = manager
            adapter =
                csadapter.apply { collection = allWords.shuffled() as MutableList<Card> }
        }
    }

    override fun finishLearningProcess() {
        val action = LearningFragmentDirections.actionNavigationLearningToNavigationCategories()
        findNavController().navigate(action)
    }

    override fun updateCard(card: Card) {
        learningViewModel.updateCardInDb(card)
    }
}