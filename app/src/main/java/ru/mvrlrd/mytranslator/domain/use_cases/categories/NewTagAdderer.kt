package ru.mvrlrd.mytranslator.domain.use_cases.categories

import android.util.Log
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class NewTagAdderer(private val searchResultRepository: IRepository) :
    UseCase<Long, Category>() {

    override suspend fun run(category: Category): Either<Failure, Long> {
        return searchResultRepository.addNewTagToDb(category)
    }
}