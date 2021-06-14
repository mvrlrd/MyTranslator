package ru.mvrlrd.mytranslator.ui.fragments.learning

import ru.mvrlrd.mytranslator.data.local.entity.Card

//handle some processes to connect CardStackAdapter to LearningFragment
interface LearningProcess {
    fun finishLearningProcess()
    fun updateCard(card: Card)
}