package ru.mvrlrd.mytranslator.ui.fragments.categories


import android.os.Bundle
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

class CategoriesFragment : Fragment() {

    private val categoriesViewModel : CategoriesViewModel by inject()
    private val addCategoryFragment : AddingCategoryFragment by inject()
    private val catAdapter: CategoriesAdapter by inject()

    companion object {

    }

    private lateinit var viewModel: CategoriesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.categories_fragment, container, false)
         val but : FloatingActionButton = root.findViewById(R.id.addNewCategoryFloatingActionButton)
        but.setOnClickListener {
            addNewCategory()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.liveAllCategoriesList.observe(viewLifecycleOwner, Observer { tags ->
            handleCategoryRecycler(tags as MutableList<Category>)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }



     private fun addNewCategory(){

         val transaction = activity?.supportFragmentManager?.beginTransaction()
         transaction?.replace(R.id.fragment_container_add_category_view, addCategoryFragment)
         transaction?.disallowAddToBackStack()
         transaction?.commit()
    }

}