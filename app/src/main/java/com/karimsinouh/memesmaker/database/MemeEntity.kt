package com.karimsinouh.memesmaker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MemeEntity(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val memePath:String,
)