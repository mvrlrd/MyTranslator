package ru.mvrlrd.mytranslator.data

import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.data.local.entity.CardOfWord
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardWithTag
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithWords
import ru.mvrlrd.mytranslator.data.network.RemoteDataSource
import ru.mvrlrd.mytranslator.data.network.response.ListSearchResult
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.functional.Either

class SearchResultIRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {

    //words
            //remote
    override suspend fun getSearchResult(wordForTranslation: String): Either<Failure, ListSearchResult?> {
        return remoteDataSource.getData(wordForTranslation)
    }
            //local
    override suspend fun saveCardToDb(cardOfWord: CardOfWord): Either<Failure, Long> {
        return localDataSource.saveCardToDb(cardOfWord)
    }

    override suspend fun deleteCardFromDb(id: Long): Either<Failure, Int> {
        return localDataSource.deleteCardFromDb(id)
    }

    override suspend fun getAllCardsFromDb(): Either<Failure, List<CardOfWord>> {
        return localDataSource.getAllCardsFromDb()
    }

    //category

    override suspend fun addNewTagToDb(category: Category): Either<Failure, Long> {
        return localDataSource.insertNewTagToDb(category)
    }

    override suspend fun deleteCategory(categoryId: Long): Either<Failure, Int> {
        return localDataSource.deleteCategory(categoryId)
    }

    override suspend fun clearCategoriesFromDb(): Either<Failure, Int> {
        return localDataSource.clearCategories()
    }
    override suspend fun getAllCategoriesForLearning(): Either<Failure, List<Category>> {
        return localDataSource.getAllCategoriesForLearning()
    }
    override suspend fun getAllTags(): Either<Failure, List<Category>> {
        return localDataSource.getAllTags()
    }

    //crossref
    override suspend fun assignTagToCard(cardId: Long, tagId: Long): Either<Failure, Long> {
        return localDataSource.assignTagToCard(cardId, tagId)
    }

    override suspend fun deleteTagFromCard(cardId: Long, tagId: Long): Either<Failure, Int> {
        return localDataSource.deleteTagFromCard(cardId, tagId)
    }

    override suspend fun getCardsOfCategory(categoryId: Long): Either<Failure, CategoryWithWords> {
        return localDataSource.getCardsOfCategory(categoryId)
    }

    //garbage
    override suspend fun getTagsOfCurrentCard(cardId: Long): Either<Failure, CardWithTag> {
        return localDataSource.getTagsOfCurrentCard(cardId)
    }
}