package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val Title:String,
    val Description:String?,
    val IsChecked:Boolean,
    @PrimaryKey val id:Int?
)
