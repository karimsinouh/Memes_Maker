package com.example.memesmaker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemeEntity::class],version = 1)
abstract class MemesDatabase:RoomDatabase() {

    abstract fun memes():MemeDao


    companion object{

        private var INSTANCE:MemesDatabase ?= null

        fun getInstance(context:Context):MemesDatabase{
            return if (INSTANCE!=null)
                INSTANCE!!
            else{
                INSTANCE=Room.databaseBuilder(context,MemesDatabase::class.java,"memes_db").build()
                INSTANCE!!
            }
        }

    }

}