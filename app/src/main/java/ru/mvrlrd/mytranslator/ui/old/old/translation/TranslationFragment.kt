package ru.mvrlrd.mytranslator.ui.old.old.translation

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.translation_fragment.*
import kotlinx.android.synthetic.main.translation_fragment.view.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.ui.old.old.tag_dialog.TagDialogFragment
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.OnSwipeListener
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.old.old.TranslationAdapter


private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "TranslationFragment"

class TranslationFragment : Fragment(),
    OnSwipeListener {
    private val translationViewModel: TranslationViewModel by inject()
    private lateinit var callback: ItemTouchHelper.Callback
    private lateinit var _adapter: TranslationAdapter
    private val vibrator: Vibrator by inject()
    private val tagDialogFragment: TagDialogFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagDialogFragment.setTargetFragment(
            this,
            TARGET_FRAGMENT_REQUEST_CODE
        )
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
                    translationViewModel.loadDataFromWeb(searchedWord_TextView.text.toString())
                    true
                }
                else -> false
            }
        }

        val searchButton: ImageButton = root.findViewById(R.id.search_button)
        searchButton.setOnClickListener {
            //hide keyboard
            val imm: InputMethodManager? =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

            translationViewModel.loadDataFromWeb(searchedWord_TextView.text.toString())




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
            translationViewModel.loadDataFromWeb("get")
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
        translationViewModel.saveCardToDb(meaningModelForRecycler)
        vibrate(vibrator)
    }



    override fun onItemLongPressed(currentCardId: Long) {
//        val bundle = Bundle()
//        bundle.putLong("id", currentCardId)
//        Log.e("Looooo","current   ${currentCardId}")
//        tagDialogFragment.arguments = bundle
//        tagDialogFragment.show(parentFragmentManager, "tagDialog")
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
////        if (resultCode != Activity.RESULT_OK) {
////            Log.e(
////                "TranslationFragment",
////                "resultCode = $requestCode doesn't equal to Activity.Result_OK"
////            )
////            return
////        }
////        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
////            data?.getStringExtra(EXTRA_GREETING_MESSAGE)?.let {
////                Log.e(TAG,translationViewModel.checkedList.size.toString())
////                for(i in translationViewModel.checkedList){
////                    Log.e(TAG,i.toString())
////                }
////                println("$TAG, $it")
////
////            }
////        }
//    }
}






