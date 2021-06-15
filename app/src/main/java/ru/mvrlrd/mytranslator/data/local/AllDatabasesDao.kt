package ru.mvrlrd.mytranslator.data.local

import androidx.room.*
import ru.mvrlrd.mytranslator.data.local.entity.Category
import ru.mvrlrd.mytranslator.data.local.entity.Card
import ru.mvrlrd.mytranslator.data.local.entity.relations.CardCategoryCrossRef
import ru.mvrlrd.mytranslator.data.local.entity.relations.CategoryWithCards

@Dao
interface AllDatabasesDao {
    //cards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card?): Long

    @Query("DELETE FROM cards_db")
    suspend fun clearCards(): Int

    @Query("DELETE FROM cards_db WHERE id =:id")
    suspend fun deleteCard(id: Long): Int

    @Query("SELECT * FROM cards_db")
    suspend fun getAllCards(): List<Card>

    @Query("SELECT * FROM cards_db WHERE word=:word")
    suspend fun getCard(word: String): Card

    @Query("UPDATE cards_db SET progress=:newProgress WHERE id =:cardId")
    suspend fun updateCardProgress(cardId: Long, newProgress: Int): Int

    //categories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Query("DELETE FROM categories_db")
    suspend fun clearCategories(): Int

    @Query("SELECT * FROM categories_db")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories_db WHERE isChecked")
    suspend fun getCategoriesForLearning(): List<Category>

    @Query("DELETE FROM categories_db WHERE categoryId =:catId")
    suspend fun deleteCategory(catId: Long): Int

    @Query("UPDATE categories_db SET averageProgress=:newProgress WHERE categoryId =:catId")
    suspend fun updateCategoryProgress(catId: Long, newProgress: Double): Int

    @Query("UPDATE categories_db SET name=:newName, icon=:newIcon  WHERE categoryId =:catId")
    suspend fun updateCategory(catId: Long, newName: String, newIcon: String): Int


    // crossref
    @Transaction
    @Query("SELECT * FROM categories_db WHERE categoryId = :categoryId")
    suspend fun getCardsOfCategory(categoryId: Long): CategoryWithCards

    @Query("DELETE  FROM cardcategorycrossref WHERE id =:id AND categoryId =:categoryId")
    suspend fun removeCardFromCategory(id: Long, categoryId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardToCategory(cardCategoryCrossRef: CardCategoryCrossRef): Long

    @Query("SELECT * FROM cardcategorycrossref")
    suspend fun getAllCrossRef(): List<CardCategoryCrossRef>
}