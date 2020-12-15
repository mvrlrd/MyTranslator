package ru.mvrlrd.mytranslator.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meanings(
    @Expose@field:SerializedName("translation") val translation: Translation?,
    @Expose@field:SerializedName("imageUrl") val imageUrl: String?
)