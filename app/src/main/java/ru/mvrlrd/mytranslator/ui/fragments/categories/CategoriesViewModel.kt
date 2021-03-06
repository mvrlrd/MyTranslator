package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.categories.*
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private val TAG = "CatViewModel"

class CategoriesViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val categoriesLoader: TagsLoader = TagsLoader(searchResultRepository)
    private val clearerCategories: ClearCategories = ClearCategories(searchResultRepository)
    private val newCategoryAdderer: NewTagAdderer = NewTagAdderer(searchResultRepository)
    private val removerCategoryFromDb: RemoverCategoryFromDb = RemoverCategoryFromDb(searchResultRepository)
    private var _allCategoryList = MutableLiveData<List<Category>>()
    val liveAllCategoriesList: LiveData<List<Category>> = _allCategoryList

    fun addNewCategory(name: String, icon: String) {
        val groupTag = Category(0, name, icon)
        when {
            _allCategoryList.value.isNullOrEmpty()
                    || !_allCategoryList.value!!.contains(groupTag) -> {
                viewModelScope.launch {
                    newCategoryAdderer(arrayOf(name, icon)) {
                        it.fold(
                            ::handleFailure,
                            ::handleAddingCategory
                        )
                    }
                }
            }
            _allCategoryList.value!!.contains(groupTag) -> {
                return
            }
        }
    }

    private fun handleAddingCategory(quantity: Long) {
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

    fun clearCategories() {
        viewModelScope.launch {
            clearerCategories(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleClearingCategories
                )
            }

        }
    }
    private fun handleClearingCategories(numOfDeleted: Int) {
        Log.e(TAG, "$numOfDeleted items were deleted from db")
        refreshCategoriesScreen()
    }

    fun deleteCategory(categoryId: Long){
        viewModelScope.launch {
            removerCategoryFromDb(categoryId){it.fold(
                ::handleFailure,
                ::handleDeletingCategory
            )}
        }
    }
    private fun handleDeletingCategory(numOfDeleted : Int){
        Log.e(TAG, "$numOfDeleted item was deleted from db")
        refreshCategoriesScreen()
    }
}






