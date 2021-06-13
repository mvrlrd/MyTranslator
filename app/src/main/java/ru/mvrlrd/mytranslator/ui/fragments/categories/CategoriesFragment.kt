package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.categories_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewCategoryDialog
import ru.mvrlrd.mytranslator.ui.old.old.ItemTouchHelperAdapter
import ru.mvrlrd.mytranslator.ui.old.old.SimpleItemTouchHelperCallback
import kotlin.math.max
import kotlin.math.min

private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val CHOOSE_FILE_REQUEST_CODE = 111
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "CategoryFragment"

class CategoriesFragment : Fragment(), CategoriesAdapter.CategoriesAdapterListener {

    private val categoriesViewModel: CategoriesViewModel by inject()
    private val newCategoryDialog: NewCategoryDialog by inject()
    private lateinit var catAdapter: CategoriesAdapter
    private val vibrator: Vibrator by inject()
    var editableId = -1L
    private lateinit var callback: ItemTouchHelper.Callback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.categories_fragment, container, false)
        val btn: Button = root.findViewById(R.id.addNewCatButton)
        btn.setOnClickListener {
            openDialogToAddNew()
        }
        catAdapter = CategoriesAdapter(this as CategoriesAdapter.CategoriesAdapterListener)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesViewModel.liveAllCategories.observe(
            viewLifecycleOwner,
            Observer { categories ->
                handleCategoryRecycler(categories as MutableList<Category>)
            })
        //for the first loading
        if (categoriesViewModel.liveAllCategories.value != null) {
            handleCategoryRecycler(categoriesViewModel.liveAllCategories.value!!)
        }
    }

    private fun handleCategoryRecycler(allCategories: List<Category>) {
        categories_recyclerview.apply {
            layoutManager =
                LinearLayoutManager(this.context)
//                GridLayoutManager(this.context, 3)
            adapter = catAdapter.apply { collection = allCategories as MutableList<Category> }
        }.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        callback =
            SimpleItemTouchHelperCallback(
                categories_recyclerview.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(categories_recyclerview)
        var mScrollY = 0F
        categories_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rcv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rcv, dx, dy)
                mScrollY += dy.toFloat()
                mScrollY = max(mScrollY, 0F)
                addNewCatLayout.translationY = min(-mScrollY, 0F)
            }
        })
    }

    private fun openDialogToAddNew() {
        newCategoryDialog.setTargetFragment(this, TARGET_FRAGMENT_REQUEST_CODE)
        newCategoryDialog.show(parentFragmentManager, "tagDialog")
    }

    private fun openDialogToEditCurrent(currentCategory: Category) {
        editableId = currentCategory.categoryId
        val bundle = Bundle()
        bundle.putString("current state", currentCategory.toString())
        newCategoryDialog.arguments = bundle
        newCategoryDialog.setTargetFragment(this, CHOOSE_FILE_REQUEST_CODE)
        newCategoryDialog.show(parentFragmentManager, "tagDialog")
    }

    override fun onResume() {
        super.onResume()
        Log.e("category", "onResume")
        categoriesViewModel.refreshCategoriesScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            Log.e(TAG, "resultCode = $requestCode doesn't equal to Activity.Result_OK")
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            data?.getStringArrayExtra(EXTRA_GREETING_MESSAGE)?.let {
                categoriesViewModel.addNewCategory(0, it[0], it[1])
            }
        }
        if (requestCode == CHOOSE_FILE_REQUEST_CODE) {
            data?.getStringArrayExtra(EXTRA_GREETING_MESSAGE)?.let {
                categoriesViewModel.addNewCategory(editableId, it[0], it[1])
            }
        }
    }

    override fun onItemClick(v: View, category: Category) {
//        openDialogToEditCurrent(category)
        categoriesViewModel.updateCategory(category)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(categoryId: Long) {
        categoriesViewModel.deleteCategory(categoryId)
        vibrate(vibrator)
    }

    override fun onItemLongPressed(id: Long) {
        val action = CategoriesFragmentDirections.actionNavigationCategoriesToWordsListFragment(id)
        findNavController().navigate(action)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.e(TAG, "selected  ")
        return super.onOptionsItemSelected(item)
    }
}