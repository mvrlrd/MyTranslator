package ru.mvrlrd.mytranslator.domain.use_cases.binders

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class BinderCardToCategory(private val searchResultRepository: IRepository) :
    UseCase<Long, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Long> {
        val cardId = params[0]
        val tagId = params[1]
        return searchResultRepository.assignCardToCategory(cardId, tagId)
    }
}