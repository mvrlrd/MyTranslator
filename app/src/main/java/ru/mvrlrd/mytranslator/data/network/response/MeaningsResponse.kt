package ru.mvrlrd.mytranslator.data.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MeaningsResponse(
    @Expose@field:SerializedName("translation") val translationResponse: TranslationResponse?,
    @Expose val imageUrl: String?,
    @Expose val transcription: String?,
    @Expose@field:SerializedName("partOfSpeechCode") val partOfSpeech: String?,
    @Expose val prefix: String?
)