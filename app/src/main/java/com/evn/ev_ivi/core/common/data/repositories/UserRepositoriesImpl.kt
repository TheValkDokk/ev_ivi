package com.evn.ev_ivi.core.common.data.repositories

import com.evn.ev_ivi.core.common.domain.entities.User
import com.evn.ev_ivi.core.common.domain.repositories.UserRepository

class UserRepositoryImpl : UserRepository {
    private val _users = arrayListOf<User>()

    override fun findUser(name: String): User? {
        return _users.firstOrNull { it.name == name }
    }

    override fun addUsers(users : List<User>) {
        _users.addAll(users)
    }
}