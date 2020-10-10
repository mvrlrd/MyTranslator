package ru.mvrlrd.mytranslator.presenter

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.mvrlrd.mytranslator.model.Meanings
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper
import ru.mvrlrd.mytranslator.view.MainActivity

class MainPresenter(val mainActivityImpl : MainActivity){
//<V : View>(protected val compositeDisposable: CompositeDisposable = CompositeDisposable()): IPresenter<V>  {
    private val apiHelper: ApiHelper = ApiHelper()
    var translationList : List<SearchResult> = ArrayList()

//    private var currentView: V? = null

     fun getTranslation() {
         val d: Observable<List<SearchResult>> = apiHelper
             .getData(mainActivityImpl.searchWord())
         val disposable: Disposable = d
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({ translations: List<SearchResult> ->
                 translationList = translations
                 // показываю результат в edittext
                     translationList[0].meanings?.get(0)?.translation?.translation.let {
                     if (it != null) {
                         mainActivityImpl.showResult(it)
                     }
                 }
             }, { throwable ->
                 Log.e(
                     TAG,
                     "$throwable"
                 )
             })
    }


    companion object{
    const val TAG = "MAIN_PRESENTER"}

//    override fun attachView(view: V) {
//        if (view != currentView) {
//            currentView = view
//        }
//    }
//
//    override fun detachView(view: V) {
//        compositeDisposable.clear()
//        if (view == currentView) {
//            currentView = null
//        }
//    }
//
//    override fun getData(word: String, isOnline: Boolean) {
//        compositeDisposable.add(
//            interactor.getData(word, isOnline)
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .doOnSubscribe(doOnSubscribe())
//                .subscribeWith(getObserver())
//        )
//    }
}