package com.plot.plotserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.tag.TagResponseDto;
import com.plot.plotserver.service.TagCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/tag-categories")
public class TagCategoryController {

    private final TagCategoryService tagCategoryService;

    @GetMapping("tag/{tagName}")
    @Comment("태그 이름으로 유저가 만든 카테고리를 찾아 반환")
    public void searchCategoryList(HttpServletResponse response, @PathVariable String tagName) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<CategoryResponseDto> result = tagCategoryService.searchByTagName(tagName);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @GetMapping("/tag")
    @Comment("현재 유저가 만든 태그 목록을 반환")
    public void searchTags(HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<TagResponseDto> result = tagCategoryService.searchTags();

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }
}
