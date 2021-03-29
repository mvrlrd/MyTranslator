package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.cards.GetterCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.categories.GetterLearningCategories
import ru.mvrlrd.mytranslator.domain.use_cases.categories.TagsLoader
import ru.mvrlrd.mytranslator.presenter.BaseViewModel
import java.util.*


private val TAG ="LearningViewModel"
class LearningViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val getterLearningCategories: GetterLearningCategories =
        GetterLearningCategories(searchResultRepository)
    private val getterWordsOfCategory: GetterCardsOfCategory = GetterCardsOfCategory(searchResultRepository)


    private var _learningCategoryList = MutableLiveData<List<Category>>()
    val liveLearningCategoriesList: LiveData<List<Category>> = _learningCategoryList

    private var _wordsList = MutableLiveData<List<CardOfWord>>()
    val liveWordsList: LiveData<List<CardOfWord>> = _wordsList

    var allWordsList: MutableList<CardOfWord> = mutableListOf()

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
//        for (i in categoryList){
//            getAllWordsOfCategory(i.categoryId)
//        }
//        _wordsList.value = allWordsList
    }

    fun getAllWordsOfCategory1(cats:List<Category>) {
        viewModelScope.launch {
            for (i in cats){
                getterWordsOfCategory(i.categoryId){ it.fold(
                    ::handleFailure,
                    ::handleGettingAllWords2
                )}
            }

        }
    }
    private fun handleGettingAllWords2(categoryWithWords: CategoryWithWords) {
        allWordsList.addAll(categoryWithWords.cards)
        _wordsList.value = allWordsList
        Log.e(TAG,allWordsList.toString())
    }






}