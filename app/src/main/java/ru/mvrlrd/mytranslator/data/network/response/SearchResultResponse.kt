package ru.mvrlrd.mytranslator.data.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchResultResponse(
    @Expose@field:SerializedName("text") val text: String?,
    @Expose@field:SerializedName("meanings") val meanings: List<MeaningsResponse>?
)