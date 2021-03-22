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
import ru.mvrlrd.mytranslator.domain.use_cases.cards.GetAllCardsFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.cards.GetterCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.SaveCardToFavorites
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel


private  val TAG = "WordsInCategoryViewModel"
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

//    private var _liveId = MutableLiveData<Long>()
//    val liveId: LiveData<Long> = _liveId

    var categoryId: Long = 0L



    private fun saveWordToCategory(categoryId: Long, cardId: Long) {
        viewModelScope.launch {
            addererWordToCategory(arrayOf(cardId, categoryId)){
                it.fold(
                    ::handleFailure,
                    ::handleAddingToCategory
                )
            }
        }
    }

    private fun handleAddingToCategory(id: Long){
        Log.e(TAG, "$id   word was added to Category" )
        getAllWordsOfCategory(categoryId)
    }

    fun saveWordToDb(cardOfWord: CardOfWord) {
        viewModelScope.launch {
            saveCardToFavorites(cardOfWord){
                it.fold(
                    ::handleFailure,
                    ::handleAddingWordToDb
                )
        }
    }
}
    private fun handleAddingWordToDb(wordId:Long){
        Log.e(TAG, "$wordId   word was added to Db" )
        saveWordToCategory(categoryId, wordId)

//        _liveId.value = wordId
    }

fun getAllWordsOfCategory(categoryId : Long){
    viewModelScope.launch {
        getterCardsOfCategory(categoryId){it.fold(
            ::handleFailure,
            ::handleGettingAllWords
        )}
    }
}

    private fun handleGettingAllWords(categoryWithWords: CategoryWithWords){
        _liveWordList.value = categoryWithWords.cards
    }

    fun clearLive(){
//        _liveId.value = null
    }
}