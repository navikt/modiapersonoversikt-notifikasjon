package no.nav.modiapersonoversikt

import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

val localDateTimeSerializer: JsonSerializer<LocalDateTime> = JsonSerializer { src, _, _ ->
    if (src == null) null else JsonPrimitive(src.format(DATE_FORMATTER))
}

val localDateTimeDeserializer: JsonDeserializer<LocalDateTime> = JsonDeserializer<LocalDateTime> { json, _, _ ->
    if (json == null) null else LocalDateTime.parse(json.asString, DATE_FORMATTER)
}