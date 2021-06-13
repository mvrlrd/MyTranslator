package ru.mvrlrd.mytranslator.ui.fragments.words

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.domain.use_cases.binders.BinderCardToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCardFromCategory
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCardToDb
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "WordsInCategoryViewModel"

class CardsOfCategoryViewModel(
    dbHelper: DbHelper
) : BaseViewModel() {

    private val localIRepository = LocalIRepository( dbHelper)
    private val loaderCardsOfCategory: LoaderCardsOfCategory =
        LoaderCardsOfCategory(localIRepository)
    private val binderCardToCategory: BinderCardToCategory =
        BinderCardToCategory(localIRepository)
    private val inserterCardToDb: InserterCardToDb =
        InserterCardToDb(localIRepository)
    private val removerCardFromCategory: RemoverCardFromCategory =
        RemoverCardFromCategory(localIRepository)
    private var _cards = MutableLiveData<List<Card>>()
    val liveCards: LiveData<List<Card>> = _cards
    var categoryId: Long = 0L

    fun saveCardToDb(card: Card) {
            viewModelScope.launch {
                inserterCardToDb(card) {
                    it.fold(
                        ::handleFailure,
                        ::handleSaveCardToDb
                    )
                }
            }
    }
    private fun handleSaveCardToDb(wordId: Long) {
//        Log.e(TAG, "new word #$wordId has been added to the database")
        saveWordToCategory(categoryId, wordId)
    }
     @SuppressLint("LongLogTag")
     fun mapJsonToCard(jsonString: String) {
        val card = Gson().fromJson(jsonString, Card::class.java)
        if(!checkIfWordIsInCategory(card)!!){
            Log.e(TAG, "word is not in Category000000000000")
            saveCardToDb(card)
        }
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

    @SuppressLint("LongLogTag")
    private fun checkIfWordIsInCategory(card: Card): Boolean? {
        Log.e(TAG, "i am in checkIfWordIsInCategorycheckIfWordIsInCategory ${liveCards.value?.contains(card)}")
        return liveCards.value?.contains(card)
    }
}