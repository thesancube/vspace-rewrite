package com.vcore.rewrite.init

import com.vcore.rewrite.sdk.Logger
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author alex
 * Created 29/12/24 at 2:01 am
 * VcoreEntrypoint
 */

@EntryPoint
@InstallIn(SingletonComponent::class)
interface VcoreEntrypoint {
    fun provideLogger(): Logger


}