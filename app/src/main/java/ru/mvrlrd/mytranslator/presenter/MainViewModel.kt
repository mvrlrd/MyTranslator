package ru.mvrlrd.mytranslator.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.SearchResultRepository
import ru.mvrlrd.mytranslator.data.response.SearchResultResponse
import ru.mvrlrd.mytranslator.data.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.response.MeaningsResponse
import ru.mvrlrd.mytranslator.domain.use_cases.GetSearchResult

import ru.mvrlrd.mytranslator.room.HistoryDao
import ru.mvrlrd.mytranslator.room.HistoryEntity


class MainViewModel (
    val apiHelper: ApiHelper ,
    val historyDao: HistoryDao): BaseViewModel() {
lateinit var s:String
    val searchResultRepository = SearchResultRepository(apiHelper)
    val getSearch : GetSearchResult = GetSearchResult(searchResultRepository)

    var liveTranslations: MutableLiveData<List<SearchResultResponse>> = MutableLiveData()
    var liveHistory : MutableLiveData<List<HistoryEntity>> = MutableLiveData()
    var liveSearchedInHistory : MutableLiveData<HistoryEntity> = MutableLiveData()

init {

}
    fun loadData(word: String) {


        viewModelScope.launch {

            println("${getSearch("hello") { it.fold(::handleFailure, ::handleRandomRecipes)}.toString()}    dc  dccdcddcdcc") }

//            val response = apiHelper.getData(word)
//            if (response.isSuccessful
//                && response.body()  != null
//            ) {
//                val data = response.body()
//                data.let { tr ->
//                    liveTranslations.value = tr
//                    println(
//                    historyDao.insert(HistoryEntity( tr?.get(0)?.text,
//                        tr?.get(0)?.meanings?.get(0)?.translation?.translation)).toString()+"     id has been added to db")
//                }
//
    }

    private fun handleRandomRecipes(randomRecipes: SearchResultResponse?) {
println("randomRecipes.toString()")




    }


     fun loadHistory(){
        viewModelScope.launch {
            liveHistory.value = historyDao.getAll()
        }


    }

    fun findWordInHistory(word:String){
        viewModelScope.launch {
            liveSearchedInHistory.value = historyDao.getCertainWord(word)
        }

    }
     fun clearHistory(){
         viewModelScope.launch {
             historyDao.clear()
             liveHistory.value = emptyList()
         }

    }

    companion object {
        const val TAG = "MAIN_PRESENTER"
    }
}