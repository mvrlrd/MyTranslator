package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.selection.SelectionTracker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCategoryToBd
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.*
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoriesFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoryFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterAllCategoriesToUnselect
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterCategoryIsChecked
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterCategoryNameAndIcon
import ru.mvrlrd.mytranslator.domain.use_cases.update.UpdaterCategoryProgress
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "CatViewModel"

class CategoriesViewModel(
    localIRepository: LocalIRepository
) : BaseViewModel() {

//insert//delete//clear//
    private val inserterCategoryToBd = InserterCategoryToBd(localIRepository)
    private val loaderCardsOfCategory = LoaderCardsOfCategory(localIRepository)
    private val loaderChosenCategoriesForLearning = LoaderChosenCategoriesForLearning(localIRepository)
    private val removerCategoriesFromDb = RemoverCategoriesFromDb(localIRepository)
    private val removerCategoryFromDb = RemoverCategoryFromDb(localIRepository)
    private val updaterCategoryProgress = UpdaterCategoryProgress(localIRepository)
    private val updaterCategoryNameAndIcon = UpdaterCategoryNameAndIcon(localIRepository)
    private val updaterCategoryIsChecked = UpdaterCategoryIsChecked(localIRepository)
    private val unselecterAllCategories = UpdaterAllCategoriesToUnselect(localIRepository)

//Warning: Never expose mutable data fields from your ViewModel—make sure this
// data can't be modified from another class. Mutable data inside the ViewModel should always be private.


    //// Declare private mutable variable that can only be modified
    //// within the class it is declared.
    //private var _count = 0
    //
    //// Declare another public immutable field and override its getter method.
    //// Return the private property's value in the getter method.
    //// When count is accessed, the get() function is called and
    //// the value of _count is returned.
    //val count: Int
    //   get() = _count

    var selectionList = mutableListOf<Long>()


    private val getterCatsFlow = GetterAllCatsFlow(localIRepository)

    private var _mutableLiveDataCats = MutableLiveData<List<Category>>()
    val catsLive = _mutableLiveDataCats

    init {
        getAllCatsFlow()
    }

    private fun getAllCatsFlow() {
        viewModelScope.launch {
            getterCatsFlow.getAllCatsFlow().collect { categories ->
                _mutableLiveDataCats.postValue(categories)
            }
        }
    }

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

    private fun handleAddNewCategory(id: Long) {
        Log.e(TAG, "$id item added to Db")
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
    }

    fun unselectAllCategories(){
        viewModelScope.launch {
            unselecterAllCategories(Unit){
                it.fold(
                    ::handleFailure,
                    ::handleUnselectionAll
                )
            }
        }
    }

    private fun handleUnselectionAll(num: Int){
       for (item in selectionList){
           selectUnselectCategory(arrayOf(item.toString(), "true"))
       }
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
    }

    fun addCategory(string: Array<String>) {
        val newCategory = parseCategory(string)
        if (!checkIfCategoryAlreadyExists(newCategory)) {
            insertCategory(newCategory)
        }
    }

    private fun checkIfCategoryAlreadyExists(addingCategory: Category): Boolean{
        return if (catsLive.value.isNullOrEmpty()
            || !catsLive.value!!.contains(addingCategory)) {
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
        Log.e(TAG,"handleUpdateCategoryProgress")
    }
}






