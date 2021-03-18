package ru.mvrlrd.mytranslator.ui.fragments.categories


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.categories_fragment.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.AddingCategoryFragment
import ru.mvrlrd.mytranslator.ui.fragments.categories.recycler.CategoriesAdapter

private const val TARGET_FRAGMENT_REQUEST_CODE = 1
private const val EXTRA_GREETING_MESSAGE = "message"
private const val TAG = "CaategoryFragment"

class CategoriesFragment : Fragment() {

    private val categoriesViewModel : CategoriesViewModel by inject()
    private val addCategoryFragment : AddingCategoryFragment by inject()
    private val catAdapter: CategoriesAdapter by inject()

    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addCategoryFragment.setTargetFragment(
            this,
            TARGET_FRAGMENT_REQUEST_CODE
        )
        val root = inflater.inflate(R.layout.categories_fragment, container, false)
         val but : FloatingActionButton = root.findViewById(R.id.addNewCategoryFloatingActionButton)
        but.setOnClickListener {
            goToAddingNewCategoryDialogFragment()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.liveAllCategoriesList.observe(viewLifecycleOwner, Observer { categories ->
            handleCategoryRecycler(categories as MutableList<Category>)
        })

        //for the first loading
        if (categoriesViewModel.liveAllCategoriesList.value != null) {
            handleCategoryRecycler(categoriesViewModel.liveAllCategoriesList.value!!)
        }


    }

    private fun handleCategoryRecycler(allCategories: List<Category>) {
        categories_recyclerview.apply {
            layoutManager = GridLayoutManager(this.context,3)
            adapter = catAdapter.apply { collection = allCategories as MutableList<Category> }
        }
    }


    private fun goToAddingNewCategoryDialogFragment(){
         addCategoryFragment.show(parentFragmentManager, "tagDialog")
    }



    override fun onResume() {
        super.onResume()
        Log.e("category","onResume")
        categoriesViewModel.getAllCategories()
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
                categoriesViewModel.addNewCategory(it[0],it[1])

                Log.e(
                    TAG,
                    "resultCode = ${it[0]}   it is a new category in db with icon ${it[1]}"
                )
            }
        }
    }


}