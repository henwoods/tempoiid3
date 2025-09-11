package oiid.core.base.database

/**
 * Cross-platform annotation for marking methods as database type converters.
 *
 * Type converters enable Room to store and retrieve complex data types that are not
 * natively supported by SQLite. This annotation marks methods that convert between
 * custom types and primitive types that SQLite can understand.
 *
 * Type converters must be static methods (or methods in an object class) and should
 * come in pairs: one method to convert from the custom type to a primitive type,
 * and another to convert back from the primitive type to the custom type.
 *
 * Example usage:
 * ```kotlin
 * object DateConverters {
 *     @TypeConverter
 *     fun fromTimestamp(value: Long?): Date? {
 *         return value?.let { Date(it) }
 *     }
 *
 *     @TypeConverter
 *     fun dateToTimestamp(date: Date?): Long? {
 *         return date?.time
 *     }
 * }
 *
 * // Register converters in your database
 * @Database(...)
 * @TypeConverters(DateConverters::class)
 * abstract class MyDatabase : RoomDatabase() {
 *     // Database implementation
 * }
 * ```
 *
 * Common use cases for type converters include:
 * - Converting Date objects to Long timestamps
 * - Converting enums to String or Int values
 * - Converting complex objects to JSON strings
 * - Converting lists or arrays to comma-separated strings
 *
 * Performance considerations:
 * - Type converters are called frequently during database operations
 * - Keep conversion logic simple and efficient
 * - Consider caching expensive conversions when appropriate
 * - Avoid complex object creation in frequently-called converters
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
expect annotation class TypeConverter()
