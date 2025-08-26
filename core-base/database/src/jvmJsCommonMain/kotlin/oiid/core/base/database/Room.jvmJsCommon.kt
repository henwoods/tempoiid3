package oiid.core.base.database

import kotlin.reflect.KClass

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
actual annotation class Dao actual constructor()

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER])
@Retention(value = AnnotationRetention.BINARY)
actual annotation class Query actual constructor(actual val value: String)

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
@Target(allowedTargets = [AnnotationTarget.FUNCTION])
@Retention(value = AnnotationRetention.BINARY)
actual annotation class Insert actual constructor(
    actual val entity: KClass<*>,
    actual val onConflict: Int,
)

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
@Target(allowedTargets = [AnnotationTarget.FIELD, AnnotationTarget.FUNCTION])
@Retention(value = AnnotationRetention.BINARY)
actual annotation class PrimaryKey actual constructor(actual val autoGenerate: Boolean)

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
@Target(allowedTargets = [])
@Retention(value = AnnotationRetention.BINARY)
actual annotation class ForeignKey

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
@Target(allowedTargets = [])
@Retention(value = AnnotationRetention.BINARY)
actual annotation class Index

@Suppress(names = ["NO_ACTUAL_FOR_EXPECT"])
@Target(allowedTargets = [AnnotationTarget.CLASS])
@Retention(value = AnnotationRetention.BINARY)
actual annotation class Entity actual constructor(
    actual val tableName: String,
    actual val indices: Array<Index>,
    actual val inheritSuperIndices: Boolean,
    actual val primaryKeys: Array<String>,
    actual val foreignKeys: Array<ForeignKey>,
    actual val ignoredColumns: Array<String>,
)