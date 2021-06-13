package ru.mvrlrd.mytranslator.domain.use_cases.loaders

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class LoaderCardsOfCategory(private val searchResultRepository: ILocalRepository) :
    UseCase<CategoryWithCards, Long>() {

    override suspend fun run(params: Long): Either<Failure, CategoryWithCards> {
        return searchResultRepository.getCardsOfCategory(params)
    }
}