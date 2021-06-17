package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCategoryToBd
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.*
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoriesFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoryFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterCategoryIsChecked
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterCategoryNameAndIcon
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterCategoryProgress
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
    private val loaderCardsOfCategory: LoaderCardsOfCategory =
        LoaderCardsOfCategory(localIRepository)
    private val loaderChosenCategoriesForLearning: LoaderChosenCategoriesForLearning =
        LoaderChosenCategoriesForLearning(localIRepository)
    private val removerCategoriesFromDb: RemoverCategoriesFromDb =
        RemoverCategoriesFromDb(localIRepository)
    private val removerCategoryFromDb: RemoverCategoryFromDb =
        RemoverCategoryFromDb(localIRepository)

    private val updaterCategoryProgress: UpdaterCategoryProgress =
        UpdaterCategoryProgress(localIRepository)
    private val updaterCategoryNameAndIcon: UpdaterCategoryNameAndIcon =
        UpdaterCategoryNameAndIcon(localIRepository)
    private val updaterCategoryIsChecked: UpdaterCategoryIsChecked =
        UpdaterCategoryIsChecked(localIRepository)

    private var _allCategories = MutableLiveData<List<Category>>()
    val liveAllCategories: LiveData<List<Category>> = _allCategories

    private fun insertCategory(newCategory: Category) {
        viewModelScope.launch {
            inserterCategoryToBd(newCategory) {
                it.fold(
                    ::handleFailure,
                    ::handleAddNewCategory
                )
            }
        }
    }
    private fun handleAddNewCategory(quantity: Long) {
        Log.e(TAG, "$quantity item added to Db")
        loadAllCategories()
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            loaderCategoriesOfDb(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleLoadAllCategories
                )
            }
        }
    }
    private fun handleLoadAllCategories(loadedCategories: List<Category>) {
        //liveAllCategories is observed by CategoriesFragment --loadedCategories--> CategoriesAdapter --> recyclerView refreshes
        _allCategories.value = loadedCategories
    }

    fun updateCategorysNameAndIcon(str: Array<String>){
        val editedCategory = parseCategory(str)
        if(!checkIfCategoryAlreadyExists(editedCategory)){
            viewModelScope.launch {
                updaterCategoryNameAndIcon(str){
                    it.fold(
                        ::handleFailure,
                        ::handleUpdateCategorysNameAndIcon
                    )
                }
            }
        }
    }
    private fun handleUpdateCategorysNameAndIcon(num: Int){
        Log.e(TAG, "$num category's name and icon were updated")
        loadAllCategories()
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
        loadAllCategories()
    }

    fun selectUnselectCategory(arr: Array<String>){
        viewModelScope.launch {
            updaterCategoryIsChecked(arr){
                it.fold(
                    ::handleFailure,
                    ::handleSelectUnselectCategory
                )
            }
        }
    }
    private fun handleSelectUnselectCategory(num: Int){
        Log.e(TAG, "$num item was checked/unchecked")
        loadAllCategories()
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
        loadAllCategories()
    }

    fun addCategory(string: Array<String>) {
        val newCategory = parseCategory(string)
        if (!checkIfCategoryAlreadyExists(newCategory)) {
            insertCategory(newCategory)
        }
    }

    private fun checkIfCategoryAlreadyExists(addingCategory: Category): Boolean{
        return if (_allCategories.value.isNullOrEmpty()
            || !_allCategories.value!!.contains(addingCategory)) {
            false
        } else {Log.e(TAG, "${addingCategory.name} already exists in Db")
            //do toast that it is already exists
            true
        }
    }

    private fun parseCategory(string: Array<String>) = Category(
        categoryId = string[0].toLong(),
        name = string[1],
        icon = string[2]
    )


    fun refreshCategoriesList() {
        viewModelScope.launch {
            loaderChosenCategoriesForLearning(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleRefreshCategoriesScreen2
                )
            }
        }
    }

    private fun handleRefreshCategoriesScreen2(cats: List<Category>){
        if(cats.isNullOrEmpty()){
            loadAllCategories()
        }else{
            getAllCardsOfCategory(cats)
        }

    }

    private fun getAllCardsOfCategory(categories: List<Category>) {
        viewModelScope.launch {
            for(cat in categories) {
                loaderCardsOfCategory(cat.categoryId) {
                    it.fold(
                        ::handleFailure,
                        ::handleGetAllCardsOfCategory
                    )
                }
            }
        }
    }

    private fun handleGetAllCardsOfCategory(categoryWithCards: CategoryWithCards) {
        if (categoryWithCards.cards.isNotEmpty()) {
            val progress = categoryWithCards.averageProgress()
            val updatedCategoryId = categoryWithCards.category.categoryId
            updateCategoryProgress(updatedCategoryId, progress)
        }
    }
    private fun updateCategoryProgress(categoryId: Long, newProgress: Double) {
        viewModelScope.launch {
            updaterCategoryProgress(arrayOf(categoryId.toString(), newProgress.toString())){
                it.fold(
                    ::handleFailure,
                    ::handleUpdateCategoryProgress
                )
            }
        }
    }
    private fun handleUpdateCategoryProgress(num: Int){
        loadAllCategories()
//        ç
    }
}






