package ru.mvrlrd.mytranslator.ui.fragments.learning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.categories.GetterLearningCategories
import ru.mvrlrd.mytranslator.domain.use_cases.categories.TagsLoader
import ru.mvrlrd.mytranslator.presenter.BaseViewModel
import java.util.*

class LearningViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val getterLearningCategories: GetterLearningCategories =
        GetterLearningCategories(searchResultRepository)

    private var _learningCategoryList = MutableLiveData<List<Category>>()
    val liveLearningCategoriesList: LiveData<List<Category>> = _learningCategoryList

    fun getCategories(){
        viewModelScope.launch {
            getterLearningCategories(Unit){it.fold(
                ::handleFailure,
                ::handleGettingCategories
            )}
        }
    }

    private fun handleGettingCategories(categoryList: List<Category>){
        _learningCategoryList.value = categoryList
    }
}