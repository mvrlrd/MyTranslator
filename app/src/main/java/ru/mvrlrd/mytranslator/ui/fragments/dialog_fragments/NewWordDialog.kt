package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter
import java.lang.StringBuilder

class NewWordDialog : DialogFragment(), IconsAdapter.IconAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.adding_word_fragment, container, false)
        val textField: TextInputEditText = root.findViewById(R.id.newWordEditText)
        root.findViewById<FloatingActionButton>(R.id.addNewWordFab)
            .setOnClickListener {
                val str = StringBuilder()
                str.append(textField.text.toString())
                textField.text?.clear()
                sendResult(str.toString())
                dismiss()
            }
        return root
    }

    private fun sendResult(str: String) {
        targetFragment ?: return
        val intent = Intent().putExtra("message", str)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }


    override fun onIconClicked(id: Int) {
        TODO("Not yet implemented")
    }
}