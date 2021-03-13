package ru.mvrlrd.mytranslator.ui.fragments.tag_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.network.ApiHelper
import ru.mvrlrd.mytranslator.domain.use_cases.tags.*
import ru.mvrlrd.mytranslator.presenter.BaseViewModel

class TagDialogViewModel(
    apiHelper: ApiHelper,
    dbHelper: DbHelper
) : BaseViewModel() {

    private val searchResultRepository = SearchResultIRepository(apiHelper, dbHelper)
    private val tagsLoader: TagsLoader = TagsLoader(searchResultRepository)
    private val addererTagToCard : AddererTagToCard = AddererTagToCard(searchResultRepository)
    private val removerTagFromCard: RemoverTagFromCard = RemoverTagFromCard(searchResultRepository)
    private val newTagAdderer : NewTagAdderer = NewTagAdderer(searchResultRepository)
    private val currentCardTagsPicker : CurrentCardTagsPicker = CurrentCardTagsPicker(searchResultRepository)

    private var _tagsOfCurrentCard = MutableLiveData<List<GroupTag>>()
    val liveTagsOfCurrentCard : LiveData<List<GroupTag>> = _tagsOfCurrentCard

    private var _allTagList = MutableLiveData<List<GroupTag>>()
    val liveAllTagList : LiveData<List<GroupTag>> = _allTagList

    init {
        getAllTags()
    }

    fun loadTagToDataBase(tagText: String) {
        val groupTag = GroupTag(0, tagText, false)
        when {
            _allTagList.value.isNullOrEmpty()
                    || !_allTagList.value!!.contains(groupTag) -> {
                viewModelScope.launch {
                    newTagAdderer(tagText)
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
    private fun mapCardForRecycler(allTagsList: List<GroupTag>) {
        _allTagList.value = allTagsList
    }

    fun addTagToCurrentCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
            addererTagToCard(arrayOf(idCard, idTag))
        }
    }

    fun deleteTagFromCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
            removerTagFromCard(arrayOf(idCard,idTag))
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