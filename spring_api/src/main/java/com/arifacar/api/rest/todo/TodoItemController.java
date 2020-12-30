package com.arifacar.api.rest.todo;

import com.arifacar.api.rest.common.BaseController;
import com.arifacar.domain.model.generic.GenericInfoResponse;
import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.service.todo.TodoItemService;
import com.arifacar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todoItem")
public class TodoItemController extends BaseController {

    private final TodoItemService todoItemService;

    @Autowired
    public TodoItemController(UserService userService, TodoItemService todoItemService) {
        super(userService);
        this.todoItemService = todoItemService;
    }

    @PostMapping(value = "/create")
    public GenericInfoResponse<TodoItem> create(@RequestBody TodoItem todoItem) {
        TodoItem createdTodoItem = todoItemService.create(todoItem, getCurrentUser());
        return getSuccessGenericInfoResponse(createdTodoItem, "Todo item is created");
    }

    @PostMapping(value = "/update")
    public GenericInfoResponse<TodoItem> update(@RequestBody TodoItem todoItem) {
        TodoItem createdTodoItem = todoItemService.update(todoItem);
        return getSuccessGenericInfoResponse(createdTodoItem, "Todo item has been updated successfully.");
    }

    @PostMapping(value = "/delete")
    public GenericInfoResponse<TodoItem> delete(@RequestBody TodoItem todoItem) {
        todoItemService.delete(todoItem);
        return getSuccessGenericInfoResponse(null, "Todo item has been deleted successfully.");
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
