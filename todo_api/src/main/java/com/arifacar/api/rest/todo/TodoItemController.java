package com.arifacar.api.rest.todo;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.service.todo.TodoItemService;
import com.arifacar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todoItem")
public class TodoItemController extends BaseController {

    private final TodoItemService todoItemService;

    @Autowired
    public TodoItemController(UserService userService, MessageSource messageSource, TodoItemService todoItemService) {
        super(userService, messageSource);
        this.todoItemService = todoItemService;
    }

    @PostMapping(value = "/create")
    public GenericInfoResponse<TodoItem> create(@RequestBody TodoItem todoItem) {
        TodoItem createdTodoItem = todoItemService.create(todoItem, getCurrentUser());
        return getSuccessGenericInfoResponse(createdTodoItem, getMessage("todo.created"));
    }

    @PostMapping(value = "/update")
    public GenericInfoResponse<TodoItem> update(@RequestBody TodoItem todoItem) {
        TodoItem createdTodoItem = todoItemService.update(getCurrentUser(), todoItem);
        return getSuccessGenericInfoResponse(createdTodoItem, getMessage("todo.update"));
    }

    @PostMapping(value = "/delete")
    public GenericInfoResponse<TodoItem> delete(@RequestBody TodoItem todoItem) {
        todoItemService.delete(getCurrentUser(), todoItem);
        return getSuccessGenericInfoResponse(null, getMessage("todo.delete"));
    }


    @RequestMapping(value = "/myTodoList/{page}")
    public GenericInfoResponse<List<TodoItem>> myTodoList(@PathVariable("page") int page) {
        List<TodoItem> todoItemList = todoItemService.myTodoList(page, getCurrentUser().getId());
        return getSuccessGenericInfoResponse(todoItemList, "Page: " + page);
    }

    @RequestMapping(value = "/all/{page}")
    public GenericInfoResponse<List<TodoItem>> findAll(@PathVariable("page") int page) {
        List<TodoItem> todoItemList = todoItemService.findAll(page);
        return getSuccessGenericInfoResponse(todoItemList, "Page: " + page);
    }
}
