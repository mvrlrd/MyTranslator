package ru.mvrlrd.mytranslator.di.modules

import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.ui.old.old.translation.TranslationViewModel
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.categories.CategoriesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordDialog
import ru.mvrlrd.mytranslator.ui.fragments.adapters.WordsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.adapters.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.adapters.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewCategoryDialog
import ru.mvrlrd.mytranslator.ui.fragments.dialog_fragments.NewWordViewModel
import ru.mvrlrd.mytranslator.ui.fragments.learning.LearningViewModel
import ru.mvrlrd.mytranslator.ui.fragments.words.WordsListViewModel
import ru.mvrlrd.mytranslator.ui.old.old.tag_dialog.TagDialogFragment
import ru.mvrlrd.mytranslator.ui.old.old.favorites.FavoritesViewModel
import ru.mvrlrd.mytranslator.ui.old.old.tag_dialog.TagDialogViewModel

val appSources = module {
    single { NetworkAvailabilityHandler(androidContext()) }
    single { DbHelper(get()) }
    single { Room.databaseBuilder(
            androidContext(),
            AppSearchingHistoryDataBase::class.java,
            "history_database"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppSearchingHistoryDataBase>().historyDao() }
    single { SearchResultIRepository(get(), get()) }
}

val appViewModules = module {
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { CategoriesViewModel(get(), get()) }
    viewModel { LearningViewModel(get(),get()) }
    viewModel { WordsListViewModel(get(), get()) }
    viewModel { NewWordViewModel(get(), get()) }
    single { TagDialogViewModel(get(), get()) }
    single { TranslationViewModel(get(), get()) }

}

val appFragments = module {
    single { NewCategoryDialog() }
    single { NewWordDialog() }
    single { TagDialogFragment() }

    factory { (listener: IconsAdapter.IconAdapterListener)-> IconsAdapter(listener) }
//    factory { (listener: OnItemClickListener)->  CategoriesAdapter(listener) }
//    single { WordsAdapter() }
}

val appToolModule = module {
    single { androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
}