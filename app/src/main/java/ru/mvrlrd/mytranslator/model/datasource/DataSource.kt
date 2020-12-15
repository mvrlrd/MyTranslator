package ru.mvrlrd.mytranslator.model.datasource

import retrofit2.Response

interface DataSource<T> {
    suspend fun getData(word: String): Response<T>
}