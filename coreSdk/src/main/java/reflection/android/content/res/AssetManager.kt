package reflection.android.content.res

import reflection.Reflector

/**
 * @author alex
 * Created 04/01/25 at 4:34 am
 * AssetManager
 */
class AssetManager {
    companion object {
        val REF: Reflector = Reflector.on("android.content.res.AssetManager")

        val _new: Reflector.ConstructorWrapper<android.content.res.AssetManager?> = REF.constructor()
        val addAssetPath: Reflector.MethodWrapper<Int?> =
            REF.method("addAssetPath", String::class.java)
    }
}