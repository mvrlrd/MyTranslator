package ru.mvrlrd.mytranslator.presenter

import dagger.Component
import ru.mvrlrd.mytranslator.presenter.modules.ApiHelperModule
import ru.mvrlrd.mytranslator.presenter.modules.AppModule
import ru.mvrlrd.mytranslator.presenter.modules.PresenterModule
import ru.mvrlrd.mytranslator.view.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [PresenterModule::class, AppModule::class, ApiHelperModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}