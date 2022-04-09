package com.example.memesmaker.ui.customMeme

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.Window
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.memesmaker.data.CustomMemeItems
import com.example.memesmaker.data.ScreenState
import com.example.memesmaker.data.Tools
import com.example.memesmaker.database.MemeEntity
import com.example.memesmaker.database.MemesDatabase
import com.example.memesmaker.util.SaveMemeToStorage
import com.example.memesmaker.util.ViewToBitmap
import kotlinx.coroutines.launch

class CustomMemeViewModel(private val app:Application) :AndroidViewModel(app) {

    private val db = MemesDatabase.getInstance(app)

    val items = mutableStateListOf<CustomMemeItems>()

    val background = mutableStateOf<Bitmap?>(null)

    val currentTool = mutableStateOf(Tools.NONE)

    val selectedItem = mutableStateOf<CustomMemeItems?>(null)

    var customMemeView by mutableStateOf<CustomMemeView?>(null)

    var state by mutableStateOf(ScreenState.IDLE)

    var uri:Uri?=null

    fun selectItem(item:CustomMemeItems){
        selectedItem.value = if (selectedItem.value?.timestamp==item.timestamp)
            null
        else
            item
    }

    fun addText(
        text:String,
        color:Color
    ){
        val item= CustomMemeItems(
            type = CustomMemeItems.TYPE_TEXT,
            text = text,
            textColor = color,
            timestamp = System.currentTimeMillis()
        )
        items.add(item)
    }

    fun addImage(
        bitmap:Bitmap?
    ){
        if (bitmap==null)
            return
        val item=CustomMemeItems(
            type = CustomMemeItems.TYPE_IMAGE,
            bitmap = bitmap,
            timestamp = System.currentTimeMillis()
        )

        items.add(item)
    }

    fun remove(item:CustomMemeItems?){
        if (item==null)
            return
        items.remove(item)
        selectedItem.value=null
    }

    fun save(window:Window){
        if (items.isEmpty()){
            state=ScreenState.ERROR.apply { message= "You need to add at least one text or image to save this meme" }
            return
        }

        state=ScreenState.LOADING
        selectedItem.value=null
        ViewToBitmap(customMemeView!!,window){bitmap->

            SaveMemeToStorage(app,bitmap){result->

                result.onSuccess {
                    uri=it
                    val item=MemeEntity(0,it.toString())
                    viewModelScope.launch {
                        db.memes().put(item)
                    }
                    state=ScreenState.DONE
                }

                result.onFailure {
                    state=ScreenState.ERROR.apply { message=it.message }
                }

            }

        }
    }

}