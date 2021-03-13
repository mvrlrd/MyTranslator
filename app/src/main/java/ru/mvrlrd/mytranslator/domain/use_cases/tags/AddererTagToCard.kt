package ru.mvrlrd.mytranslator.domain.use_cases.tags

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class AddererTagToCard(private val searchResultRepository: IRepository) :
    UseCase<Map<Long, Long>, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Map<Long,Long>> {
        val cardId = params[0]
        val tagId = params[1]
            return searchResultRepository.assignTagToCard(cardId, tagId)
    }

}