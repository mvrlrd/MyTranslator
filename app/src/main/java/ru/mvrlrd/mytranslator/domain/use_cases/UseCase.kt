package ru.mvrlrd.mytranslator.domain.use_cases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.functional.Either

abstract class UseCase<out Type, in String> where Type : Any? {

    abstract suspend fun run(wordForTranslation: String): Either<Failure, Type>

    operator fun invoke(wordForTranslation: String, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) { run(wordForTranslation) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}