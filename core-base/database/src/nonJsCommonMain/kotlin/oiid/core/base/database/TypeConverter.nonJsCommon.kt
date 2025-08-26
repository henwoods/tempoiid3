package oiid.core.base.database

import androidx.room.TypeConverter

/**
 * Type alias for Room's [TypeConverter] annotation in non-JS common code.
 *
 * This type alias maps the Room [TypeConverter] annotation to be used in the non-JS common module.
 * The [TypeConverter] annotation is used to define custom type conversions for Room database entities.
 *
 * @see androidx.room.TypeConverter
 */
actual typealias TypeConverter = TypeConverter
