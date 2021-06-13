package ru.mvrlrd.mytranslator.service.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any, L : LiveData<T>> LifecycleOwner.observeData(liveData: L, body: (T) -> Unit) =
    liveData.observe(this, Observer(body))