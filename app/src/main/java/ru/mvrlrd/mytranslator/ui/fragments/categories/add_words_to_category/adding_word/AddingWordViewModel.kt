package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.adding_word

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.cards.AddererWordToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.SaveCardToFavorites
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private val TAG = "AddingWordViewModel"

class AddingWordViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val saveCardToFavorites: SaveCardToFavorites = SaveCardToFavorites(searchResultRepository)
    private val addererWordToCategory: AddererWordToCategory = AddererWordToCategory(searchResultRepository)

    fun saveWordToDb(card : CardOfWord){
        viewModelScope.launch {
            saveCardToFavorites(card){it.fold(
                ::handleFailure,
                ::handleSaving
            )}

        }
    }

    private fun handleSaving(cardId:Long){
       Log.e(TAG,"$cardId    was added to db")
    }

     private fun saveWordToCategory(categoryId: Long, cardId: Long){
        viewModelScope.launch {
            addererWordToCategory(arrayOf(cardId, categoryId))
        }
    }







}
