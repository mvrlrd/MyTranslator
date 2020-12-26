package ru.mvrlrd.mytranslator.data.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TranslationResponse(@Expose@field:SerializedName("text") val translation: String?)