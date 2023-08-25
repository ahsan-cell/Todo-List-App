package com.example.todolist.ui.AddEditTodo

sealed class AddEditTodoEvent{
    data class OnTitleChange(val title:String):AddEditTodoEvent()
    data class OnDescriptionChange(val description:String):AddEditTodoEvent()
    object OnSaveClick:AddEditTodoEvent()
}
