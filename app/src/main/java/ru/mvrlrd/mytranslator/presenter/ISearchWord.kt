package ru.mvrlrd.mytranslator.presenter

import ru.mvrlrd.mytranslator.model.Translation

interface ISearchWord{
    //беру слово из EditText
    fun searchWord(): String
    // Вставляю результат поиска в EditText
    fun showResult(s: String)
}