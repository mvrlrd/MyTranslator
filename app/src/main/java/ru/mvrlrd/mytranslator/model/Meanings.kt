package ru.mvrlrd.mytranslator.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meanings(
    @Expose@field:SerializedName("translation") val translation: Translation?,
    @field:SerializedName("imageUrl") val imageUrl: String?
)