package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderChosenCategoriesForLearning
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private val TAG ="LearningViewModel"
class LearningViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val loaderChosenCategoriesForLearning: LoaderChosenCategoriesForLearning =
        LoaderChosenCategoriesForLearning(searchResultRepository)
    private val loaderWordsOfCardsOfCategory: LoaderCardsOfCategory = LoaderCardsOfCategory(searchResultRepository)


    private var _learningCategoryList = MutableLiveData<List<Category>>()
    val liveLearningCategoriesList: LiveData<List<Category>> = _learningCategoryList

    private var _wordsList = MutableLiveData<List<Card>>()
    val liveWordsList: LiveData<List<Card>> = _wordsList

    var allWordsList: MutableList<Card> = mutableListOf()

    fun getCategories(){
        viewModelScope.launch {
            loaderChosenCategoriesForLearning(Unit){it.fold(
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
                loaderWordsOfCardsOfCategory(i.categoryId){ it.fold(
                    ::handleFailure,
                    ::handleGettingAllWords2
                )}
            }

        }
    }
    private fun handleGettingAllWords2(categoryWithCards: CategoryWithCards) {
        allWordsList.addAll(categoryWithCards.cards)
        _wordsList.value = allWordsList
        Log.e(TAG,allWordsList.toString())
    }






}