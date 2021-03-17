package ru.mvrlrd.mytranslator.domain.use_cases.tags

import android.util.Log
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class TagsLoader(private val searchResultRepository: IRepository) :
    UseCase<List<Category>, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, List<Category>> {
        Log.e("TagLoader", "i am in run here too")
        return searchResultRepository.getAllTags()
    }
}