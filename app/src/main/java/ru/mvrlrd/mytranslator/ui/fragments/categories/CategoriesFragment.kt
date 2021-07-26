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
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.*
import kotlinx.android.synthetic.main.categories_fragment.*
import kotlinx.android.synthetic.main.item_category.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.MyItemKeyProvider
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.attachCallbackToRecycler
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewCategoryDialog
import ru.mvrlrd.mytranslator.ui.fragments.initRecycler
import ru.mvrlrd.mytranslator.ui.fragments.keepDistanceBtwHeaderAndRecyclerItemsWhileScrolling

private const val NEW_CATEGORY_DIALOG_REQUEST_CODE = 1
private const val EDIT_CATEGORY_DIALOG_REQUEST_CODE = 111
private const val JSON_STRING_CATEGORY_FROM_DIALOG = "stringArray"
private const val TAG = "CategoryFragment"

class CategoriesFragment : Fragment(), CategoriesAdapter.CategoriesAdapterListener {
    private val categoriesViewModel: CategoriesViewModel by inject()
    private val newCategoryDialog: NewCategoryDialog by inject()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val vibrator: Vibrator by inject()
    var tracker : SelectionTracker<Long>? = null

    var mScrollY = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.categories_fragment, container, false)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                categoriesAdapter.clearSelection()
                Log.e(TAG,"BACK PRESSED")

            }
        }
            )

        //        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            Log.e(TAG,"back back back")
//            // Handle the back button event
//        }
        initAddNewCategoryButton(root)
        initSaveSelectionButton(root)
        categoriesAdapter = CategoriesAdapter(this as CategoriesAdapter.CategoriesAdapterListener)






        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        handleRecycler()

//        Log.e(TAG,"asveeeeeeeeeds ${savedInstanceState?.getBundle("mySelection")}")


//        savedInstanceState?.getLongArray("mySelection")



            tracker = SelectionTracker.Builder<Long>(
                "mySelection",
                categories_recyclerview,
                MyItemKeyProvider(categories_recyclerview),
//            StableIdKeyProvider(categories_recyclerview),
                MyItemDetailsLookup(categories_recyclerview),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            )
                .build()


        savedInstanceState?.let {
            Log.e(TAG,"myBundle $it")
            tracker?.onRestoreInstanceState(it)
            Log.e(TAG,"tracker ${tracker?.selection}")
        }





        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val items = tracker?.selection!!
                button_save_categories_selection.visibility = View.VISIBLE
                if (items.size()==1){
                    categoriesAdapter.catchUp()
                }



//как сохранять селекшн в базу чтобы не терять селекшн при выходе из приложения или переворачивании экрана
//                if (items.size() > 1) {
//                categoriesViewModel.selectUnselectCategory(arrayOf("150", true.toString()))
//                    Log.e(TAG,"there are 2 items chosen $items")
//                }
            }
        })
        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionRefresh() {
                super.onSelectionRefresh()
            }
        })
        observeCategoryListChanges()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }

    private fun initSaveSelectionButton(root: View){
        val saveSelectionButton: Button =
            root.findViewById(R.id.button_save_categories_selection)
        saveSelectionButton.setOnClickListener {
            println("====================================================")

            val selectedCategories =  tracker?.selection


            selectedCategories?.forEach {
                categoriesViewModel.selectionList.add(it)
//                categoriesViewModel.selectUnselectCategory(arrayOf(it.toString(),"true"))
            }
            categoriesViewModel.unselectAllCategories()
            println("___________________________________________")
        }
    }



    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        categoriesViewModel.refreshCategoriesList()
    }

    //update category in Bd by clicking category item, because its isChecked parameter was changed
    override fun onItemClick(id: Long, isChecked: Boolean) {
//        openDialogToEditCurrentCategory(category)
//        Log.e(TAG, "______________________________________ ${categoriesAdapter.getSelected()}")
//        categoriesViewModel.selectUnselectCategory(arrayOf(id.toString(), isChecked.toString()))
        val action = CategoriesFragmentDirections.actionNavigationCategoriesToWordsListFragment(id)
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSwiped(categoryId: Long) {
        categoriesViewModel.deleteCategory(categoryId)
        vibrate(vibrator)
        if (mScrollY>=category_icon_image_view.height){
            mScrollY-=category_icon_image_view.height
        }else if(mScrollY>0){
            mScrollY-=mScrollY
        }
    }

    override fun onItemLongPressed(id: Long) {

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
        this.context?.let { initRecycler(categories_recyclerview, it,categoriesAdapter) }
        attachCallbackToRecycler(categories_recyclerview)
        keepDistanceBtwHeaderAndRecyclerItemsWhileScrolling(categories_recyclerview,addNewCatLayout)
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

    private fun initAddNewCategoryButton(root: View){
        val goToDialogToAddNewCategoryButton: Button =
            root.findViewById(R.id.go_to_dialog_to_add_new_category_button)
        goToDialogToAddNewCategoryButton.setOnClickListener {
            openDialogToAddNewCategory()
        }
    }

    private fun observeCategoryListChanges() {
        categoriesViewModel.catsLive.observe(viewLifecycleOwner, Observer { categoryList ->
            categoriesAdapter.updateCollection(categoryList)


            categoriesAdapter.tracker = tracker
        })
    }
}