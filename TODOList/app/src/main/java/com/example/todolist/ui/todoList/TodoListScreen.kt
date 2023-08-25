package com.example.todolist.ui.todoList

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoListScreen(

    onNavigate:(UiEvent.Navigate)->Unit,
    viewModel: TodoViewModel= hiltViewModel()
){
    val todos=viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState= rememberScaffoldState()
    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{event->
            when(event){
                is UiEvent.showSnackBar->{
                    val result=scaffoldState.snackbarHostState.showSnackbar(event.message,event.action)
                    if(result==SnackbarResult.ActionPerformed){
                        viewModel.onEvent(TodoListEvent.onUndoDelete)
                    }
                }
                is UiEvent.Navigate->onNavigate(event)
                else->Unit
            }

        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {FloatingActionButton(onClick = { viewModel.onEvent(TodoListEvent.onAddTodoClick) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")

        }},
        topBar = { TopAppBar(title = {Text("Todo List")}, backgroundColor = MaterialTheme.colors.primary)
        }){
        LazyColumn(modifier = Modifier.fillMaxSize() ){
            items(todos.value){todo->
                TodoItem(todo =todo , oneEvent =viewModel::onEvent, modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.onEvent(TodoListEvent.onTodoClick(todo)) }
                    .padding(16.dp))
               PushNotificationButton(true,viewModel::onEvent,todo)
            }

        }
    }



}
