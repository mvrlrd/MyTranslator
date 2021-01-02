package ru.mvrlrd.mytranslator.view.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.translation_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.ui.recycler.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.recycler.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter
import ru.mvrlrd.mytranslator.view.fragments.translation.OnSwipeListener


class TranslationFragment : Fragment(), OnSwipeListener {
    private val mainViewModel: MainViewModel by inject()
    private lateinit var callback: ItemTouchHelper.Callback
   private lateinit var _adapter: TranslationAdapter
    private lateinit var vibrator : Vibrator

    companion object {
        fun newInstance() =
            TranslationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.translation_fragment, container, false)

        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        _adapter = TranslationAdapter(this as OnSwipeListener)

        val searchButton: ImageButton = root.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            mainViewModel.loadData(searchedWord_TextView.text.toString())

            mainViewModel.loadHistory()

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.liveTranslations.observe(viewLifecycleOwner, Observer { meanings ->
            handleTranslationList(meanings as MutableList<MeaningModelForRecycler>)
        })


        mainViewModel.liveHistory.observe(viewLifecycleOwner, Observer { history ->
            println("${history.toString()}      my history_______________________")
        })



    }


    private fun handleTranslationList(list: MutableList<MeaningModelForRecycler>) {
        translation_recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = _adapter.apply { collection = list }
        }
        callback =
            SimpleItemTouchHelperCallback(translation_recyclerview.adapter as ItemTouchHelperAdapter)
        ItemTouchHelper(callback).attachToRecyclerView(translation_recyclerview)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(meaningModelForRecycler: MeaningModelForRecycler) {
        mainViewModel.saveCard(meaningModelForRecycler)
        vibrate(vibrator)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun vibrate(vibrator: Vibrator) {
        val effect =
            VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(effect)
        }
    }
}






