package com.arifacar.api.rest.todo;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.service.todo.ITodoItemService;
import com.arifacar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todoItem")
public class TodoItemController extends BaseController {

    private final ITodoItemService<TodoItem> todoItemService;

    @Autowired
    public TodoItemController(UserService userService, MessageSource messageSource, ITodoItemService todoItemService) {
        super(userService, messageSource);
        this.todoItemService = todoItemService;
    }

    @PostMapping(value = "/create")
    public GenericInfoResponse<TodoItem> create(@RequestBody TodoItem todoItem) {
        todoItem.setUser(getCurrentUser());
        TodoItem createdTodoItem = todoItemService.create(todoItem);
        return getSuccessGenericInfoResponse(createdTodoItem, getMessage("todo.created"));
    }

    @PostMapping(value = "/update")
    public GenericInfoResponse<TodoItem> update(@RequestBody TodoItem todoItem) {
        todoItem.setUser(getCurrentUser());
        TodoItem createdTodoItem = todoItemService.update(todoItem);
        return getSuccessGenericInfoResponse(createdTodoItem, getMessage("todo.update"));
    }

    @PostMapping(value = "/delete")
    public GenericInfoResponse<TodoItem> delete(@RequestBody TodoItem todoItem) {
        todoItem.setUser(getCurrentUser());
        todoItemService.delete(todoItem);
        return getSuccessGenericInfoResponse(null, getMessage("todo.delete"));
    }

    @RequestMapping(value = "/myTodoList/{page}")
    public GenericInfoResponse<List<TodoItem>> myTodoList(@PathVariable("page") int page) {
        List<TodoItem> todoItemList = todoItemService.myTodoList(page, getCurrentUser());
        return getSuccessGenericInfoResponse(todoItemList, "Page: " + page);
    }

    @RequestMapping(value = "/all/{page}")
    public GenericInfoResponse<List<TodoItem>> findAll(@PathVariable("page") int page) {
        List<TodoItem> todoItemList = todoItemService.findAll(page);
        return getSuccessGenericInfoResponse(todoItemList, "Page: " + page);
    }
}
