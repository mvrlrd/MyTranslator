package ru.mvrlrd.mytranslator.domain.use_cases.cards

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class AddererWordToCategory(private val searchResultRepository: IRepository) :
    UseCase< Long, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Long> {
        val cardId = params[0]
        val tagId = params[1]
            return searchResultRepository.assignTagToCard(cardId, tagId)
    }

}