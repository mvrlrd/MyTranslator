package ru.mvrlrd.mytranslator.domain.use_cases.loaders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.mvrlrd.mytranslator.Failure
import ru.mvrlrd.mytranslator.functional.Either

abstract class MyUseCase<out Type, in Params> where Type : Any? {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job = GlobalScope.async(Dispatchers.IO) { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}