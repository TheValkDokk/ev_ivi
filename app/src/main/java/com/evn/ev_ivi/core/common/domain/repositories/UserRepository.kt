package com.evn.ev_ivi.core.common.domain.repositories

import com.evn.ev_ivi.core.common.domain.entities.User

interface UserRepository {
    fun findUser(name : String): User?
    fun addUsers(users : List<User>)
}