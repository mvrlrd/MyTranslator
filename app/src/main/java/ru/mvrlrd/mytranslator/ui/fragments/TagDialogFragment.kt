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
import ru.mvrlrd.mytranslator.ui.recycler_tags.OnItemChecked
import ru.mvrlrd.mytranslator.ui.recycler_tags.TagsAdapter
import ru.mvrlrd.mytranslator.vm.TranslationViewModel

class TagDialogFragment : DialogFragment(), OnItemChecked {
     override var checkedList: MutableList<GroupTag> = mutableListOf()
    private val tagAdapter = TagsAdapter(this as OnItemChecked)
    private val translationViewModel: TranslationViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : View = inflater.inflate(R.layout.dialog_fragment_tag, container,false  )
        translationViewModel.loadTag("hobbies")
        translationViewModel.loadTag("football")
        translationViewModel.loadTag("verbs")
        translationViewModel.loadTag("music")
        translationViewModel.loadTag("human")
        translationViewModel.loadTag("earth")
        translationViewModel.loadTag("ecology")
        translationViewModel.loadTag("IT")
        translationViewModel.loadTag("kitchen")
        translationViewModel.loadTag("home")
        translationViewModel.loadTag("job")
        translationViewModel.loadTag("extreme")
        translationViewModel.loadTag("ocean")
        translationViewModel.loadTag("mountains")
        translationViewModel.loadTag("hiding")

        translationViewModel.loadTag("hobbies2")
        translationViewModel.loadTag("football2")
        translationViewModel.loadTag("verbs2")
        translationViewModel.loadTag("music2")
        translationViewModel.loadTag("human2")
        translationViewModel.loadTag("earth2")
        translationViewModel.loadTag("ecology2")
        translationViewModel.loadTag("IT2")
        translationViewModel.loadTag("kitchen2")
        translationViewModel.loadTag("home2")
        translationViewModel.loadTag("job2")
        translationViewModel.loadTag("extreme2")
        translationViewModel.loadTag("ocean2")
        translationViewModel.loadTag("mountains2")
        translationViewModel.loadTag("hiding2")
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

    override fun onChecked() {

    }
}