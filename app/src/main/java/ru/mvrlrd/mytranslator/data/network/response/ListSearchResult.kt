package ru.mvrlrd.mytranslator.data.network.response

import android.util.Log

class ListSearchResult : ArrayList<SearchResultResponse>()

{

    fun printAllSearchResultResponse() {
        for (i in this){
            i.text?.let { Log.e("ListRus", it) }
        }
    }


}
