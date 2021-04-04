package ru.mvrlrd.mytranslator.ui.fragments.words

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.cards.AddererWordToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.GetterCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.RemoverWordFromCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.SaveCardToFavorites
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private val TAG = "WordsInCategoryViewModel"

class WordsListViewModel(
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

    private val removerWordFromCategory: RemoverWordFromCategory = RemoverWordFromCategory(searchResultRepository)


    private var _liveWordList = MutableLiveData<List<CardOfWord>>()
    val liveWordList: LiveData<List<CardOfWord>> = _liveWordList
    var categoryId: Long = 0L


    fun saveWordToDb(str : String) {
    val card = Gson().fromJson(str, CardOfWord::class.java)
        if (checkIfWordIsInCategory(card) == false) {
            viewModelScope.launch {
                saveCardToFavorites(card) {
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




    fun deleteWordFromCategory(cardId: Long){
        viewModelScope.launch {
            removerWordFromCategory(arrayOf(cardId, categoryId)){
                it.fold(
                    ::handleFailure,
                    ::handleDeletingWordFromCat
                )
            }
        }
    }
    private fun handleDeletingWordFromCat(numOfDeletedWord : Int){
        Log.e(TAG, "#$numOfDeletedWord was deleted from $categoryId")
    }

    private fun checkIfWordIsInCategory(card: CardOfWord):Boolean? {
        return liveWordList.value?.contains(card)

    }
}