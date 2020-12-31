package ru.mvrlrd.mytranslator.data.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MeaningsResponse(
    @Expose@field:SerializedName("translation") val translationResponse: TranslationResponse?,
    @Expose@field:SerializedName("imageUrl") val imageUrl: String?,
    @Expose@field:SerializedName("transcription") val transcription: String?,
    @Expose@field:SerializedName("partOfSpeechCode") val partOfSpeech: String?
)