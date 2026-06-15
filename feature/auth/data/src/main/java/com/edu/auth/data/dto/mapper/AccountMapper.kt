package com.edu.auth.data.dto.mapper

import com.edu.auth.data.dto.response.AccountResponse
import com.edu.core.domain.auth.Account

fun AccountResponse.toAccount() : Account{
    return Account(
        id = id,
        name = name,
        username = username,
        includeAdult = includeAdult,
        language = iso6391,
        country = iso31661,
        avatarPath = avatar.tmdb.avatarPath
    )
}