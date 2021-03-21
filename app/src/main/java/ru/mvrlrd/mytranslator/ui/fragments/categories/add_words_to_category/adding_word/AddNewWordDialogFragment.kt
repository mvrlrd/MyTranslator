package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.adding_word


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler.IconsAdapter

class AddNewWordDialogFragment : DialogFragment(), IconsAdapter.IconAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME,android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.adding_word_fragment, container, false)

        return root
    }


    override fun onIconClicked(id: Int) {
        TODO("Not yet implemented")
    }

}