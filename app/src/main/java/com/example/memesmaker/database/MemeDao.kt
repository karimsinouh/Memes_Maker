package com.example.memesmaker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemeDao {

    @Insert(entity = MemeEntity::class)
    suspend fun put(memeEntity: MemeEntity)

    @Delete(entity = MemeEntity::class)
    suspend fun delete(memeEntity: MemeEntity)

    @Query("SELECT * FROM MemeEntity ORDER BY id DESC")
    fun getAllMemes():LiveData<List<MemeEntity>>

}