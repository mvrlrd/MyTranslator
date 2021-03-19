package ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.categories_fragment.*
import kotlinx.android.synthetic.main.fragment_adding_category.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.activities.MainActivity
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler.IconsAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [AddingCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddingCategoryFragment : DialogFragment(), IconsAdapter.IconAdapterListener {

    private val addNewCategoryViewModel: AddNewCategoryViewModel by inject()
    protected val iconsAdapter : IconsAdapter  by inject { parametersOf(this)}
    private var iconId: String =""

    var lastRequest: ()-> Unit = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_FRAME,android.R.style.Theme_Holo_Light)

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
            val name = nameTextField.text.toString()
            var message = ""
            when{
                name.isBlank() -> { message = emptyName() }
                iconId.isEmpty()-> { message = emptyIcon() }
            }
            if (message.isNotBlank()){
                showSnackBar(message, lastRequest )
            }else{
                nameTextField.text?.clear()
                sendResult(name, iconId)
                iconId = ""
                dismiss()
            }



//            clearAllCategories()

        }
        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        icons_of_categories_recyclerview.apply {
            layoutManager = GridLayoutManager(this.context,3)
            adapter = iconsAdapter
        }

    }

    private fun emptyName():String{
        lastRequest = {pri()}

        return "fofo"
    }

    private fun pri(){
        println("____________________________________+++++++++++++++++++++++++++++++++++++++")
    }

      private fun emptyIcon(): String{
          lastRequest = {emptyIcon()}
          return "dodo"
      }

    private fun showSnackBar(message: String, action: () -> Unit){
        Snackbar.make(
            this.place_for_snack_dialog_fragment,
            message,
            Snackbar.LENGTH_LONG
        ).setAction("Reload") { action() }.show()
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

        private fun sendResult(message: String, id: String) {
        targetFragment ?: return
        val intent = Intent().putExtra("message", arrayOf(message,id))
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun onIconClicked(id: Int) {
        Log.e("add category","$id   was chosen ")
        iconId = id.toString()
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