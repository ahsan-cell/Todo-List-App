package com.example.todolist.ui.AddEditTodo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.ui.todoList.UiEvent
import kotlinx.coroutines.flow.collect


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditTodoScreen(
    onPopBackStack:()->Unit,
    viewModel: AddEditTodoViewModel= hiltViewModel(),
    modifier: Modifier=Modifier
){
    val scaffoldState= rememberScaffoldState()

    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ event->
            when(event){
                is UiEvent.popBackStack-> onPopBackStack()
                is UiEvent.showSnackBar->{
                    scaffoldState.snackbarHostState.showSnackbar(event.message,event.action)
                }
                else->Unit
            }

        }
    }
    Scaffold(scaffoldState = scaffoldState,
    modifier = Modifier
        .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(AddEditTodoEvent.OnSaveClick) }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                
            }
        },
        topBar = { TopAppBar(title = {Text("Todo")}, backgroundColor = MaterialTheme.colors.primary)}
        
        ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            TextField(
                value = viewModel.title,
                onValueChange = {
                        viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it))
                                },
                Modifier.fillMaxWidth(),
                placeholder = {Text("Title")})

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = viewModel.description,
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                },
                placeholder = {
                    Text(text = "Description")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )

            
        }

    }

}