package ru.mvrlrd.mytranslator.domain.use_cases.cards

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class GetterCardsOfCategory(private val searchResultRepository: IRepository) :
    UseCase<CategoryWithWords, Long>() {

    override suspend fun run(params: Long): Either<Failure, CategoryWithWords> {
        return searchResultRepository.getCardsOfCategory(params)
    }
}