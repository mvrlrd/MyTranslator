package ru.mvrlrd.mytranslator.domain.use_cases.tags

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.GroupTag
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class TagsLoader(private val searchResultRepository: IRepository) :
    UseCase<List<GroupTag>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<GroupTag>> {
        return searchResultRepository.getAllTags()
    }
}