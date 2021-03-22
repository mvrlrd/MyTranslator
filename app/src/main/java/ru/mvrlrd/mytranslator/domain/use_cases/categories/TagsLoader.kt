package ru.mvrlrd.mytranslator.domain.use_cases.categories

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class TagsLoader(private val searchResultRepository: IRepository) :
    UseCase<List<Category>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<Category>> {
        return searchResultRepository.getAllTags()
    }
}