package ru.mvrlrd.mytranslator.presentation

import java.io.Serializable

data class MeaningModelForRecycler (
    val text : String?,
    val translation : String?,
    val image_url: String?
) : Serializable