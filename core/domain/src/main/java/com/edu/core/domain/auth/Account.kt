package com.edu.core.domain.auth

data class Account(
    val id: Int,
    val name: String,
    val username: String,
    val includeAdult: Boolean,
    val language: String,
    val country: String,
    val avatarPath: String?
)