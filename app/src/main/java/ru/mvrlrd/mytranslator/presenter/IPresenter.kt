package ru.mvrlrd.mytranslator.presenter

import android.view.View

interface IPresenter<V:View>{
    fun attachView(view: V)

    fun detachView(view: V)

    fun getData(word: String, isOnline: Boolean)
}