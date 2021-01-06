package com.arifacar.service.todo;

import com.arifacar.domain.model.constants.CacheConstants;
import com.arifacar.domain.model.todo.TodoItem;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.repository.todo.TodoItemRepository;
import com.arifacar.service.common.BaseService;
import com.arifacar.service.util.CacheUtil;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService extends BaseService implements ITodoItemService<TodoItem> {

    private final TodoItemRepository todoItemRepository;
    private final CacheUtil cacheUtil;

    @Autowired
    public TodoItemService(TodoItemRepository todoItemRepository, CacheUtil cacheUtil) {
        this.todoItemRepository = todoItemRepository;
        this.cacheUtil = cacheUtil;
    }

    @Override
    public TodoItem create(TodoItem todoItem) {
        todoItem.setCompleted(false);
        todoItem.setInOrder(0);
        cacheUtil.invalidateTodoItemListCache(todoItem.getUser().getId());
        return todoItemRepository.save(todoItem);
    }

    @Override
    public TodoItem update(TodoItem todoItem) {
        TodoItem todoItemPersistent = findById(todoItem.getId());
        todoItemPersistent.setTitle(StringUtils.isEmpty(todoItem.getTitle()) ? todoItemPersistent.getTitle() : todoItem.getTitle());
        todoItemPersistent.setDetail(StringUtils.isEmpty(todoItem.getDetail()) ? todoItemPersistent.getDetail() : todoItem.getDetail());
        todoItemPersistent.setDueDate(StringUtils.isEmpty(todoItem.getDueDate()) ? todoItemPersistent.getDueDate() : todoItem.getDueDate());
        todoItemPersistent.setCompleted(StringUtils.isEmpty(todoItem.getCompleted()) ? todoItemPersistent.getCompleted() : todoItem.getCompleted());
        todoItemPersistent.setInOrder(StringUtils.isEmpty(todoItem.getInOrder()) ? todoItemPersistent.getInOrder() : todoItem.getInOrder());
        cacheUtil.invalidateTodoItemListCache(todoItem.getUser().getId());
        return todoItemRepository.save(todoItemPersistent);
    }

    @Override
    public void delete(TodoItem todoItem) {
        cacheUtil.invalidateTodoItemListCache(todoItem.getUser().getId());
        todoItemRepository.delete(todoItem);
    }

    @Override
    public TodoItem findById(Long id) {
        Optional<TodoItem> todoItem = todoItemRepository.findById(id);
        Assert.isTrue(todoItem.isPresent(), "Todo item not found.");
        return todoItem.get();
    }

    @Override
    public List<TodoItem> findAll(int page) {
        return todoItemRepository.findAll(getPageRequest(page)).getContent();
    }

    @Override
    public List<TodoItem> myTodoList(int page, User currentUser) {
        String key = CacheUtil.createKey(CacheConstants.TODO_ITEM_LIST, String.valueOf(page), String.valueOf(currentUser.getId()));
        List<TodoItem> todoItemCache = cacheUtil.getFromTodoItemCache(key);

        if (todoItemCache != null) {
            return todoItemCache;
        }

        List<TodoItem> todoItemList = todoItemRepository.getTodoItemsByUserIdOrderByIdDesc(getPageRequest(page), currentUser.getId()).getContent();
        cacheUtil.setToTodoItemListCache(key, todoItemList);
        return todoItemList;
    }
}
