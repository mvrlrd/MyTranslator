package ru.mvrlrd.mytranslator.presentation

import java.io.Serializable

data class MeaningModelForRecycler (
    val translation : String?,
    val image_url: String?,
    val transcription: String?
) : Serializable