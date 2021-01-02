package ru.mvrlrd.mytranslator.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultRepository
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.use_cases.GetSearchResult
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.data.local.HistoryDao
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler


class MainViewModel
    (
    apiHelper: ApiHelper,
    private val historyDao: HistoryDao
) : BaseViewModel() {

    private val searchResultRepository = SearchResultRepository(apiHelper)
    val getSearch: GetSearchResult = GetSearchResult(searchResultRepository)

    var liveTranslations: MutableLiveData<List<MeaningModelForRecycler?>> = MutableLiveData()


    var liveHistory : MutableLiveData<List<HistoryEntity>> = MutableLiveData()
    var liveSearchedInHistory : MutableLiveData<HistoryEntity> = MutableLiveData()

    fun loadData(word: String) {
        viewModelScope.launch {
            getSearch(word) { it.fold(::handleFailure, ::handleRandomRecipes) }
        }
    }

    private fun handleRandomRecipes(response: ListSearchResult?) {
        response?.printAllSearchResultResponse()
        liveTranslations.value = response?.map { resp ->
            resp.meanings?.map { meaningsResponse ->
                MeaningModelForRecycler(
                    resp.text,
                    meaningsResponse.translationResponse?.translation,
                    meaningsResponse.imageUrl,
                    meaningsResponse.transcription,
                    meaningsResponse.partOfSpeech,
                    meaningsResponse.prefix
                )

            }?.let {
                WordModelForRecycler(
                    it
                )
            }
        }?.flatMap { it!!.meanings }
    }

    fun saveCard(meaningModelForRecycler: MeaningModelForRecycler){
        val historyEntity: HistoryEntity = meaningModelForRecycler.let{item ->
            HistoryEntity(
                0,
                item.text,
                item.translation,
                item.image_url,
                item.transcription,
                item.partOfSpeech,
                item.prefix
            )
        }
        viewModelScope.launch {
            historyDao.insert(historyEntity)
        }
        println("${historyEntity.translation}    saved")
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