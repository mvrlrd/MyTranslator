package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.CategoryIconItem
import ru.mvrlrd.mytranslator.databinding.FragmentAddCategoryBinding
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter

private const val TAG = "AddCategoryFragment"
class AddCategoryFragment : Fragment() {

    private val iconsAdapter: IconsAdapter by inject()
    private var setRandomName: () -> Unit = {}
    private val viewModel: AddCategoryViewModel by inject()
    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private var confirm: () -> Unit = {}
    private val args: AddCategoryFragmentArgs by navArgs()

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
        binding.viewModel = viewModel
        if (args.categoryId > 0) {
            viewModel.retrieveCategory(args.categoryId)
                .observe(this.viewLifecycleOwner) { editedItem ->
                    iconsAdapter.selectedPosition =
                        iconsAdapter.icons.indexOf(CategoryIconItem(editedItem.icon))
                    binding.apply {
                        newCategoryEditText.setText(editedItem.name, TextView.BufferType.SPANNABLE)
                    }
                }
                confirm = {editCategory()}
            }else{
                confirm = {addCategory()}
            }

        initRecycler()
        binding.commitAddingNewCategoryButton.setOnClickListener {
            if (!binding.newCategoryEditText.text.isNullOrBlank()){
                confirm.invoke()
                findNavController().popBackStack()
            }else{
                showSnackBar(errorEmptyName(), setRandomName)
            }
        }
    }

    private fun editCategory() {
        viewModel.editCategory(
            createCategory(args.categoryId)
        )
    }

    private fun addCategory() {
        viewModel.insertCategory(
            createCategory(0L)
        )
    }

    private fun createCategory(id: Long): Category = Category(
        categoryId = id,
        name = binding.newCategoryEditText.text.toString(),
        icon = iconsAdapter.icons[iconsAdapter.selectedPosition].drawableId
    )

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
}