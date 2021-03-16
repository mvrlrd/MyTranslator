package ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddingCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddingCategoryFragment : Fragment() {

    private val addNewCategoryViewModel: AddNewCategoryViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_adding_category, container, false)

        val addNewButton: Button = root.findViewById(R.id.addNewCategoryConfirmationButton)
        val nameTextField : TextInputEditText = root.findViewById(R.id.newCategoryEditText)
//        if (nameTextField.text?.length!! >0){
            addNewButton.isClickable = true
//        }


        addNewButton.setOnClickListener {
//            clearAllCategories()
            Log.e("AddCategoryFr", nameTextField.text.toString())
            addNewCategory(nameTextField.text.toString(), "d")
          onBackPressed2()

//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.nav_host_fragment_container, )
//            transaction?.disallowAddToBackStack()
//            transaction?.commit()

        }
        // Inflate the layout for this fragment
        return root
    }
    fun onBackPressed2() {
//activity?.onBackPressed()

    }

    private fun addNewCategory(name: String, picName: String){
        addNewCategoryViewModel.addNewCategory(name, picName)
    }

    private fun clearAllCategories(){
        addNewCategoryViewModel.clearCategories()
    }

    override fun onPause() {
        super.onPause()
        Log.e("add category","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("add category","onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("add category","onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("add category","onDestroy")
    }

    override fun onStart() {
        super.onStart()
        Log.e("add category","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("add category","onResume")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment AddingCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AddingCategoryFragment()
    }
}