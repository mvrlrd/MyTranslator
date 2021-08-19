package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.databinding.FragmentNewCategoryBinding
import ru.mvrlrd.mytranslator.ui.fragments.SharedViewModel
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter

private const val TAG = "NewCategoryDialog"

class NewCategoryDialog : DialogFragment() {
    private val iconsAdapter: IconsAdapter by inject()
    private var setRandomName: () -> Unit = {}
    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentNewCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentNewCategoryBinding.inflate(inflater, container, false)
        _binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sharedViewModel = viewModel
        initRecycler()
        binding.commitAddingNewCategoryButton.setOnClickListener {
            confirmNewCategory()

        }
    }

    override fun dismiss() {
        super.dismiss()
        binding.newCategoryEditText.text?.clear()
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
            dismiss()
        }
    }
}