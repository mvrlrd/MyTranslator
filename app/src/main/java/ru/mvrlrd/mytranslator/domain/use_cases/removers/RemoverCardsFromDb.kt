package ru.mvrlrd.mytranslator.domain.use_cases.removers

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RemoverCardsFromDb(private val searchResultRepository: ILocalRepository) :
    UseCase<Int, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, Int> {
        return searchResultRepository.clearAllCardsFromDb()
    }
}