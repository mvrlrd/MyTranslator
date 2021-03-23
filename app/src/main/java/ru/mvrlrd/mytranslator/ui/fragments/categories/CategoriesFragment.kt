package ru.mvrlrd.mytranslator.ui.fragments.categories


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.categories_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewCategoryDialog
import ru.mvrlrd.mytranslator.ui.fragments.words.WordsListFragment


private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "CategoryFragment"

class CategoriesFragment : Fragment(), OnItemClickListener {

    private val categoriesViewModel : CategoriesViewModel by inject()
    private val newCategoryDialog : NewCategoryDialog by inject()
    private val catAdapter: CategoriesAdapter by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        newCategoryDialog.setTargetFragment(
            this,
            TARGET_FRAGMENT_REQUEST_CODE
        )
        val root = inflater.inflate(R.layout.categories_fragment, container, false)
        val but: FloatingActionButton = root.findViewById(R.id.gotoAddingCategoryFragmentFab)
        but.setOnClickListener {
            goToAddingNewCategoryDialogFragment()
//            categoriesViewModel.clearCategories()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesViewModel.liveAllCategoriesList.observe(
            viewLifecycleOwner,
            Observer { categories ->
                handleCategoryRecycler(categories as MutableList<Category>)
            })
        //for the first loading
        if (categoriesViewModel.liveAllCategoriesList.value != null) {
            handleCategoryRecycler(categoriesViewModel.liveAllCategoriesList.value!!)
        }
    }

    private fun handleCategoryRecycler(allCategories: List<Category>) {
        categories_recyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = catAdapter.apply { collection = allCategories as MutableList<Category> }
        }
        categories_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) gotoAddingCategoryFragmentFab.hide()
                else if (dy < 0) gotoAddingCategoryFragmentFab.show()
            }
        })
    }

    private fun goToAddingNewCategoryDialogFragment() {
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
            Log.e(
                TAG,
                "resultCode = $requestCode doesn't equal to Activity.Result_OK"
            )
            return
        }
        if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
            data?.getStringArrayExtra(EXTRA_GREETING_MESSAGE)?.let {
                categoriesViewModel.addNewCategory(it[0], it[1])
            }
        }
    }

    override fun onItemClick(categoryId: Long) {
//        categoriesViewModel.deleteCategory(categoryId)
      launchDialogFragment(categoryId)

    }

    private fun launchDialogFragment(categoryId: Long) =
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(
                (requireView().parent as ViewGroup).id, WordsListFragment.newInstance(
                    categoryId
                )
            )
            .addToBackStack(null)
            .commit()
}