package ru.mvrlrd.mytranslator.ui.fragments.words

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.binders.BinderCardToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCardFromCategory
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCardToDb
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "WordsInCategoryViewModel"

class WordsListViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val loaderCardsOfCategory: LoaderCardsOfCategory =
        LoaderCardsOfCategory(searchResultRepository)
    private val binderCardToCategory: BinderCardToCategory =
        BinderCardToCategory(searchResultRepository)
    private val inserterCardToDb: InserterCardToDb =
        InserterCardToDb(searchResultRepository)
    private val removerCardFromCategory: RemoverCardFromCategory =
        RemoverCardFromCategory(searchResultRepository)
    private var _cards = MutableLiveData<List<Card>>()
    val liveCards: LiveData<List<Card>> = _cards
    var categoryId: Long = 0L

    fun saveWordToDb(str: String) {
        val card = Gson().fromJson(str, Card::class.java)
        if (checkIfWordIsInCategory(card) == false) {
            viewModelScope.launch {
                inserterCardToDb(card) {
                    it.fold(
                        ::handleFailure,
                        ::handleAddingWordToDb
                    )
                }
            }
        }
    }
//    private fun mapStringToCardOfWord(jsonString: String): CardOfWord {
//        return Gson().fromJson(jsonString, CardOfWord::class.java)
//    }

    @SuppressLint("LongLogTag")
    private fun handleAddingWordToDb(wordId: Long) {
        Log.e(TAG, "new word #$wordId has been added to the database")
        saveWordToCategory(categoryId, wordId)
    }

    private fun saveWordToCategory(categoryId: Long, cardId: Long) {
        viewModelScope.launch {
            binderCardToCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleAddingWordToCategory
                )
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun handleAddingWordToCategory(wordId: Long) {
        Log.e(TAG, "word #$wordId has been assigned with the category #$categoryId")
        getAllWordsOfCategory(categoryId)
    }

    fun getAllWordsOfCategory(categoryId: Long) {
        viewModelScope.launch {
            loaderCardsOfCategory(categoryId) {
                it.fold(
                    ::handleFailure,
                    ::handleGettingAllWords
                )
            }
        }
    }

    private fun handleGettingAllWords(categoryWithCards: CategoryWithCards) {
        _cards.value = categoryWithCards.cards
    }

    fun deleteWordFromCategory(cardId: Long) {
        viewModelScope.launch {
            removerCardFromCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleDeletingWordFromCat
                )
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun handleDeletingWordFromCat(numOfDeletedWord: Int) {
        Log.e(TAG, "#$numOfDeletedWord was deleted from $categoryId")
    }

    private fun checkIfWordIsInCategory(card: Card): Boolean? {
        return liveCards.value?.contains(card)

    }
}