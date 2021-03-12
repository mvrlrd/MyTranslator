package ru.mvrlrd.mytranslator.ui.fragments.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.DeleteCardFromFavorites
import ru.mvrlrd.mytranslator.domain.use_cases.GetAllCardsFromDb
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class FavoritesViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val deleteCardFromFavorites: DeleteCardFromFavorites =
        DeleteCardFromFavorites(searchResultRepository)
    private val getAllCardsFromDb: GetAllCardsFromDb = GetAllCardsFromDb(searchResultRepository)
    var liveHistory: MutableLiveData<List<MeaningModelForRecycler>> = MutableLiveData()

    init {
        loadFavoritesCards()
    }

    private fun loadFavoritesCards() {
        viewModelScope.launch {
            getAllCardsFromDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleFavorites
                )
            }
        }
    }

    private fun handleFavorites(allCardsList: List<CardOfWord>) {
        liveHistory.value = allCardsList.map { card ->
            MeaningModelForRecycler(
                card.id,
                card.text,
                card.translation,
                card.image_url,
                card.transcription,
                card.partOfSpeech,
                card.prefix
            )
        }
    }

    fun deleteCardFromFavorites(meaningModelForRecycler: MeaningModelForRecycler) {
        viewModelScope.launch {
            deleteCardFromFavorites(meaningModelForRecycler.id) {
                it.fold(
                    ::handleFailure,
                    ::handleDeleting
                )
            }
        }
    }

    private fun handleDeleting(quantity: Int) {
        Log.d(TAG, "$quantity item was deleted")
    }

    companion object {
        const val TAG = "FavoritesViewModel"
    }
}