package com.plot.plotserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.categorygroup.NewCategoryGroupReqDto;

import com.plot.plotserver.dto.request.categorygroup.UpdateCategoryGroupReqDto;
import com.plot.plotserver.dto.response.category_group.CategoryGroupResponseDto;
import com.plot.plotserver.service.CategoryGroupService;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/category-groups")
public class CategoryGroupController {


    private final CategoryGroupService categoryGroupService;


    @PostMapping("")
    public void addCategoryGroup(HttpServletResponse response, @RequestBody NewCategoryGroupReqDto newCategoryGroupReqDto) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        CategoryGroupResponseDto result = categoryGroupService.saveCategoryGroup(newCategoryGroupReqDto);


        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


    @PatchMapping("/{categoryGroupId}")
    public void updateCategoryGroup(HttpServletResponse response, @PathVariable Long categoryGroupId, @RequestBody  UpdateCategoryGroupReqDto updateCategoryGroupReqDto) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        categoryGroupService.update(categoryGroupId,updateCategoryGroupReqDto);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }



    @DeleteMapping("/{categoryGroupId}")
    public void deleteCategoryGroup(HttpServletResponse response,@PathVariable Long categoryGroupId) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        categoryGroupService.delete(categoryGroupId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @GetMapping("/all-category-path")
    public void getAllCategoryPath(HttpServletResponse response) throws IOException {//안 쓰임.

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        Long userId = SecurityContextHolderUtil.getUserId();

        List<CategoryGroupResponseDto.InTodoAdd> result = categoryGroupService.getAllCategoryPath(userId);
        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @GetMapping("/all-category-group")
    public void getAllCategoryGroup(HttpServletResponse response) throws IOException {//완료

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        Long userId = SecurityContextHolderUtil.getUserId();
        List<CategoryGroupResponseDto.InCategoryAdd> result = categoryGroupService.getAllCategoryGroup(userId);
        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @GetMapping("/all-todo")//카테고리 그룹, 카테고리들, 그 안의 투두 경로들(dailytodo 생성할때 필요)
    public void getAllTodos(HttpServletResponse response) throws IOException {//완료

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        Long userId = SecurityContextHolderUtil.getUserId();
        List<CategoryGroupResponseDto.InDailyTodoAdd> result = categoryGroupService.getAllTodos(userId);
        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @GetMapping("/all")//카테고리 그룹, 그 안의 카테고리들, 그 안의 태그들
    public void getAll(HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        Long userId = SecurityContextHolderUtil.getUserId();
        List<CategoryGroupResponseDto> result = categoryGroupService.getAll(userId);
        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


}
