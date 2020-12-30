package com.arifacar.domain.repository.todo;

import com.arifacar.domain.model.todo.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {

    Page<TodoItem> findAll(Pageable pageable);

    Page<TodoItem> getTodoItemsByUserIdOrderByIdDesc(Pageable pageable, Long userId);

}