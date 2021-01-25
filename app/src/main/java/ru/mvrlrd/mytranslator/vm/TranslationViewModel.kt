package ru.mvrlrd.mytranslator.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultRepository
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.use_cases.GetSearchResult
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.data.local.HistoryDao
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.HistoryEntity
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class TranslationViewModel
    (
    apiHelper: ApiHelper,
     val historyDao: HistoryDao
) : BaseViewModel() {

    private val searchResultRepository = SearchResultRepository(apiHelper)
    private val getSearch: GetSearchResult = GetSearchResult(searchResultRepository)
    private var _liveTranslationsList = MutableLiveData<List<MeaningModelForRecycler>>()
    val liveTranslationsList: LiveData<List<MeaningModelForRecycler>> = _liveTranslationsList

    var _tagList = MutableLiveData<List<GroupTag>>()
    val liveTagList : LiveData<List<GroupTag>> = _tagList


    var liveHistory : MutableLiveData<List<HistoryEntity>> = MutableLiveData()

    fun loadData(word: String) {
        viewModelScope.launch {
            getSearch(word) { it.fold(::handleFailure, ::handleTranslations) }
        }
    }

    private fun handleTranslations(response: ListSearchResult?) {
        response?.printAllSearchResultResponse()
        _liveTranslationsList.value = response?.map { resp ->
            resp.meanings?.map { meaningsResponse ->
                MeaningModelForRecycler(
                    0,
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
        viewModelScope.launch {
            println(
               " ${historyDao.insert(meaningModelForRecycler.let{item ->
                HistoryEntity(
                    item.id,
                    item.text,
                    item.translation,
                    item.image_url,
                    item.transcription,
                    item.partOfSpeech,
                    item.prefix
                )
            })} " +
                       "added"
            )
        }
//        println("${historyEntity.translation}    saved")
    }

    fun loadHistory() {
        viewModelScope.launch {
            liveHistory.value = historyDao.getAll()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyDao.clear()
            liveHistory.value = emptyList()
        }
    }

    fun loadTag(tagText : String){
        val groupTag = GroupTag(0,tagText, false)

        viewModelScope.launch {
            for (i in historyDao.getAllTags()){
                if (i.equals(groupTag)){
                    _tagList.value = historyDao.getAllTags()
                    cancel()
                }
            }
            historyDao.insertTag(GroupTag(0,tagText, false))
            _tagList.value = historyDao.getAllTags()
        }
    }
//    fun getAllTags(): List<GroupTag>{
//        var listOfTags = emptyList<GroupTag>()
//        viewModelScope.launch {
//            listOfTags = historyDao.getAllTags()
//        }
//        return listOfTags
//    }

    companion object {
        const val TAG = "MainViewModel"
    }
}