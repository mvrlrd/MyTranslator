package ru.mvrlrd.mytranslator.presentation

import java.io.Serializable

data class FullInfoForRecycler (
    val text: String?,
    val translation : String?,
    val image_url: String?,
    val transcription: String?,
    val partOfSpeech: String?
):Serializable