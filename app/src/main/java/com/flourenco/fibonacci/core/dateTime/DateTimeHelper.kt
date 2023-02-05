package com.flourenco.fibonacci.core.dateTime

import org.threeten.bp.LocalDateTime

interface DateTimeHelper {
    fun getCurrentDateTime(): LocalDateTime
}