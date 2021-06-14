package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCategoryToBd
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.*
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoriesFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoryFromDb
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "CatViewModel"

class CategoriesViewModel(
    dbHelper: DbHelper
) : BaseViewModel() {
    private val localIRepository = LocalIRepository( dbHelper)
//insert//delete//clear//
    private val inserterCategoryToBd: InserterCategoryToBd =
        InserterCategoryToBd(localIRepository)
    private val loaderCategoriesOfDb: LoaderCategoriesOfDb =
        LoaderCategoriesOfDb(localIRepository)
    private val removerCategoriesFromDb: RemoverCategoriesFromDb =
        RemoverCategoriesFromDb(localIRepository)
    private val removerCategoryFromDb: RemoverCategoryFromDb =
        RemoverCategoryFromDb(localIRepository)
    private var _allCategories = MutableLiveData<List<Category>>()
    val liveAllCategories: LiveData<List<Category>> = _allCategories

    private fun insertCategory(newCategory: Category) {
        if (_allCategories.value.isNullOrEmpty()
            || !_allCategories.value!!.contains(newCategory))
            {
                viewModelScope.launch {
                    inserterCategoryToBd(newCategory) {
                        it.fold(
                            ::handleFailure,
                            ::handleAddNewCategory
                        )
                    }
                }
            }
            else if(_allCategories.value!!.contains(newCategory)) {
                Log.e(TAG, "${newCategory.name} already exists in Db")
            //do toast that it is already exists
                return
            }
        }
    private fun handleAddNewCategory(quantity: Long) {
        Log.e(TAG, "$quantity item added to Db")
        refreshCategoriesScreen()
    }
    fun refreshCategoriesScreen() {
        viewModelScope.launch {
            loaderCategoriesOfDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleRefreshCategoriesScreen
                )
            }
        }
    }
    private fun handleRefreshCategoriesScreen(loadedCategories: List<Category>) {
        //liveAllCategories is observed by CategoriesFragment --loadedCategories--> CategoriesAdapter --> recyclerView refreshes
        _allCategories.value = loadedCategories
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            inserterCategoryToBd(category)
        }
    }

    fun clearCategories() {
        viewModelScope.launch {
            removerCategoriesFromDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleClearCategories
                )
            }

        }
    }

    private fun handleClearCategories(numOfDeleted: Int) {
        Log.e(TAG, "$numOfDeleted items(all) were deleted from db")
        refreshCategoriesScreen()
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            removerCategoryFromDb(categoryId) {
                it.fold(
                    ::handleFailure,
                    ::handleDeleteCategory
                )
            }
        }
    }

    private fun handleDeleteCategory(oneItem: Int) {
        Log.e(TAG, "$oneItem item was deleted from db")
        refreshCategoriesScreen()
    }

    fun addCategory(string: Array<String>){
        val newCategory = Category( categoryId = string[0].toLong(), name = string[1], icon = string[2])
        insertCategory(newCategory)
    }
    fun addCategory(category: Category){
        insertCategory(category)
    }
    fun addCategory(editingCategoryId: Long, string: Array<String>){
        val category = Category( categoryId = editingCategoryId, name = string[0], icon = string[1])
        insertCategory(category)
    }

}






