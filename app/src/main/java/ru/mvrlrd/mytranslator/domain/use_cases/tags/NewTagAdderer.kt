package ru.mvrlrd.mytranslator.domain.use_cases.tags

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.UseCase
import ru.mvrlrd.mytranslator.functional.Either

class NewTagAdderer(private val searchResultRepository: IRepository) :
    UseCase<Long, Array<String>>() {

    override suspend fun run(params: Array<String>): Either<Failure, Long> {
        return searchResultRepository.addNewTagToDb(params[0],params[1])
    }
}