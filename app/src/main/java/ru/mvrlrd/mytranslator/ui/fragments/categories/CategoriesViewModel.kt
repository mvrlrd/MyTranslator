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

    fun addNewCategory(id: Long, name: String, icon: String) {
        val groupTag = Category(id, name, icon, false, 0.0)
        when {
            _allCategories.value.isNullOrEmpty()
                    || !_allCategories.value!!.contains(groupTag) -> {
                viewModelScope.launch {
                    inserterCategoryToBd(groupTag) {
                        it.fold(
                            ::handleFailure,
                            ::handleAddingCategory
                        )
                    }
                }
            }
            _allCategories.value!!.contains(groupTag) -> {
                return
            }
        }
    }

    private fun handleAddingCategory(quantity: Long) {
        Log.e(TAG, "$quantity      added")
        refreshCategoriesScreen()
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            inserterCategoryToBd(category)
        }
    }

    fun refreshCategoriesScreen() {
        viewModelScope.launch {
            loaderCategoriesOfDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::refreshListOfCategories
                )
            }
        }
    }

    private fun refreshListOfCategories(allCategoriesList: List<Category>) {
        _allCategories.value = allCategoriesList
    }

    fun clearCategories() {
        viewModelScope.launch {
            removerCategoriesFromDb(Unit) {
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

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            removerCategoryFromDb(categoryId) {
                it.fold(
                    ::handleFailure,
                    ::handleDeletingCategory
                )
            }
        }
    }

    private fun handleDeletingCategory(numOfDeleted: Int) {
        Log.e(TAG, "$numOfDeleted item was deleted from db")
        refreshCategoriesScreen()
    }
}






