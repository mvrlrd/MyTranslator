package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.data.network.response.SearchResultResponse
import ru.mvrlrd.mytranslator.domain.use_cases.cards.SaveCardToFavorites
import ru.mvrlrd.mytranslator.domain.use_cases.network.GetSearchResult
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel
import java.util.*

class NewWordViewModel (
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val searcherWithApi: GetSearchResult = GetSearchResult(searchResultRepository)
    private val cardSaverToDb: SaveCardToFavorites = SaveCardToFavorites(searchResultRepository)

    private var _liveTranslationsList = MutableLiveData<List<MeaningModelForRecycler>>()
    val liveTranslationsList: LiveData<List<MeaningModelForRecycler>> = _liveTranslationsList

    private var queryName: String =""

    fun loadDataFromWeb(word: String) {
        queryName = word
        viewModelScope.launch {
            searcherWithApi(word) {
                it.fold(
                    ::handleFailure,
                    ::handleLoadingData
                )
            }
        }
    }

    private fun handleLoadingData(response: ListSearchResult?) {
        val filteredResponseList: List<SearchResultResponse>? =
            response?.filter { it.text == queryName }
        Log.e(TAG, "${filteredResponseList?.size}   sizeeeeeeee" )

        _liveTranslationsList.value = filteredResponseList?.map { resp ->

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

    fun saveCardToDb(meaningModelForRecycler: MeaningModelForRecycler) {
        viewModelScope.launch {
            meaningModelForRecycler.let { item ->
                cardSaverToDb(
                    CardOfWord(
                        item.id,
                        item.text,
                        item.translation,
                        item.image_url,
                        item.transcription,
                        item.partOfSpeech,
                        item.prefix
                    )
                )
                {
                    it.fold(
                        ::handleFailure,
                        ::handleSavingCard
                    )
                }
            }
        }
    }

    private fun handleSavingCard(id: Long) {
        Log.d(TAG, "id#$id was added to the database")
    }

    companion object {
        const val TAG = "TranslationViewModel"
    }

    fun clearLiveTranslationList(){
        _liveTranslationsList.value = emptyList()
    }
}