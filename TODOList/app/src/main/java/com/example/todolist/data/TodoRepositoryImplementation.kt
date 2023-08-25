package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

class TodoRepositoryImplementation (
    private val todoDatabase: TodoDatabase
        ):TodoRepository {
    override suspend fun insertTodo(todo: Todo) {
        todoDatabase.dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        todoDatabase.dao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return todoDatabase.dao.getTodoById(id)
    }

    override fun getTodos(): Flow<List<Todo>> {
       return todoDatabase.dao.getTodos()
    }
}