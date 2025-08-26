package cmp.navigation.di

import cmp.navigation.AppViewModel
import cmp.navigation.SplashViewModel
import cmp.navigation.ui.TabViewModel
import com.oiid.core.common.di.DispatchersModule
import com.oiid.core.data.ui.TabRepository
import com.oiid.core.datastore.di.DatastoreModule
import com.oiid.feature.auth.AuthModule
import com.oiid.feature.auth.OnboardingModule
import com.oiid.feature.events.di.EventsModule
import com.oiid.feature.fanzone.di.FanzoneModule
import com.oiid.feature.feed.di.FeedModule
import com.oiid.feature.imagepick.ImagePickModule
import com.oiid.feature.settings.ProfileModule
import com.oiid.network.NetworkModule
import oiid.core.base.designsystem.AppStateViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object KoinModules {
    private val DataModule = module {
        includes(com.oiid.core.data.di.DataModule)
    }

    private val DispatcherModule = module {
        includes(DispatchersModule)
    }

    private val SplashModule = module {
        viewModelOf(::SplashViewModel)
    }

    private val AppModule = module {
        single<TabRepository> { (TabRepository(context = get())) }
        viewModelOf(::TabViewModel)
        viewModelOf(::AppViewModel)
        single { AppStateViewModel() }
    }

    private val FeatureModule = module {
        includes(
            FeedModule,
            ProfileModule,
            OnboardingModule,
            NetworkModule,
            AuthModule,
            ImagePickModule,
            FanzoneModule,
            EventsModule
        )
    }

    val allModules = listOf(
        DataModule,
        DispatcherModule,
        DatastoreModule,
        FeatureModule,
        AppModule,
        SplashModule
    )
}
