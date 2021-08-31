package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.item_category.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.androidtools.vibrate
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.databinding.FragmentCategoriesBinding
import ru.mvrlrd.mytranslator.ui.fragments.*
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import java.util.*

private const val NEW_CATEGORY_DIALOG_REQUEST_CODE = 1
private const val EDIT_CATEGORY_DIALOG_REQUEST_CODE = 111
//private const val JSON_STRING_CATEGORY_FROM_DIALOG = "stringArray"
private const val TAG = "CategoryFragment"

class CategoriesFragment : Fragment(), CategoriesAdapter.CategoriesAdapterListener {
    private val sharedViewModel: SharedViewModel by sharedViewModel()

    private lateinit var categoriesAdapter: CategoriesAdapter
    private val vibrator: Vibrator by inject()
    var selectionTracker: SelectionTracker<Long>? = null
    var _binding : FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoriesRecyclerView: RecyclerView

    private var mScrollY = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e(TAG, "onCreateView")

        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_categories, container, false)
        val view = binding.root

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    categoriesAdapter.clearSelection()
                    Log.e(TAG, "BACK PRESSED")
                }
            }
        )

        initAddNewCategoryButton(binding)
        initSaveSelectionButton(binding)
        categoriesAdapter = CategoriesAdapter(this as CategoriesAdapter.CategoriesAdapterListener)

        val butt = binding.buttonSaveCategoriesSelection
        butt.visibility = View.VISIBLE
        butt.setOnClickListener {
            Log.e(TAG, "${selectionTracker?.selection}   selectionTracker")
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated   $this")
        categoriesRecyclerView = binding.categoriesRecyclerview
//        categoriesViewModel.refreshCategoriesList()
        handleRecycler(binding)



        selectionTracker = SelectionTracker.Builder<Long>(
            "mySelection",
            categoriesRecyclerView,
            MyItemKeyProvider(categoriesRecyclerView),
//            StableIdKeyProvider(categories_recyclerview),
            MyItemDetailsLookup(categoriesRecyclerView),
            StorageStrategy.createLongStorage()
        )
//                .withSelectionPredicate(
////                SelectionPredicates.createSelectAnything()
//            )
            .build()
        categoriesAdapter.tracker = selectionTracker
        observeCategoryListChanges()

        selectionTracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val items = selectionTracker?.selection!!
                button_save_categories_selection.visibility = View.VISIBLE
                if (items.size()==1){
//                    categoriesAdapter.catchUp()
                }

//
//
//
////как сохранять селекшн в базу чтобы не терять селекшн при выходе из приложения или переворачивании экрана
////                if (items.size() > 1) {
////                categoriesViewModel.selectUnselectCategory(arrayOf("150", true.toString()))
////                    Log.e(TAG,"there are 2 items chosen $items")
////                }
            }
        })
        selectionTracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionRefresh() {
                super.onSelectionRefresh()
            }

            override fun onSelectionRestored() {
                super.onSelectionRestored()
//                Log.e(TAG,"onSelectionRestored ${selectionTracker?.selection}")
//                categoriesAdapter.tracker = selectionTracker
            }
        })

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e(TAG, "onViewStateRestored START  ${Date().time}")
//        Log.e(TAG, "RESTORED  ${selectionTracker?.selection}  ${selectionTracker}}")
        if (savedInstanceState != null) {
//            Log.e(TAG, "onViewStateRestored BEFORE ${selectionTracker?.selection}")
            selectionTracker?.onRestoreInstanceState(savedInstanceState)



//            Log.e(TAG,"onViewStateRestored DONE")

//            selectionTracker?.select(152L)
//            Log.e(TAG, "onViewStateRestored STOP  ${Date().time}")
//            Log.e(TAG, "RESTORED  ${selectionTracker?.selection}  ${selectionTracker}}")


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.e(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        selectionTracker?.onSaveInstanceState(outState)
    }

    private fun initSaveSelectionButton(binding: FragmentCategoriesBinding){
        val saveSelectionButton: Button =
            binding.buttonSaveCategoriesSelection
        saveSelectionButton.setOnClickListener {
            println("====================================================")

            val selectedCategories =  selectionTracker?.selection


            selectedCategories?.forEach {
                sharedViewModel.selectionList.add(it)
//                categoriesViewModel.selectUnselectCategory(arrayOf(it.toString(),"true"))
            }
            sharedViewModel.unselectAllCategories()
            println("___________________________________________")
        }
    }



    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume   ${selectionTracker?.selection}")
        sharedViewModel.refreshCategoriesList()
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause   $this")

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
        sharedViewModel.deleteCategory(categoryId)
        vibrate(vibrator)
        if (mScrollY>=category_icon_image_view.height){
            mScrollY-=category_icon_image_view.height
        }else if(mScrollY>0){
            mScrollY-=mScrollY
        }
    }

    override fun onItemLongPressed(id: Long) {

    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy   $this")
        super.onDestroy()

    }

    override fun editCurrentItem(category: Category) {
        editCurrentCategory(category)
    }




    private fun handleRecycler(binding: FragmentCategoriesBinding) {
        this.context?.let { initRecycler(categoriesRecyclerView, it,categoriesAdapter) }
        attachCallbackToRecycler(categoriesRecyclerView)
        keepDistanceBtwHeaderAndRecyclerItemsWhileScrolling(categoriesRecyclerView,binding.addNewCatLayout)
//        if (categoriesViewModel.liveAllCategories.value!=null){
//            categoriesViewModel.getAllCardsOfCategory(categoriesViewModel.liveAllCategories.value!!)
//        }
        categoriesRecyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )

    }

    private fun addNewCategory() {
        val action = CategoriesFragmentDirections.actionNavigationCategoriesToAddCategoryFragment()
        findNavController().navigate(action)
    }

    private fun editCurrentCategory(currentCategory: Category) {
        val action = CategoriesFragmentDirections.actionNavigationCategoriesToAddCategoryFragment(currentCategory.categoryId)

        findNavController().navigate(action)
    }

    private fun initAddNewCategoryButton(binding: FragmentCategoriesBinding){
        val goToDialogToAddNewCategoryButton: Button =
            binding.goToDialogToAddNewCategoryButton
        goToDialogToAddNewCategoryButton.setOnClickListener {
            addNewCategory()
        }
    }

    private fun observeCategoryListChanges() {
        Log.e(TAG, "observeCategoryListChanges START  ${Date().time}")
        sharedViewModel.catsLive.observe(viewLifecycleOwner, Observer { categoryList ->
            categoriesAdapter.updateCollection(categoryList)
            Log.e(TAG,"LIST DOWNLOADED ${selectionTracker?.selection}  $selectionTracker ")
//            Log.e(TAG,"observeCategoryListChanges DONE")
        })
        Log.e(TAG, "observeCategoryListChanges END  ${Date().time}")
    }
}