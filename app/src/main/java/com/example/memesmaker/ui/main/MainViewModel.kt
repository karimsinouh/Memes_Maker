package com.example.memesmaker.ui.main

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.memesmaker.data.Meme
import com.example.memesmaker.database.MemesDatabase

class MainViewModel(private val app: Application):AndroidViewModel(app) {

    private val db = MemesDatabase.getInstance(app)

    val memesList = db.memes().getAllMemes()


}