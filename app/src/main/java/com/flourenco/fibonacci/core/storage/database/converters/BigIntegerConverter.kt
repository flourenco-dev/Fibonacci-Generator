package com.flourenco.fibonacci.core.storage.database.converters

import androidx.room.TypeConverter
import java.math.BigInteger

class BigIntegerConverter {

    @TypeConverter
    fun fromBigInteger(bigInteger: BigInteger?): String? = bigInteger?.toString()

    @TypeConverter
    fun toBigInteger (value: String?): BigInteger? = value?.let { BigInteger(it) }
}