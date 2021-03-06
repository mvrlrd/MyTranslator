package ru.mvrlrd.mytranslator.domain.use_cases.categories

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class ClearCategories(private val searchResultRepository: IRepository) :
    UseCase<Int, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, Int> {
        return searchResultRepository.clearCategoriesFromDb()
    }
}