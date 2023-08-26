package com.plot.plotserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.categorygroup.NewCategoryGroupReqDto;

import com.plot.plotserver.dto.request.categorygroup.UpdateCategoryGroupReqDto;
import com.plot.plotserver.dto.response.category_group.CategoryGroupResponseDto;
import com.plot.plotserver.service.CategoryGroupService;
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


        categoryGroupService.saveCategoryGroup(newCategoryGroupReqDto);

        Message message = Message.builder()
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
    public void GetCategoryGroupList(HttpServletResponse response,@PathVariable Long categoryGroupId) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        categoryGroupService.delete(categoryGroupId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


    @GetMapping("/all")
    public void getAll(HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<CategoryGroupResponseDto> result = categoryGroupService.getAll();
        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


}
