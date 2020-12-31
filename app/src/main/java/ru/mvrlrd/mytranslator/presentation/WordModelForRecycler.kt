package ru.mvrlrd.mytranslator.presentation

import java.io.Serializable

data class WordModelForRecycler(
    val meanings: List<MeaningModelForRecycler>
):Serializable