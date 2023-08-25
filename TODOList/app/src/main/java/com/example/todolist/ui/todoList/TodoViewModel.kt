package com.example.todolist.ui.todoList

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.MainActivity
import com.example.todolist.R
import com.example.todolist.TodoApp
import com.example.todolist.data.Todo
import com.example.todolist.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepository: TodoRepository): ViewModel() {
    val todos=todoRepository.getTodos()
    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private var recentlyDeletedTodo: Todo?=null

    fun onEvent(todoListEvent: TodoListEvent){
        when(todoListEvent){
            is TodoListEvent.DeleteTodo->{
                viewModelScope.launch {
                    recentlyDeletedTodo=todoListEvent.todo
                    todoRepository.deleteTodo(todoListEvent.todo)
                    sendUiEvent(UiEvent.showSnackBar("Todo Deleted","Undo"))
                }
            }
            is TodoListEvent.onUndoDelete->{
                recentlyDeletedTodo?.let { todo->
                    viewModelScope.launch {
                        todoRepository.insertTodo(todo)
                    }
                }
            }
            is TodoListEvent.onTodoClick->{
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO+"?=todoId=${todoListEvent.todo.id}"))
            }
            is TodoListEvent.OnDoneChange->{
                viewModelScope.launch {
                    todoRepository.insertTodo(
                        todoListEvent.todo.copy(IsChecked = todoListEvent.isDone)
                    )
                }
            }
            is TodoListEvent.onAddTodoClick->{
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.onPushNotificationClick->{
                var builder=NotificationCompat.Builder(todoListEvent.context,"Todo_Channel")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(todoListEvent.todo.Title)
                    .setContentText(todoListEvent.todo?.Description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                with(NotificationManagerCompat.from(todoListEvent.context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(todoListEvent.todo.id?:-1, builder.build())
                }
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }


}