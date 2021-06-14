package ru.mvrlrd.mytranslator.ui.fragments.learning

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCardToDb
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderChosenCategoriesForLearning
import ru.mvrlrd.mytranslator.domain.use_cases.update.RefresherCardProgress
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

private const val TAG = "LearningViewModel"

class LearningViewModel(
    dbHelper: DbHelper
) : BaseViewModel() {

    private val localIRepository = LocalIRepository( dbHelper)
    private val loaderChosenCategoriesForLearning: LoaderChosenCategoriesForLearning =
        LoaderChosenCategoriesForLearning(localIRepository)
    private val loaderCardsOfCategory: LoaderCardsOfCategory =
        LoaderCardsOfCategory(localIRepository)
    private val inserterCardToDb: InserterCardToDb =
        InserterCardToDb(localIRepository)
    private val refresherCardProgress: RefresherCardProgress=
        RefresherCardProgress(localIRepository)

    private var _categoriesForLearning = MutableLiveData<List<Category>>()
    val liveCategoriesForLearning: LiveData<List<Category>> = _categoriesForLearning

    private var _cardsOfCategory = MutableLiveData<List<Card>>()
    val liveCardsOfCategory: LiveData<List<Card>> = _cardsOfCategory


    var allCards: MutableList<Card> = mutableListOf()

    fun getCategories() {
        viewModelScope.launch {
            loaderChosenCategoriesForLearning(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleGettingCategories
                )
            }
        }
    }

    private fun handleGettingCategories(categoryList: List<Category>) {
        _categoriesForLearning.value = categoryList
//        for (i in categoryList){
//            getAllWordsOfCategory(i.categoryId)
//        }
//        _wordsList.value = allWordsList
    }

    fun updateCardProgress(cardId: Long, newProgress: Int){
        viewModelScope.launch {
            refresherCardProgress(arrayOf(cardId, newProgress.toLong())){
                it.fold(
                    ::handleFailure,
                    ::handleUpdateCardProgress
                )
            }
        }
    }
    private fun handleUpdateCardProgress(num : Int){
        Log.e(TAG, "$num item's progress was updated")
    }

    fun getAllWordsOfCategory1(cats: List<Category>) {
        viewModelScope.launch {
            for (i in cats) {
                loaderCardsOfCategory(i.categoryId) {
                    it.fold(
                        ::handleFailure,
                        ::handleGettingAllWords2
                    )
                }
            }

        }
    }

    private fun handleGettingAllWords2(categoryWithCards: CategoryWithCards) {
        allCards.addAll(categoryWithCards.cards)
        _cardsOfCategory.value = allCards
        Log.e(TAG, allCards.toString())
    }

    fun updateCardInDb(card: Card){
        viewModelScope.launch { inserterCardToDb(card){
            it.fold(
                ::handleFailure,
                ::handleUpdateCardInDb
            )
        } }
    }

    private fun handleUpdateCardInDb(cardId: Long){

    }
}