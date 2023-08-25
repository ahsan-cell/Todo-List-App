package com.example.todolist.ui.todoList

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.todolist.data.Todo

@Composable
fun TodoItem(
    todo: Todo,
    oneEvent: (TodoListEvent)->Unit,
    modifier:Modifier=Modifier
){

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = todo.Title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        oneEvent(TodoListEvent.DeleteTodo(todo = todo))

                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")

                    }
                }
                todo.Description?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it)
                }

            }
            Checkbox(checked = todo.IsChecked,
                onCheckedChange = { isChecked ->
                    oneEvent(TodoListEvent.OnDoneChange(todo, isChecked))
                })
        }


}

