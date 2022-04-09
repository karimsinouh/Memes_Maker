package com.example.memesmaker.ui.main

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memesmaker.data.Meme
import com.example.memesmaker.database.MemeEntity
import com.example.memesmaker.database.MemesDatabase
import com.example.memesmaker.util.ads.GetAdRequest
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.launch

class MainViewModel(app: Application):AndroidViewModel(app) {

    //adRequest
    val adRequest=GetAdRequest(app)

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

    fun delete(){
        viewModelScope.launch {
            db.memes().deleteList(selectedMemes)
            selectedMemes.clear()
        }
    }

    fun delete(meme:MemeEntity){
        viewModelScope.launch {
            db.memes().delete(meme)
        }
    }

}