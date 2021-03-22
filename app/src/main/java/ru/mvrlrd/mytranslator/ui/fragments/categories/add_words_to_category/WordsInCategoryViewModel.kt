package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.cards.AddererWordToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.GetterCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.SaveCardToFavorites
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private val TAG = "WordsInCategoryViewModel"

class WordsInCategoryViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val getterCardsOfCategory: GetterCardsOfCategory =
        GetterCardsOfCategory(searchResultRepository)
    private val addererWordToCategory: AddererWordToCategory =
        AddererWordToCategory(searchResultRepository)
    private val saveCardToFavorites: SaveCardToFavorites =
        SaveCardToFavorites(searchResultRepository)


    private var _liveWordList = MutableLiveData<List<CardOfWord>>()
    val liveWordList: LiveData<List<CardOfWord>> = _liveWordList
    var categoryId: Long = 0L


    fun saveWordToDb(str : String) {
        viewModelScope.launch {
            saveCardToFavorites(mapStringToCardOfWord(str)) {
                it.fold(
                    ::handleFailure,
                    ::handleAddingWordToDb
                )
            }
        }
    }

    private fun handleAddingWordToDb(wordId: Long) {
        Log.e(TAG, "new word #$wordId has been added to the database")
        saveWordToCategory(categoryId, wordId)
    }

    private fun saveWordToCategory(categoryId: Long, cardId: Long) {
        viewModelScope.launch {
            addererWordToCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleAddingWordToCategory
                )
            }
        }
    }

    private fun handleAddingWordToCategory(wordId: Long) {
        Log.e(TAG, "word #$wordId has been assigned with the category #$categoryId")
        getAllWordsOfCategory(categoryId)
    }


    fun getAllWordsOfCategory(categoryId: Long) {
        viewModelScope.launch {
            getterCardsOfCategory(categoryId) {
                it.fold(
                    ::handleFailure,
                    ::handleGettingAllWords
                )
            }
        }
    }

    private fun handleGettingAllWords(categoryWithWords: CategoryWithWords) {
        _liveWordList.value = categoryWithWords.cards
    }

    private fun mapStringToCardOfWord(nowItIsOnlyTitle: String): CardOfWord {
        return CardOfWord(
            0,
            nowItIsOnlyTitle,
            "",
            "",
            "",
            "",
            ""
        )
    }
}