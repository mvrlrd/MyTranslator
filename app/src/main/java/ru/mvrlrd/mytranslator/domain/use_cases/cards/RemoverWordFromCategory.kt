package ru.mvrlrd.mytranslator.domain.use_cases.cards

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RemoverWordFromCategory(private val searchResultRepository: IRepository) :
    UseCase<Int, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Int> {
        val wordId = params[0]
        val categoryId = params[1]
        return searchResultRepository.deleteTagFromCard(wordId, categoryId)
    }

}