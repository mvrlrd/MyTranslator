package ru.mvrlrd.mytranslator.ui.fragments.words

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.words_in_category_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.SharedViewModel
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CardsOfCategoryAdapter
import ru.mvrlrd.mytranslator.ui.fragments.attachCallbackToRecycler

private const val NEW_WORD_DIALOG_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "CardsOfCategoryFragment"
private const val CHOOSE_FILE_REQUEST_CODE = 111

class CardsOfCategoryFragment : Fragment(), OnItemClickListener {

    private lateinit var cardsOfCategoryAdapter: CardsOfCategoryAdapter
    private val newWordDialog: NewWordDialog by inject()
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val vibrator: Vibrator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.get("categoryId")
        id?.let {
            sharedViewModel.getAllWordsOfCategory(it as Long)
            sharedViewModel.categoryId = id as Long
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        newWordDialog.setTargetFragment(
            this,
            NEW_WORD_DIALOG_REQUEST_CODE
        )
        sharedViewModel.liveCards.observe(viewLifecycleOwner, Observer { cards ->
            handleRecycler(cards)
        })
        val root = inflater.inflate(R.layout.words_in_category_fragment, container, false)
        initFloatingButtons(root)
        cardsOfCategoryAdapter = CardsOfCategoryAdapter(this as OnItemClickListener)
        return root
    }


    private fun handleRecycler(cards: List<Card>) {
        initRecycler(cards)
        attachCallbackToRecycler(words_recycler)
        addOnscrollListenerToRecycler()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                NEW_WORD_DIALOG_REQUEST_CODE -> {
                    data?.getStringExtra(EXTRA_GREETING_MESSAGE)?.let {
                        sharedViewModel.saveCardToDb(it)
                    }
                }
                CHOOSE_FILE_REQUEST_CODE -> {
                    val selectedFilename = data?.data //The uri with the location of the file
                    if (selectedFilename != null) {
                        context?.contentResolver?.openInputStream(selectedFilename)
                            ?.bufferedReader()
                            ?.forEachLine {
//                                cardsOfCategoryViewModel.saveCardToDb(mapperAllStringsToOne(it))
                            }
                    }
                }
                else -> {
                    val msg = "Null filename data received!"
                    val toast = Toast.makeText(this.context, msg, Toast.LENGTH_LONG)
                    toast.show()
                }
            }
        } else {
            Log.e(TAG, "resultCode = $requestCode doesn't equal to Activity.Result_OK")
            return
        }
    }

    override fun onItemClick(categoryId: Long) {
        Log.e(TAG, "onShortPressed")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(cardId: Long) {
        sharedViewModel.deleteWordFromCategory(cardId)
        vibrate(vibrator)
    }

    override fun onItemLongPressed(categoryId: Long) {
        Log.e(TAG, "onLongPressed")
    }


    private fun initRecycler(cards: List<Card>){
        words_recycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = cardsOfCategoryAdapter.apply { collection = cards as MutableList<Card> }
        }
    }

    private fun addOnscrollListenerToRecycler(){
        words_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    gotoAddNewWordFab.hide()
                    addListOfWordsFab.hide()
                } else if (dy < 0) {
                    gotoAddNewWordFab.show()
                    addListOfWordsFab.show()
                }
            }
        })
    }

    fun initFloatingButtons(root: View){
        root.findViewById<FloatingActionButton>(R.id.gotoAddNewWordFab).setOnClickListener {
            newWordDialog.show(parentFragmentManager, "addNewWordDialog")
//            categoriesViewModel.clearCategories()
        }
        root.findViewById<FloatingActionButton>(R.id.addListOfWordsFab).setOnClickListener {
            val intent = Intent()
                .setType("text/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select file"), CHOOSE_FILE_REQUEST_CODE)
        }
    }
}