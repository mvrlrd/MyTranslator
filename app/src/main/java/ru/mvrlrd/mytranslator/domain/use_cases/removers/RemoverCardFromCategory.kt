package ru.mvrlrd.mytranslator.domain.use_cases.removers

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RemoverCardFromCategory(private val searchResultRepository: ILocalRepository) :
    UseCase<Int, Array<Long>>() {

    override suspend fun run(params: Array<Long>): Either<Failure, Int> {
        val wordId = params[0]
        val categoryId = params[1]
        return searchResultRepository.deleteCardFromCategory(wordId, categoryId)
    }
}