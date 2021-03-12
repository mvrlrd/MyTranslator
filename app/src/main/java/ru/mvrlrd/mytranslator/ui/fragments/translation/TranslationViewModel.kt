package ru.mvrlrd.mytranslator.ui.fragments.translation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.use_cases.GetSearchResult
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.domain.use_cases.SaveCardToFavorites
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class TranslationViewModel
    (
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper,dbHelper)
    private val getSearch: GetSearchResult = GetSearchResult(searchResultRepository)
    private val cardSaver : SaveCardToFavorites = SaveCardToFavorites(searchResultRepository)

    private var _liveTranslationsList = MutableLiveData<List<MeaningModelForRecycler>>()
    val liveTranslationsList: LiveData<List<MeaningModelForRecycler>> = _liveTranslationsList


    fun loadData(word: String) {
        viewModelScope.launch {
            getSearch(word) {
                it.fold(
                    ::handleFailure,
                    ::handleTranslations
                )
            }
        }
    }


    private fun handleTranslations(response: ListSearchResult?) {
//        response?.printAllSearchResultResponse()
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
           meaningModelForRecycler.let { item ->
               cardSaver(CardOfWord(
                   item.id,
                   item.text,
                   item.translation,
                   item.image_url,
                   item.transcription,
                   item.partOfSpeech,
                   item.prefix
               ))
           }
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}