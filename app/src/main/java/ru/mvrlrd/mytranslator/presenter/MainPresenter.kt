package ru.mvrlrd.mytranslator.presenter

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.retrofit.ApiHelper

class MainPresenter (val mainActivityImpl : ISearchWord){
    private val apiHelper: ApiHelper = ApiHelper()
    var translationList : List<SearchResult> = ArrayList()


     fun getTranslation() {
         val d: Observable<List<SearchResult>> = apiHelper
             .requestServer(mainActivityImpl.searchWord())
         val disposable: Disposable = d
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({ translations: List<SearchResult> ->
                 translationList = translations
                 // показываю результат в edittext
                 mainActivityImpl.showResult(translationList[0].text.toString())
             }, { throwable ->
                 Log.e(
                     TAG,
                     "$throwable"
                 )
             })
    }

    companion object{
    const val TAG = "MAIN_PRESENTER"}
}