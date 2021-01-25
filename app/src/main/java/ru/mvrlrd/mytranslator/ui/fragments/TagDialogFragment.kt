package ru.mvrlrd.mytranslator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_fragment_tag.*
import kotlinx.android.synthetic.main.translation_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.ui.recycler.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.recycler.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.recycler_tags.TagsAdapter
import ru.mvrlrd.mytranslator.vm.TranslationViewModel

class TagDialogFragment : DialogFragment() {
    private val tagAdapter = TagsAdapter()
    private val translationViewModel: TranslationViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : View = inflater.inflate(R.layout.dialog_fragment_tag, container,false  )
        translationViewModel.loadTag("programming")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        translationViewModel.liveTagList.observe(viewLifecycleOwner, Observer { tags ->
            handleTagList(tags as MutableList<GroupTag>)
        })

        if (translationViewModel.liveTagList.value != null) {
            handleTagList(translationViewModel.liveTagList.value!!)
        }
    }

    private fun handleTagList(list: List<GroupTag>) {
        tags_recyclerview.apply {
            layoutManager =  LinearLayoutManager(this.context)
            adapter = tagAdapter.apply { collection = list as MutableList<GroupTag> }
        }
    }
}