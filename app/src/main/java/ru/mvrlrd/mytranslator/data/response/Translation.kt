package ru.mvrlrd.mytranslator.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Translation(@Expose@field:SerializedName("text") val translation: String?)