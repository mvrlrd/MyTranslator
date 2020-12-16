package ru.mvrlrd.mytranslator.data.response

class ListSearchResult : ArrayList<SearchResultResponse>(){



    fun printAllMeanings(){
        for (i in this){
           for (j in i.meanings!!){
               println("${i.text}   ${j.translationResponse?.translation}")
           }
        }

    }
}

