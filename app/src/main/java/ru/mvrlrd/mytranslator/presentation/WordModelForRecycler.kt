package ru.mvrlrd.mytranslator.presentation

import ru.mvrlrd.mytranslator.data.network.response.MeaningsResponse
import java.io.Serializable

data class WordModelForRecycler(
    val meanings: List<MeaningModelForRecycler>
):Serializable