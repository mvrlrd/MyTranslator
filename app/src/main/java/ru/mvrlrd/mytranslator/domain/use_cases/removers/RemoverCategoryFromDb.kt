package ru.mvrlrd.mytranslator.domain.use_cases.removers

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class RemoverCategoryFromDb(private val searchResultRepository: ILocalRepository) :
    UseCase<Int, Long>() {

    override suspend fun run(params: Long): Either<Failure, Int> {
        return searchResultRepository.deleteCategoryFromDb(params)
    }
}