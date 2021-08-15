package ru.mvrlrd.mytranslator.domain.use_cases.loaders

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class LoaderChosenCategoriesForLearning(private val localRepository: ILocalRepository) :
    UseCase<List<Category>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<Category>> {
        return localRepository.getAllCategoriesForLearning()
    }
}