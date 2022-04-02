package com.example.memesmaker.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.memesmaker.data.Meme

class MainViewModel:ViewModel() {

    val memesList = mutableStateListOf<Meme>()


}