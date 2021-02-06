package ru.mvrlrd.mytranslator.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.translation_fragment.*
import kotlinx.android.synthetic.main.translation_fragment.view.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.ui.recycler.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.recycler.OnSwipeListener
import ru.mvrlrd.mytranslator.ui.recycler.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter
import ru.mvrlrd.mytranslator.vm.TranslationViewModel


private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"

class TranslationFragment : Fragment(),
    OnSwipeListener {
    private val translationViewModel: TranslationViewModel by inject()
    private lateinit var callback: ItemTouchHelper.Callback
    private lateinit var _adapter: TranslationAdapter
    private val vibrator: Vibrator by inject()
    private val tagDialogFragment: TagDialogFragment by inject()

//    companion object {
//        fun newInstance() =
//            TranslationFragment()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagDialogFragment.setTargetFragment(this, TARGET_FRAGMENT_REQUEST_CODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.translation_fragment, container, false)

        _adapter =
            TranslationAdapter(this as OnSwipeListener)

        root.searchedWord_TextView.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    translationViewModel.loadData(searchedWord_TextView.text.toString())
                    true
                }
                else -> false
            }
        }

//        val cards = listOf(HistoryEntity(1,"fuck","hernya","http1","[ewew]","n","a"),
//            HistoryEntity(2,"loh","kruto","http2","[rrrr]","v","the"))
//        val tags = listOf(GroupTag(1,"series/movies"),
//            GroupTag(2,"programming"),
//            GroupTag(3,"travelling"),
//        GroupTag(4,"games"),
//        GroupTag(5,"sport"))
//
//        val crossRefList =  listOf(
//            CardTagCrossRef(1,2),
//            CardTagCrossRef(1,3),
//            CardTagCrossRef(2,2))
//
//        lifecycleScope.launch {
//            cards.forEach { card ->
//                translationViewModel.historyDao.insert(card)
//            }
//            tags.forEach { tag ->
//                translationViewModel.historyDao.insertTag(tag)
//            }
//            crossRefList.forEach { crossRef ->
//                translationViewModel.historyDao.insertCardTagCrossRef(crossRef)
//            }
//
//
//            println("${translationViewModel.historyDao.getCardsOfTag(1)}+++++++++++++++++++++++++++++++++++++++++")
//        }





        val searchButton: ImageButton = root.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            //hide keyboard
            val imm: InputMethodManager? =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

            translationViewModel.loadData(searchedWord_TextView.text.toString())




        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        translationViewModel.liveTranslationsList.observe(viewLifecycleOwner, Observer { meanings ->
            handleTranslationList(meanings as MutableList<MeaningModelForRecycler>)
        })

        if (translationViewModel.liveTranslationsList.value != null) {
            handleTranslationList(translationViewModel.liveTranslationsList.value!!)
        } else {
            translationViewModel.loadData("get")
        }
    }


    private fun handleTranslationList(list: List<MeaningModelForRecycler>) {
        translation_recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = _adapter.apply { collection = list as MutableList<MeaningModelForRecycler> }
        }
        callback =
            SimpleItemTouchHelperCallback(
                translation_recyclerview.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(translation_recyclerview)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(meaningModelForRecycler: MeaningModelForRecycler) {
        translationViewModel.saveCard(meaningModelForRecycler)
        vibrate(vibrator)
    }

    override fun onItemLongPressed() {
        tagDialogFragment.show(parentFragmentManager, "tagDialog")


        // setup the alert builder
        // setup the alert builder
//        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
//        builder.setTitle("Choose tag")
//// add a checkbox list
//// add a checkbox list
//        val tags = listOf(GroupTag(1,"series/movies"),
//            GroupTag(2,"programming"),
//            GroupTag(3,"travelling"),
//            GroupTag(4,"games"),
//            GroupTag(5,"sport"))
//
//        val tagsStr =
//            arrayOf("series/movies", "programming", "travelling", "games", "sport")
//        val checkedItems = booleanArrayOf(false, false, false, false, false)
//        builder.setMultiChoiceItems(tagsStr, checkedItems,
//            OnMultiChoiceClickListener { dialog, which, isChecked ->
//                // user checked or unchecked a box
//            })
//// add OK and Cancel buttons
//// add OK and Cancel buttons
//        builder.setPositiveButton("OK",
//            DialogInterface.OnClickListener { dialog, which ->
//                // user clicked OK
//            })
//        builder.setNegativeButton("Cancel", null)
//// create and show the alert dialog
//// create and show the alert dialog
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Log.e(
                "TranslationFragment",
                "resultCode = $requestCode doesn't equal to Activity.Result_OK"
            )
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            data?.getStringExtra(EXTRA_GREETING_MESSAGE)?.let {
                println("My Log,$it")
                for(i in translationViewModel.checkedList){
                    println(i.toString())
                }

            }
        }
    }
}






