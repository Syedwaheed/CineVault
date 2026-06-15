package com.edu.feature.profile.data.mapper

import com.edu.core.database.entity.UserEntity
import com.edu.core.domain.auth.Account
import com.edu.feature.profile.data.dto.ProfileAccountResponse

internal fun ProfileAccountResponse.toAccount(): Account = Account(
    id = id,
    name = name,
    username = username,
    includeAdult = includeAdult,
    language = language,
    country = country,
    avatarPath = avatar.tmdb.avatarPath,
)

internal fun Account.toUserEntity(): UserEntity = UserEntity(
    tmdbAccountId = id,
    username = username,
    name = name,
    avatarPath = avatarPath,
    includeAdult = includeAdult,
    region = country,
)

internal fun UserEntity.toAccount(): Account = Account(
    id = tmdbAccountId,
    name = name,
    username = username,
    includeAdult = includeAdult,
    language = "",
    country = region,
    avatarPath = avatarPath,
)
