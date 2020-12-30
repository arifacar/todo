package com.arifacar.service.todo;

import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.todo.TodoItemRepository;
import com.arifacar.service.common.BaseService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService extends BaseService {

    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TodoItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public TodoItem create(TodoItem todoItem, User currentUser) {
        todoItem.setUser(currentUser);
        todoItem.setCompleted(false);
        todoItem.setInOrder(0);
        return todoItemRepository.save(todoItem);
    }

    public TodoItem update(TodoItem todoItem) {
        TodoItem todoItemPersistent = findByTodoItemId(todoItem.getId());
        todoItemPersistent.setTitle(StringUtils.isEmpty(todoItem.getTitle()) ? todoItemPersistent.getTitle() : todoItem.getTitle());
        todoItemPersistent.setDetail(StringUtils.isEmpty(todoItem.getDetail()) ? todoItemPersistent.getDetail() : todoItem.getDetail());
        todoItemPersistent.setDueDate(StringUtils.isEmpty(todoItem.getDueDate()) ? todoItemPersistent.getDueDate() : todoItem.getDueDate());
        todoItemPersistent.setCompleted(StringUtils.isEmpty(todoItem.getCompleted()) ? todoItemPersistent.getCompleted() : todoItem.getCompleted());
        todoItemPersistent.setInOrder(StringUtils.isEmpty(todoItem.getInOrder()) ? todoItemPersistent.getInOrder() : todoItem.getInOrder());
        return todoItemRepository.save(todoItemPersistent);
    }

    private TodoItem findByTodoItemId(Long id) {
        Optional<TodoItem> todoItem = todoItemRepository.findById(id);
        Assert.isTrue(todoItem.isPresent(), "Todo item not found.");
        return todoItem.get();
    }

    public void delete(TodoItem todoItem) {
        todoItemRepository.delete(todoItem);
    }

    public List<TodoItem> findAll(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, getCommonPageSize());
        return todoItemRepository.findAll(pageRequest).getContent();
    }

    public List<TodoItem> myTodoList(int page, Long userId) {
        PageRequest pageRequest = PageRequest.of(page - 1, getCommonPageSize());
        return todoItemRepository.getTodoItemsByUserIdOrderByIdDesc(pageRequest, userId).getContent();
    }
}
