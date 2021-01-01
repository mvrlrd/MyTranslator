package ru.mvrlrd.mytranslator.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.translation_fragment.*
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
        val root = inflater.inflate(R.layout.translation_fragment, container, false)
//        val button : Button = root.findViewById(R.id.button)



//        button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_translationFragment_to_favoritesFragment, null))
        return root
    }


}






