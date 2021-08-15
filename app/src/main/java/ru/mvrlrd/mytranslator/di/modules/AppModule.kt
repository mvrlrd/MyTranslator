package ru.mvrlrd.mytranslator.di.modules

import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.data.LocalIRepository
import ru.mvrlrd.mytranslator.data.RemoteIRepository
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.data.local.LocalDataSource
import ru.mvrlrd.mytranslator.domain.ILocalRepository
import ru.mvrlrd.mytranslator.domain.IRepository
import ru.mvrlrd.mytranslator.domain.use_cases.binders.BinderCardToCategory
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCardToDb
import ru.mvrlrd.mytranslator.domain.use_cases.inserters.InserterCategoryToBd
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.GetterAllCatsFlow
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderCardsOfCategory
import ru.mvrlrd.mytranslator.domain.use_cases.loaders.LoaderChosenCategoriesForLearning
import ru.mvrlrd.mytranslator.domain.use_cases.network.GetSearchResult
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCardFromCategory
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoriesFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.removers.RemoverCategoryFromDb
import ru.mvrlrd.mytranslator.domain.use_cases.update.*
import ru.mvrlrd.mytranslator.ui.fragments.SharedViewModel
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewCategoryDialog
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordViewModel

import ru.mvrlrd.mytranslator.ui.old.old.favorites.FavoritesViewModel

val appSources = module {
    single { NetworkAvailabilityHandler(androidContext()) }
    single <LocalDataSource> { DbHelper(get()) }
//    single { DbHelper(get()) }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppSearchingHistoryDataBase::class.java,
            "all_db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppSearchingHistoryDataBase>().allDatabasesDao() }
    single <ILocalRepository>{ LocalIRepository(get()) }



    single {InserterCategoryToBd (get())}
        single {LoaderCardsOfCategory(get())}
            single {LoaderChosenCategoriesForLearning(get())}
                single {RemoverCategoriesFromDb(get())}
                    single {RemoverCategoryFromDb(get())}
                        single {UpdaterCategoryProgress(get())}
                            single {UpdaterCategoryNameAndIcon(get())}
                                single {UpdaterCategoryIsChecked(get())}
                                    single {UpdaterAllCategoriesToUnselect(get())}
                                        single {GetterAllCatsFlow(get())}
    single {InserterCardToDb(get())}
    single {UpdaterCardProgress(get())}
    single {BinderCardToCategory(get())}
    single {RemoverCardFromCategory(get())}

    single {GetSearchResult(get())}

single <IRepository>{ RemoteIRepository(get()) }
//        single <IRepository>{ get() }
}

val appViewModules = module {
    viewModel { FavoritesViewModel(get()) }
    viewModel { parameters -> SharedViewModel(get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get(),get() ) }

    viewModel { NewWordViewModel(get(),get()) }
}

val appFragments = module {
    single { NewCategoryDialog() }
    single { NewWordDialog() }
    factory { (listener: IconsAdapter.IconAdapterListener) -> IconsAdapter(listener) }
}

val appToolModule = module {
    single { androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
}