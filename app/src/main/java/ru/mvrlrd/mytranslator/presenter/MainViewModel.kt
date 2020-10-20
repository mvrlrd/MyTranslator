package ru.mvrlrd.mytranslator.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper


class MainViewModel(val apiHelper: ApiHelper): ViewModel() {
    var liveTranslations: MutableLiveData<List<SearchResult>> = MutableLiveData()

     fun loadData(word: String) {
        viewModelScope.launch {
            val response = apiHelper.getData(word)
            if (response.isSuccessful
                && response.body() != null
            ) {
                val data = response.body()
                data.let { tr ->
                    liveTranslations.value = tr
                }
            }
        }
    }

//    fun getTranslation(word: String) {
//        val d: Observable<List<SearchResult>> = apiHelper
//            .getData(word)
//        val disposable: Disposable = d
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ translations: List<SearchResult> ->
//                liveTranslations.value = translations[0].meanings?.get(0)?.translation?.translation
//            }, { throwable ->
//                Log.e(
//                    TAG,
//                    "$throwable"
//                )
//            })
//    }

    companion object {
        const val TAG = "MAIN_PRESENTER"
    }
}