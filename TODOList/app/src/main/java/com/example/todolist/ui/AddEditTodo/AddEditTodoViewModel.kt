package com.example.todolist.ui.AddEditTodo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Todo
import com.example.todolist.data.TodoRepository
import com.example.todolist.ui.todoList.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditTodoViewModel @Inject constructor(private val repository:TodoRepository,
savedStateHandle:SavedStateHandle):ViewModel() {

    private val _uiEvent= Channel<UiEvent>()
    val uiEvent=_uiEvent.receiveAsFlow()

    var todo by mutableStateOf<Todo?>(null)
        private set
    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set

    init{
        val todoId=savedStateHandle.get<Int>("todoId")
        if(todoId!=-1){
           viewModelScope.launch {
               if (todoId != null) {
                   repository.getTodoById(todoId)?.let { todo->
                       title=todo.Title
                       description=todo.Description?:""
                        this@AddEditTodoViewModel.todo=todo
                   }
               }
           }

        }
    }

    fun onEvent(event:AddEditTodoEvent){
        when(event){
            is AddEditTodoEvent.OnTitleChange->{
                title=event.title
            }
            is AddEditTodoEvent.OnDescriptionChange->{
                description=event.description
            }
            is AddEditTodoEvent.OnSaveClick->{
                viewModelScope.launch {
                    if(title.isNotBlank()){
                        repository.insertTodo(Todo(
                            title,description,todo?.IsChecked?:false,id=todo?.id
                        ))
                        sendUiEvent(UiEvent.popBackStack)
                    }
                    else{
                        sendUiEvent(UiEvent.showSnackBar(message = "Title cannot be blank"))
                        return@launch
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event:UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}