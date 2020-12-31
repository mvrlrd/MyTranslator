package ru.mvrlrd.mytranslator.data.network.response

import android.util.Log

class ListSearchResult : ArrayList<SearchResultResponse>()

{

    fun printAllSearchResultResponse() {
        println(this.toString())
        for (i in this){
            i.text?.let {
                for (j in i.meanings!!) {
                    println("text = ${i.text}   transl = ${j.translationResponse?.translation}  part = ${j.partOfSpeech}  transcrp = ${j.prefix}")
                }
            }

        }
    }


}
