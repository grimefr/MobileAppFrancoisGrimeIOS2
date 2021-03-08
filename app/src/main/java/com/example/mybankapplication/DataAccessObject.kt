package com.example.mybankapplication

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM entities")
    fun getAll(): List<Entities>

    @Query("SELECT * FROM entities WHERE uid IS (:id)")
    fun searchId(id: String): Entities

    @Update
    fun update(vararg users: Entities)
    @Insert
    fun insert(entities: Entities)

    @Delete
    fun delete(entities: Entities)
}
