package ru.mvrlrd.mytranslator.domain.use_cases.inserters

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class InserterCategoryToBd(private val searchResultRepository: ILocalRepository) :
    UseCase<Long, Category>() {

    override suspend fun run(category: Category): Either<Failure, Long> {
        return searchResultRepository.addCategoryToDb(category)
    }
}