package com.vcore.rewrite.init


import android.content.Context
import com.vcore.rewrite.sdk.Logger
import com.vcore.rewrite.sdk.LoggerImpl
import com.vcore.rewrite.sdk.vspace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * @author alex
 * Created 29/12/24 at 2:02 am
 * VcoreModule
 */
@Module
@InstallIn(SingletonComponent::class)
object VcoreModule {

    @Provides
    fun provideLogger(): Logger = LoggerImpl()

    // saves the context for later use in other modules or components
    @Provides
    fun provideAppContext( @ApplicationContext context: Context ): Context = vspace.getContext()

}