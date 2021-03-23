package ru.mvrlrd.mytranslator.ui.old.old.tag_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.cards.AddererWordToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.cards.RemoverWordFromCategory
import ru.mvrlrd.mytranslator.domain.use_cases.categories.*
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class TagDialogViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val tagsLoader: TagsLoader = TagsLoader(searchResultRepository)
    private val addererWordToCategory : AddererWordToCategory = AddererWordToCategory(searchResultRepository)
    private val removerWordFromCategory: RemoverWordFromCategory = RemoverWordFromCategory(searchResultRepository)
    private val newTagAdderer : NewTagAdderer = NewTagAdderer(searchResultRepository)
    private val currentCardTagsPicker : CurrentCardTagsPicker = CurrentCardTagsPicker(searchResultRepository)

    private var _tagsOfCurrentCard = MutableLiveData<List<Category>>()
    val liveTagsOfCurrentCard : LiveData<List<Category>> = _tagsOfCurrentCard

    private var _allTagList = MutableLiveData<List<Category>>()
    val liveAllTagList : LiveData<List<Category>> = _allTagList

    init {
        getAllTags()
    }

    fun loadTagToDataBase(tagText: String) {
        val groupTag = Category(0, tagText, "false")
        when {
            _allTagList.value.isNullOrEmpty()
                    || !_allTagList.value!!.contains(groupTag) -> {
                viewModelScope.launch {
                    newTagAdderer(arrayOf(tagText,"sdf"))
                    getAllTags()
                }
            }
            _allTagList.value!!.contains(groupTag) -> {
                return
            }
        }
    }

    private fun getAllTags(){
        viewModelScope.launch  {
            tagsLoader(Unit) {
                it.fold(
                    ::handleFailure,
                    ::mapCardForRecycler
                )
            }
        }
    }
    private fun mapCardForRecycler(allTagsList: List<Category>) {
        _allTagList.value = allTagsList
    }

    fun addTagToCurrentCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
            addererWordToCategory(arrayOf(idCard, idTag))
        }
    }

    fun deleteTagFromCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
            removerWordFromCategory(arrayOf(idCard,idTag))
        }
    }

    fun getAllTagsForCurrentCard(currentCardId : Long){
        viewModelScope.launch {
            currentCardTagsPicker(currentCardId) {
                it.fold(
                    ::handleFailure,
                    ::handleCardWithTag
                )
            }
        }
    }

    private fun handleCardWithTag(cardWithTag: CardWithTag){
        cardWithTag.tags.let { _tagsOfCurrentCard.value = it }
    }


}