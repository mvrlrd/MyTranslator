package ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.RemoteIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.data.network.response.SearchResultResponse
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCardToDb
import ru.mvrlrd.mytranslator.domain.use_cases.network.GetSearchResult
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class NewWordViewModel (
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {
    private val localRepository = LocalIRepository( dbHelper)
    private val remoteIRepository = RemoteIRepository(apiHelper)
    private val getSearchResult: GetSearchResult = GetSearchResult(remoteIRepository)
    private val inserterCardToDb: InserterCardToDb = InserterCardToDb(localRepository)
    private var _liveTranslations = MutableLiveData<List<MeaningModelForRecycler>>()
    val liveTranslations: LiveData<List<MeaningModelForRecycler>> = _liveTranslations
    private var queryName: String = ""

    fun loadDataFromWeb(word: String) {
        queryName = word
        viewModelScope.launch {
            getSearchResult(word) {
                it.fold(
                    ::handleFailure,
                    ::handleLoadDataFromWeb
                )
            }
        }
    }

    private fun handleLoadDataFromWeb(response: ListSearchResult?) {
        val filteredResponseList: List<SearchResultResponse>? =
            response?.filter { it.text == queryName }
//        Log.e(TAG, "${filteredResponseList?.size}   sizeeeeeeee")
        _liveTranslations.value = filteredResponseList?.map { resp ->
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
                inserterCardToDb(
                    Card(
                        item.id,
                        item.text,
                        item.translation,
                        item.image_url,
                        item.transcription,
                        item.partOfSpeech,
                        item.prefix,
                        0
                    )
                )
                {
                    it.fold(
                        ::handleFailure,
                        ::handleSaveCardToDb
                    )
                }
            }
        }
    }

    private fun handleSaveCardToDb(id: Long) {
        Log.d(TAG, "id#$id was added to the database")
    }

    companion object {
        const val TAG = "TranslationViewModel"
    }

    fun clearLiveTranslationList() {
        _liveTranslations.value = emptyList()
    }
}