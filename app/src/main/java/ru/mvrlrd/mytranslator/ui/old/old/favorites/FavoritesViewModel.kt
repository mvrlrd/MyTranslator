package ru.mvrlrd.mytranslator.ui.old.old.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCardsFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCardFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderCardsOfDb
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class FavoritesViewModel(
    localIRepository: LocalIRepository
) : BaseViewModel() {

//    private val localIRepository = LocalIRepository( dbHelper)
    private val removerCardFromDb: RemoverCardFromDb =
        RemoverCardFromDb(localIRepository)
    private val loaderCardsOfDb: LoaderCardsOfDb = LoaderCardsOfDb(localIRepository)
    private val removerAllOfCardsFromDb: RemoverCardsFromDb =
        RemoverCardsFromDb(localIRepository)
    var liveMeaningsForRecycler: MutableLiveData<List<MeaningModelForRecycler>> = MutableLiveData()

    init {
        loadFavoritesCards()
    }

    private fun loadFavoritesCards() {
        viewModelScope.launch {
            loaderCardsOfDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::mapCardForRecycler
                )
            }
        }
    }

    private fun mapCardForRecycler(allCardsList: List<Card>) {
        liveMeaningsForRecycler.value = allCardsList.map { card ->
            MeaningModelForRecycler(
                card.id,
                card.word,
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
            removerCardFromDb(meaningModelForRecycler.id) {
                it.fold(
                    ::handleFailure,
                    ::handleDeleting
                )
            }
        }
    }

    private fun handleDeleting(quantity: Int) {
        Log.d(TAG, "$quantity item was deleted from the database")
    }

    fun clearAllWordsFromDb() {
        viewModelScope.launch {
            removerAllOfCardsFromDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleCleaningWords
                )
            }
        }
    }

    private fun handleCleaningWords(num: Int) {
        liveMeaningsForRecycler.value = mutableListOf()
        Log.e(TAG, "$num items were deleted from words db")
    }

    companion object {
        const val TAG = "FavoritesViewModel"
    }
}