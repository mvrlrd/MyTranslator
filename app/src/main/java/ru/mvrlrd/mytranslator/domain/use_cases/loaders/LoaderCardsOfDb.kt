package ru.mvrlrd.mytranslator.domain.use_cases.loaders

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class LoaderCardsOfDb(private val searchResultRepository: ILocalRepository) :
    UseCase<List<Card>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<Card>> {
        return searchResultRepository.getAllCardsFromDb()
    }
}