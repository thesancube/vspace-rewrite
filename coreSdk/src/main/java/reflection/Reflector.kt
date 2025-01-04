package reflection


/**
 * @author alex
 * Created 04/01/25 at 2:43 am
 * Reflector
 */

import android.util.Log
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.*


class Reflector private constructor(private val clazz: Class<*>) {

    companion object {
        private const val TAG = "Reflector"

        fun on(name: String): Reflector = Reflector(findClass(name))

        fun <T> wrap(method: Method?): MethodWrapper<T> = MethodWrapper(method)

        fun <T> wrapStatic(method: Method?): StaticMethodWrapper<T> = StaticMethodWrapper(method)

        fun <T> wrap(field: Field?): FieldWrapper<T> = FieldWrapper(field)

        fun <T> wrap(constructor: Constructor<T>?): ConstructorWrapper<T> = ConstructorWrapper(constructor)

        fun findClass(name: String): Class<*> = try {
            Class.forName(name)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, e.message.orEmpty())
            throw e
        }

        fun getMethod(clazz: Class<*>, name: String, vararg parameterTypes: Class<*>): Method? =
            findMethod(clazz, name, *parameterTypes)

        fun findMethod(clazz: Class<*>, name: String, vararg parameterTypes: Class<*>): Method? {
            checkParameterTypes(*parameterTypes)
            return findMethodNoChecks(clazz, name, *parameterTypes)
        }

        private fun findMethodNoChecks(clazz: Class<*>, name: String, vararg parameterTypes: Class<*>): Method? {
            var currentClass: Class<*>? = clazz
            while (currentClass != null) {
                try {
                    return currentClass.getDeclaredMethod(name, *parameterTypes).apply { isAccessible = true }
                } catch (e: NoSuchMethodException) {
                    try {
                        return HiddenApiBypass.getDeclaredMethod(currentClass, name, *parameterTypes).apply { isAccessible = true }
                    } catch (ignored: Exception) {
                    }
                }
                currentClass = currentClass.superclass
            }
            return null
        }

        fun getField(clazz: Class<*>, name: String): Field? = findField(clazz, name)

        fun findField(clazz: Class<*>, name: String): Field? = findFieldNoChecks(clazz, name)

        private fun findFieldNoChecks(clazz: Class<*>, name: String): Field? {
            var currentClass: Class<*>? = clazz
            while (currentClass != null) {
                try {
                    return currentClass.getDeclaredField(name).apply { isAccessible = true }
                } catch (e: NoSuchFieldException) {
                    try {
                        return findInstanceField(currentClass, name)
                    } catch (ignored: NoSuchFieldException) {
                        try {
                            return findStaticField(currentClass, name)
                        } catch (ignored: NoSuchFieldException) {
                        }
                    }
                }
                currentClass = currentClass.superclass
            }
            return null
        }

        private fun findInstanceField(clazz: Class<*>, name: String): Field {
            val fields = HiddenApiBypass.getInstanceFields(clazz)
            for (field in fields) {
                if (field is Field && field.name == name) {
                    field.isAccessible = true
                    return field
                }
            }
            throw NoSuchFieldException("Field '$name' not found in class '${clazz.name}'")
        }


        private fun findStaticField(clazz: Class<*>, name: String): Field {
            val fields = HiddenApiBypass.getStaticFields(clazz)
            for (field in fields) {
                if (field is Field && field.name == name) {
                    field.isAccessible = true
                    return field
                }
            }
            throw NoSuchFieldException("Static field '$name' not found in class '${clazz.name}'")
        }


        fun <T> getConstructor(clazz: Class<*>, vararg parameterTypes: Class<*>): Constructor<T>? =
            findConstructor(clazz, *parameterTypes)

        fun <T> findConstructor(clazz: Class<*>, vararg parameterTypes: Class<*>): Constructor<T>? {
            checkParameterTypes(*parameterTypes)
            return findConstructorNoChecks(clazz, *parameterTypes)
        }

        private fun <T> findConstructorNoChecks(clazz: Class<*>, vararg parameterTypes: Class<*>): Constructor<T>? = try {
            clazz.getDeclaredConstructor(*parameterTypes).apply { isAccessible = true } as Constructor<T>
        } catch (e: NoSuchMethodException) {
            try {
                HiddenApiBypass.getDeclaredConstructor(clazz, *parameterTypes).apply { isAccessible = true } as Constructor<T>
            } catch (ignored: Exception) {
                null
            }
        }

        private fun checkParameterTypes(vararg parameterTypes: Class<*>) {
            parameterTypes.forEachIndexed { index, type ->
                requireNotNull(type) { "parameterTypes[$index] == null" }
            }
        }
    }

    fun <T> method(name: String, vararg parameterTypes: Class<*>): MethodWrapper<T> =
        wrap(getMethod(clazz, name, *parameterTypes))

    fun <T> staticMethod(name: String, vararg parameterTypes: Class<*>): StaticMethodWrapper<T> =
        wrapStatic(getMethod(clazz, name, *parameterTypes))

    fun <T> field(name: String): FieldWrapper<T> = wrap(getField(clazz, name))

    fun <T> constructor(vararg parameterTypes: Class<*>): ConstructorWrapper<T> =
        wrap(getConstructor(clazz, *parameterTypes))

    open class MemberWrapper<M : AccessibleObject>(protected val member: M?) {
        init {
            member?.isAccessible = true
        }
    }

    class MethodWrapper<T>(method: Method?) : MemberWrapper<Method>(method) {
        fun call(instance: Any?, vararg args: Any?): T? = try {
            @Suppress("UNCHECKED_CAST")
            member?.invoke(instance, *args) as T?
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    class StaticMethodWrapper<T>(method: Method?) : MemberWrapper<Method>(method) {
        fun call(vararg args: Any?): T? = try {
            @Suppress("UNCHECKED_CAST")
            member?.invoke(null, *args) as T?
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

    class FieldWrapper<T>(field: Field?) : MemberWrapper<Field>(field) {
        fun get(instance: Any?): T? = try {
            @Suppress("UNCHECKED_CAST")
            member?.get(instance) as T?
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }

        fun set(instance: Any?, value: Any?) {
            try {
                member?.set(instance, value)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    class ConstructorWrapper<T>(constructor: Constructor<T>?) : MemberWrapper<Constructor<T>>(constructor) {
        fun newInstance(vararg args: Any?): T? = try {
            member?.newInstance(*args)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
}