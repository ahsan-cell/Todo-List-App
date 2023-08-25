package com.example.todolist.ui.todoList

import android.content.Context
import com.example.todolist.data.Todo

sealed class TodoListEvent{
    data class DeleteTodo(val todo: Todo):TodoListEvent()
    data class OnDoneChange(val todo: Todo,val isDone:Boolean):TodoListEvent()
    object onUndoDelete:TodoListEvent()
    data class onTodoClick(val todo:Todo):TodoListEvent()
    object onAddTodoClick:TodoListEvent()
    class onPushNotificationClick(val context: Context,val todo:Todo):TodoListEvent()

}
