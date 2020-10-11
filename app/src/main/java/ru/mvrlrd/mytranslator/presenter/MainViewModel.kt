package ru.mvrlrd.mytranslator.presenter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper
import javax.inject.Inject


class MainViewModel(val apiHelper: ApiHelper) {
    var liveTranslations: MutableLiveData<String> = MutableLiveData()

    fun getTranslation(word: String) {
        val d: Observable<List<SearchResult>> = apiHelper
            .getData(word)
        val disposable: Disposable = d
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ translations: List<SearchResult> ->
                liveTranslations.value = translations[0].meanings?.get(0)?.translation?.translation
            }, { throwable ->
                Log.e(
                    TAG,
                    "$throwable"
                )
            })
    }

    companion object {
        const val TAG = "MAIN_PRESENTER"
    }
}