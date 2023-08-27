package com.plot.plotserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.category.NewCategoryReqDto;
import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.dto.response.category.CategoryTodosResponseDto;
import com.plot.plotserver.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/categories")
public class CategoryController {


    private final CategoryService categoryService;

    @PostMapping("/add/{categoryGroupId}")
    public void addCategory(@PathVariable Long categoryGroupId, @RequestBody NewCategoryReqDto newCategoryReqDto,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        categoryService.save(categoryGroupId,newCategoryReqDto);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @PatchMapping("{categoryGroupId}/{categoryId}")
    public void updateCategory(@PathVariable Long categoryGroupId,@PathVariable Long categoryId,@RequestBody UpdateCategoryReqDto updateCategoryReqDto, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        categoryService.update(categoryGroupId,categoryId,updateCategoryReqDto);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


    @DeleteMapping("/{categoryId}")
    public void deleteCategory(HttpServletResponse response,@PathVariable Long categoryId) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        categoryService.delete(categoryId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @GetMapping("/{categoryId}") //카테고리 별로 todos 보여주기.
    @Comment("카테고리 별로 Todos 보여주기.")
    public void showTodosByCategory(@PathVariable Long categoryId,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        CategoryTodosResponseDto categoryTodosResponseDto = categoryService.searchByCategoryId(categoryId);

        Message message = Message.builder()
                .data(categoryTodosResponseDto)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }
}
