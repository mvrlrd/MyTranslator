package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.databinding.FragmentAddCategoryBinding
import ru.mvrlrd.mytranslator.ui.fragments.SharedViewModel
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter

private const val TAG = "AddCategoryFragment"
class AddCategoryFragment : Fragment() {


    private val iconsAdapter: IconsAdapter by inject()
    private var setRandomName: () -> Unit = {}
    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private var confirm: () -> Unit = {}


    private val args: AddCategoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sharedViewModel = viewModel



        if (args.id>0) {
            binding.apply {
                newCategoryEditText.setText(id.toString(), TextView.BufferType.SPANNABLE)
            }
          confirm = {editCategory()}
        }else{
            confirm = {addCategory()}
        }

        initRecycler()



        binding.commitAddingNewCategoryButton.setOnClickListener {
           confirm.invoke()
        }


    }

    private fun editCategory(){
        Log.e(TAG, "${args.id}    editCategory")
//        confirmNewCategory()
    }

    private fun addCategory(){
        Log.e(TAG, "${args.id}    addCategory")
        confirmNewCategory()
    }


    private fun initRecycler() {
        binding.iconsOfCategoriesRecyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = iconsAdapter
        }
    }

    private fun errorEmptyName(): String{
        setRandomName = {setRandomName()}
        return "title shouldn't be empty"
    }

    private fun setRandomName() {
        binding.newCategoryEditText.setText("Haha")
    }

    private fun showSnackBar(message: String, action: () -> Unit){
        Snackbar.make(
            this.binding.placeForSnackDialogFragment,
            message,
            Snackbar.LENGTH_LONG
        ).setAction("RandomTitle") { action() }.show()
    }

    private fun confirmNewCategory(){
        val name = binding.newCategoryEditText.text.toString()
        if (name.isBlank()) {
            showSnackBar(errorEmptyName(), setRandomName)
        } else {
            viewModel.insertCategory(
                Category(
                    name = name,
                    icon = iconsAdapter.icons[iconsAdapter.selectedPosition].drawableId
                )
            )
            findNavController().popBackStack()

        }
    }
}