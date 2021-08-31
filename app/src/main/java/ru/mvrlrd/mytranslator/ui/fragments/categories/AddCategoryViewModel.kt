package ru.mvrlrd.mytranslator.ui.fragments.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCategoryToBd
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.GetterCategory
import ru.mvrlrd.mytranslator.domain.use_cases.update.*
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "AddCategoryViewModel"
class AddCategoryViewModel(
    private val inserterCategoryToBd: InserterCategoryToBd,
    private val updaterCategoryNameAndIcon: UpdaterCategoryNameAndIcon,
    private val getterCategory: GetterCategory
) : BaseViewModel() {
    private var _mutableLiveDataCats = MutableLiveData<List<Category>>()
    private val allCategories = _mutableLiveDataCats

    fun insertCategory(category: Category) {
        if(!ifExists(category)) {
            viewModelScope.launch {
                inserterCategoryToBd(category) {
                    it.fold(
                        ::handleFailure,
                        ::handleAdding
                    )
                }
            }
        }
    }

    private fun handleAdding(id: Long) {
        Log.e(TAG, "$id item added to Db")
    }

    fun retrieveCategory(id: Long): LiveData<Category> {
        return getterCategory.run(id).asLiveData()
    }

    fun editCategory(category: Category){
        if(!ifExists(category)){
            viewModelScope.launch {
                updaterCategoryNameAndIcon(category){
                    it.fold(
                        ::handleFailure,
                        ::handleEditing
                    )
                }
            }
        }
    }

    private fun handleEditing(num: Int){
        Log.e(TAG, "$num category was updated")
    }
    private fun ifExists(category: Category): Boolean{
        return if (allCategories.value.isNullOrEmpty()
            || !allCategories.value!!.contains(category)) {
            false
        } else {
            Log.e(TAG, "${category.name} already exists in Db")
            //do toast that it is already exists
            true
        }
    }
}


