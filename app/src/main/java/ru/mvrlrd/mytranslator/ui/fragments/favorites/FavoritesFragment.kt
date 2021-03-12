package ru.mvrlrd.mytranslator.ui.fragments.favorites

import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.translation_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogFragment
import ru.mvrlrd.mytranslator.ui.recycler.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.recycler.OnSwipeListener
import ru.mvrlrd.mytranslator.ui.recycler.SimpleItemTouchHelperCallback
import ru.mvrlrd.mytranslator.ui.recycler.TranslationAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "TranslationFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment(), OnSwipeListener {

    private val tagDialogFragment: TagDialogFragment by inject()

    private val vibrator: Vibrator by inject()
    private lateinit var _adapter: TranslationAdapter
    private val viewModel: FavoritesViewModel by inject()
    private lateinit var callback: ItemTouchHelper.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagDialogFragment.setTargetFragment(this,
            TARGET_FRAGMENT_REQUEST_CODE
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _adapter =
            TranslationAdapter(this as OnSwipeListener)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveHistory.observe(viewLifecycleOwner, Observer { meanings ->
            handleTranslationList(meanings as MutableList<MeaningModelForRecycler>)
        })

    }
    private fun handleTranslationList(list: MutableList<MeaningModelForRecycler>) {
        translation_recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = _adapter.apply { collection = list  as MutableList<MeaningModelForRecycler> }
        }
        callback =
            SimpleItemTouchHelperCallback(
                translation_recyclerview.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(translation_recyclerview)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FavoritesFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(meaningModelForRecycler: MeaningModelForRecycler) {
        viewModel.deleteCardFromFavorites(meaningModelForRecycler)

        vibrate(vibrator)

    }


    override fun onItemLongPressed(currentCardId: Long) {
        val bundle = Bundle()
        bundle.putLong("id", currentCardId)
        Log.e("Looooo","current   ${currentCardId}")
        tagDialogFragment.arguments = bundle
        tagDialogFragment.show(parentFragmentManager, "tagDialog")
    }
}