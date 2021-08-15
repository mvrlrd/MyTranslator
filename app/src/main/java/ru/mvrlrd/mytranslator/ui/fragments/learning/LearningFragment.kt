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
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CardStackAdapter
import ru.mvrlrd.mytranslator.ui.fragments.categories.SharedViewModel

private const val TAG = "LearningFragment"

class LearningFragment : Fragment(), LearningProcess {
    private lateinit var manager: CardStackLayoutManager
    lateinit var csadapter: CardStackAdapter
    private val sharedViewModel: SharedViewModel by sharedViewModel()



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
        sharedViewModel.getChosenCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.liveCategoriesForLearning.observe(viewLifecycleOwner, Observer { cats ->
            sharedViewModel.getAllWordsOfCategory1(cats)
        })

        sharedViewModel.liveCardsOfCategory.observe(
            viewLifecycleOwner,
            Observer { words ->
                Log.e(TAG, words.toString())
                handleCategoryRecycler(words as MutableList<Card>)
            })
        //for the first loading
        if (sharedViewModel.liveCardsOfCategory.value != null) {
            handleCategoryRecycler(sharedViewModel.liveCardsOfCategory.value!!)
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

    override fun updateCard(cardId: Long, newProgress: Int) {
        sharedViewModel.updateCardProgress(cardId, newProgress)
    }


}