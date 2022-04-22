package com.karimsinouh.memesmaker.ui.main

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.memesmaker.database.MemeEntity
import com.karimsinouh.memesmaker.database.MemesDatabase
import com.karimsinouh.memesmaker.util.ads.GetAdRequest
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