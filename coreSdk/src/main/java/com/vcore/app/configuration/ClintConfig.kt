package com.vcore.app.configuration

import java.io.File

interface ClintConfig  {
    fun isHideRoot(): Boolean
    fun isHideXposed(): Boolean
    fun getHostPackageName(): String
    fun requestInstallPackage(file: File, userId: Int): Boolean




}
