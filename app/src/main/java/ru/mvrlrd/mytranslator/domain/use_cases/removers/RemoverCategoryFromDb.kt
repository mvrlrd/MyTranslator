package ru.mvrlrd.mytranslator.domain.use_cases.removers

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RemoverCategoryFromDb(private val searchResultRepository: IRepository) :
    UseCase<Int, Long>() {

    override suspend fun run(params: Long): Either<Failure, Int> {
        return searchResultRepository.deleteCategory(params)
    }
}