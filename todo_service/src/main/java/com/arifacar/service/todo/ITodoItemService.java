package com.arifacar.service.todo;

import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.domain.model.user.User;
import com.arifacar.service.generic.ICrudService;

import java.util.List;

public interface ITodoItemService<T> extends ICrudService<T> {

    List<TodoItem> myTodoList(int page, User user);

}