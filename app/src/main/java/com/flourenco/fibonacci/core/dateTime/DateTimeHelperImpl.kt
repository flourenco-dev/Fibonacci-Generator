package com.flourenco.fibonacci.core.dateTime

import javax.inject.Inject
import org.threeten.bp.LocalDateTime

class DateTimeHelperImpl @Inject constructor(): DateTimeHelper {
    override fun getCurrentDateTime(): LocalDateTime = LocalDateTime.now()
}