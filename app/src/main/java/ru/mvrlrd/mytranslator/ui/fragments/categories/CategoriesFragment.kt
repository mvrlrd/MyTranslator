package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

private const val NEW_CATEGORY_DIALOG_REQUEST_CODE = 1
private const val EDIT_CATEGORY_DIALOG_REQUEST_CODE = 111
private const val JSON_STRING_CATEGORY_FROM_DIALOG = "stringArray"
private const val TAG = "CategoryFragment"

class CategoriesFragment : Fragment(), CategoriesAdapter.CategoriesAdapterListener {

    private val categoriesViewModel: CategoriesViewModel by inject()
    private val newCategoryDialog: NewCategoryDialog by inject()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val vibrator: Vibrator by inject()
    private lateinit var callback: ItemTouchHelper.Callback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.categories_fragment, container, false)
        initAddNewCategoryButton(root)
        categoriesAdapter = CategoriesAdapter(this as CategoriesAdapter.CategoriesAdapterListener)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRecycler()
        observeCategoryListChanges()
    }


    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        categoriesViewModel.refreshCategoriesList()
    }

    //update category in Bd by clicking category item, because its isChecked parameter was changed
    override fun onItemClick(id: Long, isChecked: Boolean) {
//        openDialogToEditCurrentCategory(category)
        categoriesViewModel.selectUnselectCategory(arrayOf(id.toString(), isChecked.toString()))
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

    override fun editCurrentItem(category: Category) {
        openDialogToEditCurrentCategory(category)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                NEW_CATEGORY_DIALOG_REQUEST_CODE -> {
                    getStringArrayFromIntent(data)?.let { categoriesViewModel.addCategory(it) }
                }
                EDIT_CATEGORY_DIALOG_REQUEST_CODE -> {
                    getStringArrayFromIntent(data)?.let { categoriesViewModel.updateCategorysNameAndIcon(it) }
                }
            }

        } else {
            Log.e(TAG, "resultCode = $requestCode doesn't equal to Activity.Result_OK")
            return
        }
    }

    private fun getStringArrayFromIntent(data: Intent?) = data?.getStringArrayExtra(JSON_STRING_CATEGORY_FROM_DIALOG)

    private fun handleRecycler() {
        initRecycler()
        attachCallbackToRecycler()
        keepDistanceBtwHeaderAndRecyclerItemsWhileScrolling()
//        if (categoriesViewModel.liveAllCategories.value!=null){
//            categoriesViewModel.getAllCardsOfCategory(categoriesViewModel.liveAllCategories.value!!)
//        }
    }

    private fun openDialogToAddNewCategory() {
        newCategoryDialog.setTargetFragment(this, NEW_CATEGORY_DIALOG_REQUEST_CODE)
        newCategoryDialog.show(parentFragmentManager, "tagDialog")
    }

    private fun openDialogToEditCurrentCategory(currentCategory: Category) {
        val bundle = Bundle()
        bundle.putString("current state", currentCategory.toString())
        newCategoryDialog.arguments = bundle
        newCategoryDialog.setTargetFragment(this, EDIT_CATEGORY_DIALOG_REQUEST_CODE)
        newCategoryDialog.show(parentFragmentManager, "tagDialog")
    }


    private fun initRecycler() =
        categories_recyclerview.apply {
            layoutManager =
                LinearLayoutManager(this.context)
            adapter =
                categoriesAdapter
        }
//            .addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))


    //keep a distance between header("add new category") and the recycler items while scrolling
    private fun keepDistanceBtwHeaderAndRecyclerItemsWhileScrolling() {
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

    private fun attachCallbackToRecycler() {
        callback =
            SimpleItemTouchHelperCallback(
                categories_recyclerview.adapter as ItemTouchHelperAdapter
            )
        ItemTouchHelper(callback).attachToRecyclerView(categories_recyclerview)
    }

    private fun initAddNewCategoryButton(root: View){
        val goToDialogToAddNewCategoryButton: Button =
            root.findViewById(R.id.go_to_dialog_to_add_new_category_button)
        goToDialogToAddNewCategoryButton.setOnClickListener {
            openDialogToAddNewCategory()
        }
    }
    private fun observeCategoryListChanges(){
        categoriesViewModel.liveAllCategories.observe(
            viewLifecycleOwner,
            Observer { categories ->
                categoriesAdapter.updateCollection(categories)
            })
    }
}