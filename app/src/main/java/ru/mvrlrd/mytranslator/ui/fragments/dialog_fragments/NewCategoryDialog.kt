package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.adding_category_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter

private const val TAG = "NewCategoryDialog"
private const val JSON_STRING_CATEGORY_FROM_DIALOG = "stringArray"

class NewCategoryDialog : DialogFragment(), IconsAdapter.IconAdapterListener {

    private val iconsAdapter : IconsAdapter  by inject { parametersOf(this)}
    private var iconId: String =""
    private var lastRequest: ()-> Unit = {}
    private var currentId = "0"
    private var currentTitle = ""
    private var currentIcon = ""
    private var isChecked =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
        if (arguments != null) {
            val mArgs = arguments
            val myDay= mArgs?.getString("current state")
            if (myDay != null) {
                Log.e(TAG, myDay)
                val arr = myDay.split(",")
                currentId = arr[0]
                currentTitle = arr[1]
                currentIcon = arr[2]
                isChecked = arr[3]
            }
            arguments?.clear()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.adding_category_fragment, container, false)

//        val addNewButton: FloatingActionButton = root.findViewById(R.id.addNewCategoryFab)
        val nameTextField: TextInputEditText = root.findViewById(R.id.newCategoryEditText)
//        addNewButton.isClickable = true
        val commitAddingButton = root.findViewById<Button>(R.id.commitAddingNewCategoryButton)
        commitAddingButton.setOnClickListener {
            val name = nameTextField.text.toString()
            var message = ""
            when {
                name.isBlank() -> {
                    message = emptyName()
                }
                iconId.isEmpty() -> {
                    message = emptyIcon()
                }
            }
            if (message.isNotBlank()) {
                showSnackBar(message, lastRequest)
            } else {
                nameTextField.text?.clear()
                sendResult(currentId, name, iconId, isChecked)
                currentId="0"

//                iconId = ""
                dismiss()
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRecycler()
    }

    private fun handleRecycler() {
        icons_of_categories_recyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = iconsAdapter

        }
//        icons_of_categories_recyclerview.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
////                if (dy > 0) addNewCategoryFab.hide()
////                else if (dy < 0) addNewCategoryFab.show()
////            }
//        })
    }

    private fun emptyName(): String{
        lastRequest = {pri()}
        return "title shouldn't be empty"
    }

    private fun pri(){
        Log.e(TAG, "There is no title. please insert the title of the category")
    }

    private fun emptyIcon(): String{
        lastRequest = {emptyIcon()}
        return "icon shouldn't be empty"
    }

    override fun onResume() {
        super.onResume()
        if (targetRequestCode == 111) {
            newCategoryEditText.setText(currentTitle)
            Log.e(TAG,"on resume $iconId")
        }
    }

    private fun showSnackBar(message: String, action: () -> Unit){
        Snackbar.make(
            this.place_for_snack_dialog_fragment,
            message,
            Snackbar.LENGTH_LONG
        ).setAction("Reload") { action() }.show()
    }

    private fun sendResult(catId: String, name: String, iconId: String, isChecked : String) {
        targetFragment ?: return
        val intent = Intent().putExtra(JSON_STRING_CATEGORY_FROM_DIALOG, arrayOf(catId, name,iconId, isChecked))
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun onIconClicked(view: View, id: Int) {
        iconId = if (iconId == id.toString()){
            ""
        }else{
            id.toString()
        }
    }
}