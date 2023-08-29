package com.plot.plotserver.service;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.Tag;
import com.plot.plotserver.dto.request.tag_category.SearchTagCategoryReqDto;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.tag.TagResponseDto;
import com.plot.plotserver.repository.TagCategoryRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagCategoryService {

    private final TagCategoryRepository tagCategoryRepository;


    public List<CategoryResponseDto> searchByTagName(String tagNameParam){

        Long userId = SecurityContextHolderUtil.getUserId();

        String[] tagNames = tagNameParam.split("&");
        List<Category> tempList= new ArrayList<>();
        List<CategoryResponseDto> result = new ArrayList<>();

        for (String tagName : tagNames) {
            List<Category> categories = tagCategoryRepository.findByUserIdAndTagName(userId, tagName);
            categories.forEach(category -> result.add(CategoryResponseDto.of(category, tagName)));
        }

        return result;
    }

    public List<TagResponseDto> searchTags() {

        Long userId = SecurityContextHolderUtil.getUserId();
        List<TagResponseDto> result = new ArrayList<>();

        List<Tag> tagList = tagCategoryRepository.findByUserId(userId);

        tagList.forEach(tag -> result.add(TagResponseDto.of(tag.getTagName())));

        return result;
    }
}
