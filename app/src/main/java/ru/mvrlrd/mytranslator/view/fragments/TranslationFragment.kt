package ru.mvrlrd.mytranslator.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.translation_fragment.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.ui.recycler.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.recycler.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter


class TranslationFragment : Fragment() {
    private val mainViewModel: MainViewModel by inject()
    private lateinit var callback: ItemTouchHelper.Callback
    private val _adapter: TranslationAdapter = get()

    companion object {
        fun newInstance() =
            TranslationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.translation_fragment, container, false)
        val searchButton: ImageButton = root.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            mainViewModel.loadData(searchedWord_TextView.text.toString())
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.liveTranslations.observe(viewLifecycleOwner, Observer { meanings ->
            handleTranslationList(meanings as MutableList<MeaningModelForRecycler>)
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
}






