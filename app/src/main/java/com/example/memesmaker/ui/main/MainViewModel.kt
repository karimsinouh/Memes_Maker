package com.example.memesmaker.ui.main

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.memesmaker.data.Meme
import com.example.memesmaker.database.MemesDatabase

class MainViewModel(app: Application):AndroidViewModel(app) {

    private val db = MemesDatabase.getInstance(app)

    val memesList = db.memes().getAllMemes()

    val selectedMemes = mutableStateListOf<Int>()

    fun isSelected(id: Int)=selectedMemes.contains(id)

    fun isSelectionMode()= !selectedMemes.isEmpty()

    fun select(id:Int){
        if (!isSelected(id))
            selectedMemes.add(id)
        else
            selectedMemes.remove(id)
    }

}