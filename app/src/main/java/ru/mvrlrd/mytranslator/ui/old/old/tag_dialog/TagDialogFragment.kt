package ru.mvrlrd.mytranslator.ui.old.old.tag_dialog


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
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.old.old.recycler_tags.OnItemChecked
import ru.mvrlrd.mytranslator.ui.old.old.recycler_tags.TagsAdapter


private const val TAG = "TagFragment"

class TagDialogFragment : DialogFragment(), OnItemChecked {

    override var _checkedList: MutableList<Category> = mutableListOf()
    private val tagAdapter = TagsAdapter(this as OnItemChecked)
    private val tagDialogViewModel: TagDialogViewModel by inject()

    private var allTagsForCurrentCard: MutableList<Category> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root : View = inflater.inflate(R.layout.dialog_fragment_tag, container,true  )


        val bundle = arguments
        val currentCardId = bundle!!.getLong("id", 0)
//        Log.e(TAG,"current card id    ${arguments}")
//
//        Log.e(TAG,"current card id    ${currentCardId.toString()}")


        val cancelButton: Button = root.findViewById(R.id.cancel_button)
        val okButton: Button = root.findViewById(R.id.ok_button)

//        tagDialogViewModel.getAllTagsForCurrentCard(currentCardId)

        tagDialogViewModel.liveTagsOfCurrentCard.observe(viewLifecycleOwner, Observer { tags ->
//            for(t in tags){
//                Log.e(TAG,"$currentCardId    ${t.tag}   hey ho")
//            }
allTagsForCurrentCard = tags as MutableList<Category>
        })


        cancelButton.setOnClickListener {

            dismiss()
        }
        okButton.setOnClickListener {
            saveTag(currentCardId)
            dismiss()
            //придумать как сохранять в чекд лист позиции с галочками
        }
        return root
    }

     private fun saveTag(cardId: Long) {
         for (tag in tagDialogViewModel.liveAllTagList.value!!) {
             Log.e(TAG,"what about check  ${tag.name}  ${tag.name}")
//             if (tag.isChecked) {tagDialogViewModel.addTagToCurrentCard(cardId, tag.tagId)}
//             if ((!tag.isChecked)&&(allTagsForCurrentCard.contains(tag))){
//                 tagDialogViewModel.deleteTagFromCard(cardId,tag.tagId)
//             }

         }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tagDialogViewModel.liveAllTagList.observe(viewLifecycleOwner, Observer { tags ->
            for (tag in tags) {
//                tag.isChecked = allTagsForCurrentCard.contains(tag)
            }
            handleTagList(tags as MutableList<Category>)
        })
        //for the first loading
        if (tagDialogViewModel.liveAllTagList.value != null) {
            handleTagList(tagDialogViewModel.liveAllTagList.value!!)
        }
    }

    private fun handleTagList(allTags: List<Category>) {
        tags_recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = tagAdapter.apply { allTagList = allTags as MutableList<Category> }
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