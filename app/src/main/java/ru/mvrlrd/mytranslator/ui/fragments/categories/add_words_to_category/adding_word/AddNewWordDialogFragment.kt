package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.adding_word


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.item_word.*
import org.koin.android.ext.android.inject
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.WordsInCategoryViewModel

class AddNewWordDialogFragment : DialogFragment(), IconsAdapter.IconAdapterListener {

//    private val addingWordViewModel: AddingWordViewModel by inject()
private val wordsInCategoryViewModel: WordsInCategoryViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME,android.R.style.Theme_Holo_Light)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.adding_word_fragment, container, false)

        val textField: TextInputEditText= root.findViewById(R.id.newWordEditText)

        root.findViewById<FloatingActionButton>(R.id.addNewWordFab)
            .setOnClickListener {
//            addingWordViewModel.saveWordToDb(CardOfWord(0,textField.text.toString(),
//            "",
//            "",
//            "",
//            "",
//            ""))
                val cardOfWord = CardOfWord(
                    0,
                    textField.text.toString(),
                    "",
                    "haha",
                    "",
                    "",
                    ""
                )
                wordsInCategoryViewModel.saveWordToDb(cardOfWord)
//                sendResult(cardOfWord.toStringArray())

                dismiss()
        }

            return root
    }

    private fun sendResult(arr: Array<String?>) {
        targetFragment ?: return
        val intent = Intent().putExtra("message", arr)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }


    override fun onIconClicked(id: Int) {
        TODO("Not yet implemented")
    }

}