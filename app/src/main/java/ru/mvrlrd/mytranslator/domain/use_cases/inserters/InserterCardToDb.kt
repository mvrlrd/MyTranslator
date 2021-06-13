package ru.mvrlrd.mytranslator.domain.use_cases.inserters

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class InserterCardToDb(private val searchResultRepository: ILocalRepository) :
    UseCase<Long, Card>() {

    override suspend fun run(params: Card): Either<Failure, Long> {
        return searchResultRepository.saveCardToDb(params)
    }
}