package ru.mvrlrd.mytranslator.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mvrlrd.mytranslator.R


class TranslationFragment : Fragment() {

    companion object {
        fun newInstance() =
            TranslationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.translation_fragment, container, false)
    }
}






