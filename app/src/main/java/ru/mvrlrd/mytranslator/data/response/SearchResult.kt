package ru.mvrlrd.mytranslator.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResult(
    @Expose@field:SerializedName("text") val text: String?,
    @Expose@field:SerializedName("meanings") val meanings: List<Meanings>?
)
