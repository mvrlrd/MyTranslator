package ru.mvrlrd.mytranslator.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.model.SearchResult
import ru.mvrlrd.mytranslator.model.datasource.retrofit.ApiHelper

import ru.mvrlrd.mytranslator.room.HistoryDao
import ru.mvrlrd.mytranslator.room.HistoryEntity


class MainViewModel (
    val apiHelper: ApiHelper,
    val historyDao: HistoryDao): ViewModel() {
    var liveTranslations: MutableLiveData<List<SearchResult>> = MutableLiveData()
    var liveHistory : MutableLiveData<List<HistoryEntity>> = MutableLiveData()

    fun loadData(word: String) {
        viewModelScope.launch {
            val response = apiHelper.getData(word)
            if (response.isSuccessful
                && response.body()  != null
            ) {
                val data = response.body()
                data.let { tr ->
                    liveTranslations.value = tr

                    println(
                    historyDao.insert(HistoryEntity( tr?.get(0)?.text,
                        tr?.get(0)?.meanings?.get(0)?.translation?.translation)).toString()+"     id has been added")
                }
            }
        }
    }

     fun loadHistory(){
        viewModelScope.launch {
            liveHistory.value = historyDao.getAll()
        }

    }

    companion object {
        const val TAG = "MAIN_PRESENTER"
    }
}