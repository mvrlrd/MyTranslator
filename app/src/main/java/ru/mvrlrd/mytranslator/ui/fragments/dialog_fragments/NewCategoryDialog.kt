package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.adding_category_fragment.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.ui.fragments.SharedViewModel
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter

private const val TAG = "NewCategoryDialog"

class NewCategoryDialog : DialogFragment(), IconsAdapter.IconAdapterListener {
    private val iconsAdapter : IconsAdapter  by inject { parametersOf(this)}
    private var lastRequest: ()-> Unit = {}
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var nameTextField: TextInputEditText

private var newCategory: Category = Category(name = "",icon = iconsAdapter.icons[iconsAdapter.selectedPosition].drawableId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.adding_category_fragment, container, false)
         nameTextField = root.findViewById(R.id.newCategoryEditText)
        val commitAddingButton = root.findViewById<Button>(R.id.commitAddingNewCategoryButton)

        commitAddingButton.setOnClickListener {
            newCategory.name = nameTextField.text.toString()
            if (newCategory.name.isBlank()){
                    showSnackBar(emptyName(), lastRequest)
                }
            else if(newCategory.icon == 0){
                showSnackBar(emptyIcon(),lastRequest)
            }
            else{
                sharedViewModel.insertCategory(newCategory)
                dismiss()
            }
        }
        return root
    }

    override fun onStop() {
        super.onStop()
//        iconsAdapter.selectedPosition=-1
        //on start надо запихнуть номер позиции иконки которую открыли редактировать/ тогда при редактировании категории сразу будет выбрана старая иконка
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        icons_of_categories_recyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = iconsAdapter
        }
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


    private fun showSnackBar(message: String, action: () -> Unit){
        Snackbar.make(
            this.place_for_snack_dialog_fragment,
            message,
            Snackbar.LENGTH_LONG
        ).setAction("Reload") { action() }.show()
    }

    override fun onIconSelected(view: View, _iconId: Int) {
        newCategory.icon = _iconId
    }

    override fun dismiss() {
        super.dismiss()
        nameTextField.text?.clear()
//        newCategory.icon = iconsAdapter.icons[iconsAdapter.selectedPosition].drawableId
    }
}