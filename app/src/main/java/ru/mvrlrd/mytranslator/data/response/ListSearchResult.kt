package ru.mvrlrd.mytranslator.data.response

class ListSearchResult:
ArrayList<SearchResultResponse>(){

//{


    fun printAllMeanings(){
        for (i in this){
           for (j in i.meanings!!){
               println("text = ${i.text}   tr = ${j.translationResponse?.translation}    ${j.imageUrl}")
           }
        }

    }
}

