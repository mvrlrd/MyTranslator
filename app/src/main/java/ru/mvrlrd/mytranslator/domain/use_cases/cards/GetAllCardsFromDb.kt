package ru.mvrlrd.mytranslator.domain.use_cases.cards

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class GetAllCardsFromDb(private val searchResultRepository: IRepository) :
    UseCase<List<CardOfWord>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<CardOfWord>> {
        return searchResultRepository.getAllCardsFromDb()
    }
}