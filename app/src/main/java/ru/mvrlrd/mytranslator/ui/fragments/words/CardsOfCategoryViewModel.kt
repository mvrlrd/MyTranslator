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

private const val TAG = "CardsOfCategoryViewModel"

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

    fun saveCardToDb(jsonString: String) {
        val card = mapJsonToCard(jsonString)
        if(!checkIfWordIsInCategory(card)!!) {
            viewModelScope.launch {
                inserterCardToDb(card) {
                    it.fold(
                        ::handleFailure,
                        ::handleSaveCardToDb
                    )
                }
            }
        }
    }
    private fun handleSaveCardToDb(wordId: Long) {
//        Log.e(TAG, "new word #$wordId has been added to the database")
        bindCardToCategory(categoryId, wordId)
    }

    private fun mapJsonToCard(jsonString: String): Card {
        return Gson().fromJson(jsonString, Card::class.java)
    }

    private fun bindCardToCategory(categoryId: Long, cardId: Long) {
        viewModelScope.launch {
            binderCardToCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleBindCardToCategory
                )
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun handleBindCardToCategory(wordId: Long) {
        Log.e(TAG, "word #$wordId has been assigned with the category #$categoryId")
        getAllWordsOfCategory(categoryId)
    }

    fun getAllWordsOfCategory(categoryId: Long) {
        viewModelScope.launch {
            loaderCardsOfCategory(categoryId) {
                it.fold(
                    ::handleFailure,
                    ::handleGetAllWordsOfCategory
                )
            }
        }
    }

    private fun handleGetAllWordsOfCategory(categoryWithCards: CategoryWithCards) {
        _cards.value = categoryWithCards.cards
    }

    fun deleteWordFromCategory(cardId: Long) {
        viewModelScope.launch {
            removerCardFromCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleDeleteWordFromCategory
                )
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun handleDeleteWordFromCategory(numOfDeletedWord: Int) {
        Log.e(TAG, "#$numOfDeletedWord was deleted from $categoryId")
    }

    @SuppressLint("LongLogTag")
    private fun checkIfWordIsInCategory(card: Card): Boolean? {
        Log.e(TAG, "i am in checkIfWordIsInCategorycheckIfWordIsInCategory ${liveCards.value?.contains(card)}")
        return liveCards.value?.contains(card)
    }





//    private fun mapperAllStringsToOne(oneString: String): String {
//        val arr = oneString.split(";")
//        val str2 = StringBuilder()
//        if (arr.size == 2) {
//            str2.append("{\"id\":\"0\",")
//            str2.append("\"word\":\"${arr[0].changeSymbol()}\",")
//            str2.append("\"translation\":\"${arr[1].changeSymbol()}\",")
//            str2.append("\"image_url\":\"_\",")
//            str2.append("\"transcription\":\"_\",")
//            str2.append("\"partOfSpeech\":\"_\",")
//            str2.append("\"prefix\":\"_\",")
//            str2.append("\"progress\":\"0\"}")
//        }
//        return str2.toString()
//    }
//
//    private fun String.changeSymbol(): String =
//        this.replace("\"", "\\\"")
}