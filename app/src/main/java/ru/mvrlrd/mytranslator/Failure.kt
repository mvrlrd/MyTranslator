package ru.mvrlrd.mytranslator

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    abstract class AppFailure : Failure()
}