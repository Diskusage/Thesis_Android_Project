package com.example.sqliteapp.adapters

//An interface to define onClick events
//will be used in RecyclerView lists with buttons
interface ClickListener {
    fun onPositionClicked(i: Int)
}