package ru.mvrlrd.mytranslator.ui.fragments.tag_dialog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_fragment_tag.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.ui.recycler_tags.OnItemChecked
import ru.mvrlrd.mytranslator.ui.recycler_tags.TagsAdapter


private const val TAG = "TagFragment"

class TagDialogFragment : DialogFragment(), OnItemChecked {


     override var _checkedList: MutableList<GroupTag> = mutableListOf()
    private val tagAdapter = TagsAdapter(this as OnItemChecked)
    private val tagDialogViewModel: TagDialogViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : View = inflater.inflate(R.layout.dialog_fragment_tag, container,true  )
        tagDialogViewModel.loadTagToDataBase("hobbies")
        tagDialogViewModel.loadTagToDataBase("football")
        tagDialogViewModel.loadTagToDataBase("verbs")
        tagDialogViewModel.loadTagToDataBase("music")
        tagDialogViewModel.loadTagToDataBase("human")
        tagDialogViewModel.loadTagToDataBase("earth")
        tagDialogViewModel.loadTagToDataBase("ecology")
        tagDialogViewModel.loadTagToDataBase("IT")
        tagDialogViewModel.loadTagToDataBase("kitchen")

        val bundle = arguments
        val currentCardId = bundle!!.getLong("id", 0)
        Log.e(TAG,"current card id    ${arguments}")

        Log.e(TAG,"current card id    ${currentCardId.toString()}")


        val cancelButton: Button = root.findViewById(R.id.cancel_button)
        val okButton: Button = root.findViewById(R.id.ok_button)

        cancelButton.setOnClickListener {
            dismiss()
        }
        okButton.setOnClickListener {
            //придумать как сохранять в чекд лист позиции с галочками
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        tagDialogViewModel.liveAllTagList.observe(viewLifecycleOwner, Observer { tags ->
            handleTagList(tags as MutableList<GroupTag>)
        })

        if (tagDialogViewModel.liveAllTagList.value != null) {
            handleTagList(tagDialogViewModel.liveAllTagList.value!!)
        }
    }

    private fun handleTagList(list: List<GroupTag>) {
        tags_recyclerview.apply {
            layoutManager =  LinearLayoutManager(this.context)
            adapter = tagAdapter.apply { collection = list as MutableList<GroupTag> }
        }
    }

    override fun fillCheckedList() {
//        tagDialogViewModel.checkedList.addAll(_checkedList)
    }

//    private fun sendResult(message: String) {
//        targetFragment ?: return
//        val intent = Intent().putExtra("message", message)
//        fillCheckedList()
//        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
////        searchingByIngredients_EditText.text?.clear()
////        println(checkedList.toString())
//        Log.e(TAG,translationViewModel.toString())
//
//        dismiss()
//    }


}