package com.example.mybankapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM entities")
    fun getAll(): List<Entities>

    @Insert
    fun insert(entities: Entities)

    @Delete
    fun delete(entities: Entities)
}
