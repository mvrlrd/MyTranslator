package ru.mvrlrd.mytranslator.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultRepository
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.data.network.response.MeaningsResponse
import ru.mvrlrd.mytranslator.domain.use_cases.GetSearchResult
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.data.local.HistoryDao
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity


class MainViewModel
    (
    apiHelper: ApiHelper,
    val historyDao: HistoryDao
) : BaseViewModel() {

    val searchResultRepository = SearchResultRepository(apiHelper)
    val getSearch: GetSearchResult = GetSearchResult(searchResultRepository)

    var liveTranslations: MutableLiveData<List<MeaningModelForRecycler>> = MutableLiveData()


    var liveHistory : MutableLiveData<List<HistoryEntity>> = MutableLiveData()
    var liveSearchedInHistory : MutableLiveData<HistoryEntity> = MutableLiveData()

    fun loadData(word: String) {
        viewModelScope.launch {
            getSearch(word) { it.fold(::handleFailure, ::handleRandomRecipes) }
        }

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


    private fun handleRandomRecipes(response: ListSearchResult?) {
        liveTranslations.value = response?.map { data ->
            data.meanings?.let { handle2(data.text, it) }
            MeaningModelForRecycler(
                data.text,
                data.meanings?.get(0)?.translationResponse?.translation,
                data.meanings?.get(0)?.imageUrl
            )
        }
    }

    private fun handle2(text: String?, resp: List<MeaningsResponse?>) {
        liveTranslations.value = resp.map { meaning ->
            MeaningModelForRecycler(
                text,
                meaning?.translationResponse?.translation,
                meaning?.imageUrl
            )
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            liveHistory.value = historyDao.getAll()
        }
    }

    fun findWordInHistory(word: String) {
        viewModelScope.launch {
            liveSearchedInHistory.value = historyDao.getCertainWord(word)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyDao.clear()
            liveHistory.value = emptyList()
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}