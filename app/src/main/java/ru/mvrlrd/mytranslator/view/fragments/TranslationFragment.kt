package ru.mvrlrd.mytranslator.view.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.translation_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.MainViewModel
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter

class TranslationFragment : Fragment() {

    lateinit var translationAdapter : TranslationAdapter
    private val mainViewModel: MainViewModel by inject()

    companion object {
        fun newInstance() =
            TranslationFragment()
    }

    private lateinit var viewModel: TranslationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        translationAdapter = TranslationAdapter()
      val root = inflater.inflate(R.layout.translation_fragment, container, false)

        initializeView(root.context)

        return inflater.inflate(R.layout.translation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TranslationViewModel::class.java)
        // TODO: Use the ViewModel

        mainViewModel.liveTranslations.observe(viewLifecycleOwner, Observer { meanings ->
            handleTranslationList(meanings)
        })

    }

    private fun handleTranslationList(list: List<MeaningModelForRecycler>) {
        translationAdapter.collection = list
    }


    private fun initializeView(context: Context) {
        translation_recyclerview.apply {
            layoutManager =  LinearLayoutManager(context)
            adapter = translationAdapter
        }

    }

}