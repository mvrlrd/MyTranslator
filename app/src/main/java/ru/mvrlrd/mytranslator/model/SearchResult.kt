package ru.mvrlrd.mytranslator.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResult(
    @field:SerializedName("text") val text: String?,
    @Expose@field:SerializedName("meanings") val meanings: List<Meanings>?
)
