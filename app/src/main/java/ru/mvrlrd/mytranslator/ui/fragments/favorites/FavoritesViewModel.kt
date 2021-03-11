package ru.mvrlrd.mytranslator.ui.fragments.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.HistoryDao
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.DeleteCardFromFavorites
import ru.mvrlrd.mytranslator.domain.use_cases.GetSearchResult
import ru.mvrlrd.mytranslator.domain.use_cases.SaveCardToFavorites
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class FavoritesViewModel(
    private val historyDao: HistoryDao,
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper,dbHelper)
    private val deleteCardFromFavorites: DeleteCardFromFavorites = DeleteCardFromFavorites(searchResultRepository)

    var liveHistory : MutableLiveData<List<MeaningModelForRecycler>> = MutableLiveData()

    init {
        loadHistory()
    }

//    var liveSearchedInHistory : MutableLiveData<HistoryEntity> = MutableLiveData()


    private fun loadHistory() {
        viewModelScope.launch {
            liveHistory.value = historyDao.getAll().map{ historyEntity ->
                MeaningModelForRecycler(
                    historyEntity.id,
                    historyEntity.text,
                    historyEntity.translation,
                    historyEntity.image_url,
                    historyEntity.transcription,
                    historyEntity.partOfSpeech,
                    historyEntity.prefix
                )
            }
        }
    }


//    fun saveData() {
//        viewModelScope.launch {
//            getSearch(word) { it.fold(::handleFailure, ::handleRandomRecipes) }
//        }
//    }

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

//    private fun handleRandomRecipes(response: ListSearchResult?) {
//        response?.printAllSearchResultResponse()
//        liveTranslations.value = response?.map { resp ->
//            resp.meanings?.map { meaningsResponse ->
//                MeaningModelForRecycler(
//                    resp.text,
//                    meaningsResponse.translationResponse?.translation,
//                    meaningsResponse.imageUrl,
//                    meaningsResponse.transcription,
//                    meaningsResponse.partOfSpeech,
//                    meaningsResponse.prefix
//                )
//
//            }?.let {
//                WordModelForRecycler(
//                    it
//                )
//            }
//        }?.flatMap { it!!.meanings }
//    }



//    fun findWordInHistory(word: String) {
//        viewModelScope.launch {
//            liveSearchedInHistory.value = historyDao.getCertainWord(word)
//        }
//    }

    fun deleteWord(meaningModelForRecycler: MeaningModelForRecycler){
        viewModelScope.launch {
            println("${deleteCardFromFavorites(meaningModelForRecycler.id)}    deleted")
        }
    }

//    fun deleteWord(meaningModelForRecycler: MeaningModelForRecycler) {
//        viewModelScope.launch {
//            historyDao.delete(meaningModelForRecycler.id)
//            loadHistory()
//        }
//    }

    fun clearHistory() {
        viewModelScope.launch {
            historyDao.clear()
            liveHistory.value = emptyList()
        }
    }
}