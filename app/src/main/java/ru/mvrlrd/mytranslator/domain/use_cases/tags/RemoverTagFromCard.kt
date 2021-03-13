package ru.mvrlrd.mytranslator.domain.use_cases.tags

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RemoverTagFromCard(private val searchResultRepository: IRepository) :
    UseCase<Int, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Int> {
        val cardId = params[0]
        val tagId = params[1]
        return searchResultRepository.deleteTagFromCard(cardId, tagId)
    }

}