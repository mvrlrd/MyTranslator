package ru.mvrlrd.mytranslator.ui.fragments

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.data.network.response.SearchResultResponse
import ru.mvrlrd.mytranslator.domain.use_cases.binders.BinderCardToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCardToDb
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCategoryToBd
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.*
import ru.mvrlrd.mytranslator.domain.use_cases.network.GetSearchResult
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCardFromCategory
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoriesFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoryFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.update.*
import ru.mvrlrd.mytranslator.presentation.MeaningModelForRecycler
import ru.mvrlrd.mytranslator.presentation.WordModelForRecycler
import ru.mvrlrd.mytranslator.presenter.BaseViewModel
import androidx.lifecycle.asLiveData

private const val TAG = "SharedViewModel"

class SharedViewModel(
    private val inserterCategoryToBd: InserterCategoryToBd,
    private val loaderCardsOfCategory: LoaderCardsOfCategory,
    private val loaderChosenCategoriesForLearning:
    LoaderChosenCategoriesForLearning,
    private val removerCategoriesFromDb: RemoverCategoriesFromDb,
    private val removerCategoryFromDb: RemoverCategoryFromDb,
    private val updaterCategoryProgress: UpdaterCategoryProgress,
    private val updaterCategoryNameAndIcon: UpdaterCategoryNameAndIcon,
    private val updaterCategoryIsChecked: UpdaterCategoryIsChecked,
    private val unselecterAllCategories: UpdaterAllCategoriesToUnselect,
    private val getterCatsFlow: GetterAllCatsFlow,
    private val inserterCardToDb: InserterCardToDb,
    private val updaterCardProgress: UpdaterCardProgress,
    private val binderCardToCategory: BinderCardToCategory,
    private val removerCardFromCategory: RemoverCardFromCategory,
    private val getSearchResult: GetSearchResult,
    private val getterCategory: GetterCategory

) : BaseViewModel() {


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

    private var _mutableLiveDataCats = MutableLiveData<List<Category>>()
    val catsLive = _mutableLiveDataCats

    private var _cards = MutableLiveData<List<Card>>()
    val liveCards: LiveData<List<Card>> = _cards
    var categoryId: Long = 0L

    //add new word
    private var _liveTranslations = MutableLiveData<List<MeaningModelForRecycler>>()
    val liveTranslations: LiveData<List<MeaningModelForRecycler>> = _liveTranslations
    private var queryName: String = ""



    init {
        getAllCatsFlow()
    }

    fun retrieveCategory(id: Long):LiveData<Category> {
return getterCategory.run(id).asLiveData()
    }


     private fun getAllCatsFlow() {

            viewModelScope.launch {
                getterCatsFlow.getAllCatsFlow().collect { categories ->
                    _mutableLiveDataCats.postValue(categories)
                }
            }

    }

     fun insertCategory(newCategory: Category) {
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

    fun updateCategorysNameAndIcon(category: Category){
        if(!checkIfCategoryAlreadyExists(category)){
            viewModelScope.launch {
                updaterCategoryNameAndIcon(category){
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


    private fun checkIfCategoryAlreadyExists(addingCategory: Category): Boolean{
        return if (catsLive.value.isNullOrEmpty()
            || !catsLive.value!!.contains(addingCategory)) {
            false
        } else {Log.e(TAG, "${addingCategory.name} already exists in Db")
            //do toast that it is already exists
            true
        }
    }

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


    private var _categoriesForLearning = MutableLiveData<List<Category>>()
    val liveCategoriesForLearning: LiveData<List<Category>> = _categoriesForLearning

    private var _cardsOfCategory = MutableLiveData<List<Card>>()
    val liveCardsOfCategory: LiveData<List<Card>> = _cardsOfCategory


    var allCards: MutableList<Card> = mutableListOf()

    fun getChosenCategories() {
        viewModelScope.launch {
            loaderChosenCategoriesForLearning(Unit) {
                it.fold(
                    ::handleFailure,
                    ::handleGetChosenCategories
                )
            }
        }
    }



    private fun handleGetChosenCategories(categoryList: List<Category>) {
        _categoriesForLearning.value = categoryList
//        for (i in categoryList){
//            getAllWordsOfCategory(i.categoryId)
//        }
//        _wordsList.value = allWordsList
    }

    fun updateCardProgress(cardId: Long, newProgress: Int){
        viewModelScope.launch {
            updaterCardProgress(arrayOf(cardId, newProgress.toLong())){
                it.fold(
                    ::handleFailure,
                    ::handleUpdateCardProgress
                )
            }
        }
    }
    private fun handleUpdateCardProgress(num : Int){
        Log.e(TAG, "$num card's progress was updated")
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
        for(i in categoryWithCards.cards){
            i.categoryId = categoryWithCards.category.categoryId
        }
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




    //CARDS OF CATEGORIES

    fun saveCardToDb(jsonString: String) {
        val card = mapJsonToCard(jsonString)
        if(!checkIfWordIsInCategory(card)!!) {
            viewModelScope.launch {
                inserterCardToDb(card) {
                    it.fold(
                        ::handleFailure,
                        ::handleSaveCardToDb
                    )
                }
            }
        }
    }
    private fun handleSaveCardToDb(wordId: Long) {
//        Log.e(TAG, "new word #$wordId has been added to the database")
        bindCardToCategory(categoryId, wordId)
    }

    private fun mapJsonToCard(jsonString: String): Card {
        return Gson().fromJson(jsonString, Card::class.java)
    }

    private fun bindCardToCategory(categoryId: Long, cardId: Long) {
        viewModelScope.launch {
            binderCardToCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleBindCardToCategory
                )
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun handleBindCardToCategory(wordId: Long) {
        Log.e(TAG, "word #$wordId has been assigned with the category #$categoryId")
        getAllWordsOfCategory(categoryId)
    }

    fun getAllWordsOfCategory(categoryId: Long) {
        viewModelScope.launch {
            loaderCardsOfCategory(categoryId) {
                it.fold(
                    ::handleFailure,
                    ::handleGetAllWordsOfCategory
                )
            }
        }
    }

    private fun handleGetAllWordsOfCategory(categoryWithCards: CategoryWithCards) {
        _cards.value = categoryWithCards.cards
    }

    fun deleteWordFromCategory(cardId: Long) {
        viewModelScope.launch {
            removerCardFromCategory(arrayOf(cardId, categoryId)) {
                it.fold(
                    ::handleFailure,
                    ::handleDeleteWordFromCategory
                )
            }
        }
    }

    @SuppressLint("LongLogTag")
    private fun handleDeleteWordFromCategory(numOfDeletedWord: Int) {
        Log.e(TAG, "#$numOfDeletedWord was deleted from $categoryId")
    }

    @SuppressLint("LongLogTag")
    private fun checkIfWordIsInCategory(card: Card): Boolean? {
        Log.e(TAG, "i am in checkIfWordIsInCategorycheckIfWordIsInCategory ${liveCards.value?.contains(card)}")
        return liveCards.value?.contains(card)
    }






    //add new word
    fun loadDataFromWeb(word: String) {
        queryName = word
        viewModelScope.launch {
            getSearchResult(word) {
                it.fold(
                    ::handleFailure,
                    ::handleLoadDataFromWeb
                )
            }
        }
    }

    private fun handleLoadDataFromWeb(response: ListSearchResult?) {
        val filteredResponseList: List<SearchResultResponse>? =
            response?.filter { it.text == queryName }
//        Log.e(TAG, "${filteredResponseList?.size}   sizeeeeeeee")
        _liveTranslations.value = filteredResponseList?.map { resp ->
            resp.meanings?.map { meaningsResponse ->
                MeaningModelForRecycler(
                    0,
                    resp.text,
                    meaningsResponse.translationResponse?.translation,
                    meaningsResponse.imageUrl,
                    meaningsResponse.transcription,
                    meaningsResponse.partOfSpeech,
                    meaningsResponse.prefix
                )
            }?.let {
                WordModelForRecycler(
                    it
                )
            }
        }?.flatMap { it!!.meanings }
    }

    fun saveCardToDb(meaningModelForRecycler: MeaningModelForRecycler) {
        viewModelScope.launch {
            meaningModelForRecycler.let { item ->
                inserterCardToDb(
                    Card(
                        item.id,
                        item.text,
                        item.translation,
                        item.image_url,
                        item.transcription,
                        item.partOfSpeech,
                        item.prefix,
                        0,
                        0
                    )
                )
                {
                    it.fold(
                        ::handleFailure,
                        ::handleSaveCardToDb
                    )
                }
            }
        }
    }



    companion object {
        const val TAG = "TranslationViewModel"
    }

    fun clearLiveTranslationList() {
        _liveTranslations.value = emptyList()
    }

}






