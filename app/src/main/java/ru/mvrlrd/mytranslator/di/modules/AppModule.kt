package ru.mvrlrd.mytranslator.di.modules

import android.content.Context
import android.os.Vibrator
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.mytranslator.NetworkAvailabilityHandler
import ru.mvrlrd.mytranslator.data.SearchResultIRepository
import ru.mvrlrd.mytranslator.ui.fragments.translation.TranslationViewModel
import ru.mvrlrd.mytranslator.data.local.AppSearchingHistoryDataBase
import ru.mvrlrd.mytranslator.data.local.DbHelper
import ru.mvrlrd.mytranslator.ui.fragments.OnItemClickListener
import ru.mvrlrd.mytranslator.ui.fragments.categories.CategoriesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.AddingCategoryFragment
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler.IconsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.WordsInCategoryViewModel
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.adding_word.AddNewWordDialogFragment
import ru.mvrlrd.mytranslator.ui.fragments.categories.add_words_to_category.recycler.WordsAdapter
import ru.mvrlrd.mytranslator.ui.fragments.categories.recycler.CategoriesAdapter
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogFragment
import ru.mvrlrd.mytranslator.ui.fragments.favorites.FavoritesViewModel
import ru.mvrlrd.mytranslator.ui.fragments.tag_dialog.TagDialogViewModel

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
    single { SearchResultIRepository(get(),get()) }
}

val appViewModules = module{
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { CategoriesViewModel(get(),get()) }
    single { WordsInCategoryViewModel(get(),get()) }

    single { TagDialogViewModel(get(), get()) }
    single { TranslationViewModel(get(), get()) }

}

val appFragments = module {
    single { AddingCategoryFragment() }
    single { AddNewWordDialogFragment() }
    single { TagDialogFragment() }

    factory { (listener: IconsAdapter.IconAdapterListener)-> IconsAdapter(listener) }
    factory { (listener: OnItemClickListener)->  CategoriesAdapter(listener)}
    single { WordsAdapter() }
}

val appToolModule = module {
    single { androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
}