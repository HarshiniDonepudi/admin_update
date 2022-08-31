package com.example.admin_app

import com.google.type.DateTime
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

data class lcghnews(
    var title: String? = null,
    var description: String? = null,
    var image: String? = null,
    var date: Date? = null
)
