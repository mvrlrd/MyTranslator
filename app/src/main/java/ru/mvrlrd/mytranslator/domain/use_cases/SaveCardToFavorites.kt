package ru.mvrlrd.mytranslator.domain.use_cases

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.functional.Either

class SaveCardToFavorites(private val searchResultRepository: IRepository) :
    UseCase<Long, CardOfWord>() {

    override suspend fun run(params: CardOfWord): Either<Failure, Long> {
       return searchResultRepository.saveCardToDb(params)
    }
}