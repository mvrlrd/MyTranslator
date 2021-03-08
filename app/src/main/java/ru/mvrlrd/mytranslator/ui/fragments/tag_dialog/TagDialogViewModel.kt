package ru.mvrlrd.mytranslator.ui.fragments.tag_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.data.local.HistoryDao
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardTagCrossRef

class TagDialogViewModel(
    private val historyDao: HistoryDao
) : ViewModel() {
    var currentId: Long = 0
    var _tagsOfCurrentCard = MutableLiveData<List<GroupTag>>()
    val liveTagsOfCurrentCard : LiveData<List<GroupTag>> = _tagsOfCurrentCard

    var _allTagList = MutableLiveData<List<GroupTag>>()
    val liveAllTagList : LiveData<List<GroupTag>> = _allTagList


        fun loadTagToDataBase(tagText : String){
            val groupTag = GroupTag(0,tagText, false)
            viewModelScope.launch {
                for (i in historyDao.getAllTags()){
                    if (i.equals(groupTag)){
                        _allTagList.value = historyDao.getAllTags()
                        cancel()
                    }
                }
                historyDao.insertTag(GroupTag(0,tagText, false))
                _allTagList.value = historyDao.getAllTags()
            }
    }

    fun addTagToCurrentCard(idCard: Long, idTag:Long){
        viewModelScope.launch {
            historyDao.insertCardTagCrossRef(CardTagCrossRef(idCard,idTag))
        }
    }

    fun getAllTagsForCurrentCard(currentCardId : Long){
        viewModelScope.launch {
            historyDao.getTagsOfCard(currentCardId).tags.let {_tagsOfCurrentCard.value = it}
        }
    }


}