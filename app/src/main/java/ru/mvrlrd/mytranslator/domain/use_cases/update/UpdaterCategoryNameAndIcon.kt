package ru.mvrlrd.mytranslator.domain.use_cases.update

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class UpdaterCategoryNameAndIcon (private val localIRepository: ILocalRepository) :
    UseCase<Int, Category>() {

    override suspend fun run(category: Category): Either<Failure, Int> {
        return localIRepository.updateCategory(
            category.categoryId,
            category.name,
            category.icon
        )
    }
}