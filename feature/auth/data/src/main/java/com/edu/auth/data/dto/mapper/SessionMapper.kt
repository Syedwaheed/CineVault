package com.edu.auth.data.dto.mapper

import com.edu.auth.data.dto.session.SessionDto
import com.edu.core.domain.auth.Session

fun SessionDto.toSession(): Session{
    return Session(
        sessionId = sessionId,
        accountId = accountId,
        username = username
    )
}

fun Session.toSessionDto(): SessionDto{
    return SessionDto(
        sessionId = sessionId,
        accountId = accountId,
        username = username
    )
}

