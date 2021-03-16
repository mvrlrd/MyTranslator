package ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.tags.ClearCategories
import ru.mvrlrd.mytranslator.domain.use_cases.tags.NewTagAdderer
import ru.mvrlrd.mytranslator.domain.use_cases.tags.TagsLoader
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class AddNewCategoryViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val categoriesLoader: TagsLoader = TagsLoader(searchResultRepository)
//    private val addererTagToCard : AddererTagToCard = AddererTagToCard(searchResultRepository)
//    private val removerTagFromCard: RemoverTagFromCard = RemoverTagFromCard(searchResultRepository)

    private val clearerCategories : ClearCategories = ClearCategories(searchResultRepository)


    private val newCategoryAdderer : NewTagAdderer = NewTagAdderer(searchResultRepository)
//    private val currentCardTagsPicker : CurrentCardTagsPicker = CurrentCardTagsPicker(searchResultRepository)

//    private var _tagsOfCurrentCard = MutableLiveData<List<GroupTag>>()
//    val liveTagsOfCurrentCard : LiveData<List<GroupTag>> = _tagsOfCurrentCard

    @Volatile
    private var _allCategoryList = MutableLiveData<List<Category>>()
    val liveAllCategoriesList : LiveData<List<Category>> = _allCategoryList

    init {
        getAllCategories()
    }


    fun addNewCategory(tagText: String, icon: String) {
        val groupTag = Category(0, tagText, icon)

        when {
            _allCategoryList.value.isNullOrEmpty()
                    || !_allCategoryList.value!!.contains(groupTag) -> {
                viewModelScope.launch {
                    newCategoryAdderer(arrayOf(tagText,icon))
                    getAllCategories()
                }
            }
            _allCategoryList.value!!.contains(groupTag) -> {
                return
            }
        }

    }

    fun clearCategories(){
        viewModelScope.launch {
            clearerCategories(Unit)
        }
    }


    private fun getAllCategories(){
        viewModelScope.launch  {
            categoriesLoader(Unit) {
                it.fold(
                    ::handleFailure,
                    ::mapCardForRecycler
                )
            }
        }
    }
    private fun mapCardForRecycler(allCategoriesList: List<Category>) {
        _allCategoryList.value = allCategoriesList
    }

    fun addTagToCurrentCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
//            addererTagToCard(arrayOf(idCard, idTag))
        }
    }

    fun deleteTagFromCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
//            removerTagFromCard(arrayOf(idCard,idTag))
        }
    }

    fun getAllTagsForCurrentCard(currentCardId : Long){
        viewModelScope.launch {
//            currentCardTagsPicker(currentCardId) {
//                it.fold(
//                    ::handleFailure,
//                    ::handleCardWithTag
//                )
        }
    }
}

private fun handleCardWithTag(cardWithTag: CardWithTag){
//        cardWithTag.tags.let { _tagsOfCurrentCard.value = it }
}
