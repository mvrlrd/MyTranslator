package ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.cards.AddererWordToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.GetterCardsOfCategory
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class WordsInCategoryViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val getterCardsOfCategory: GetterCardsOfCategory = GetterCardsOfCategory(searchResultRepository)
    private val addererWordToCategory: AddererWordToCategory = AddererWordToCategory(searchResultRepository)


     fun saveWordToCategory(categoryId: Long, cardId: Long){
        viewModelScope.launch {
            addererWordToCategory(arrayOf(cardId, categoryId))
        }
    }


}