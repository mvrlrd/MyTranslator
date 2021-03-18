package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.tags.*
import ru.mvrlrd.mytranslator.presenter.BaseViewModel


private val TAG = "CatViewModel"
class CategoriesViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val categoriesLoader: TagsLoader = TagsLoader(searchResultRepository)
//    private val addererTagToCard : AddererTagToCard = AddererTagToCard(searchResultRepository)
//    private val removerTagFromCard: RemoverTagFromCard = RemoverTagFromCard(searchResultRepository)
private val clearerCategories : ClearCategories = ClearCategories(searchResultRepository)

    private val newCategoryAdderer: NewTagAdderer = NewTagAdderer(searchResultRepository)
//    private val currentCardTagsPicker : CurrentCardTagsPicker = CurrentCardTagsPicker(searchResultRepository)

//    private var _tagsOfCurrentCard = MutableLiveData<List<GroupTag>>()
//    val liveTagsOfCurrentCard : LiveData<List<GroupTag>> = _tagsOfCurrentCard


    private var _allCategoryList = MutableLiveData<List<Category>>()
    val liveAllCategoriesList: LiveData<List<Category>> = _allCategoryList

    init {
//        getAllCategories()
    }


    fun addNewCategory(name: String, icon: String) {
        val groupTag = Category(0, name, icon)
        when {
            _allCategoryList.value.isNullOrEmpty()
                    || !_allCategoryList.value!!.contains(groupTag) -> {
                viewModelScope.launch {
                    newCategoryAdderer(arrayOf(name, icon)) {
                        it.fold(
                            ::handleFailure,
                            ::handleAdding
                        )
                    }
                }
            }
            _allCategoryList.value!!.contains(groupTag) -> {
                return
            }
        }

    }

    private fun handleAdding(quantity: Long) {
        Log.e(TAG, "$quantity      added")
        refreshCategoriesScreen()
    }


    fun refreshCategoriesScreen() {
        viewModelScope.launch {
            categoriesLoader(Unit) {
                it.fold(
                    ::handleFailure,
                    ::refreshListOfCategories
                )
            }

        }
    }

    private fun refreshListOfCategories(allCategoriesList: List<Category>) {
        _allCategoryList.value = allCategoriesList
    }

    fun addTagToCurrentCard(idCard: Long, idTag: Long) {
        viewModelScope.launch {
//            addererTagToCard(arrayOf(idCard, idTag))
        }
    }

    fun deleteTagFromCard(idCard: Long, idTag: Long) {
        viewModelScope.launch {
//            removerTagFromCard(arrayOf(idCard,idTag))
        }
    }

    fun getAllTagsForCurrentCard(currentCardId: Long) {
        viewModelScope.launch {
//            currentCardTagsPicker(currentCardId) {
//                it.fold(
//                    ::handleFailure,
//                    ::handleCardWithTag
//                )
        }
    }

    fun clearCategories() {
        viewModelScope.launch {
            clearerCategories(Unit){
                it.fold(
                    ::handleFailure,
                    ::handleClearingCategories
                )
            }

        }
    }

    private fun handleClearingCategories(numOfDeleted: Int){
        Log.e(TAG, "$numOfDeleted were deleted from db")
        refreshCategoriesScreen()
    }
}



    private fun handleCardWithTag(cardWithTag: CardWithTag){
//        cardWithTag.tags.let { _tagsOfCurrentCard.value = it }
    }



