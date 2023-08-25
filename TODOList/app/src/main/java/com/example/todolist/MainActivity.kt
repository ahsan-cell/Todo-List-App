package com.example.todolist

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.ui.AddEditTodo.AddEditTodoScreen
import com.example.todolist.ui.theme.TODOListTheme
import com.example.todolist.ui.todoList.Routes
import com.example.todolist.ui.todoList.TodoListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setting up the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel= NotificationChannel("Todo_Channel","Todos", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        setContent {
            TODOListTheme {

                val navController= rememberNavController()
                NavHost(navController = navController, startDestination = Routes.TODO_LIST, builder ={
                    composable(route = Routes.TODO_LIST){
                        TodoListScreen(onNavigate = { navController.navigate(it.route) })
                    }
                    composable(route = Routes.ADD_EDIT_TODO+"?=todoId={todoId}",
                    arguments = listOf(
                        navArgument(name = "todoId"){
                            type= NavType.IntType
                            defaultValue=-1
                        }
                    )){
                        AddEditTodoScreen(onPopBackStack = {navController.popBackStack()})
                    }
                } )

            }
        }
    }
}

