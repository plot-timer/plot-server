package com.plot.plotserver.service;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.Tag;
import com.plot.plotserver.dto.request.tag.SearchTagReqDto;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.tag.TagResponseDto;
import com.plot.plotserver.exception.category.CategoryNotExistException;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.TagRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryResponseDto> searchByTagName(SearchTagReqDto reqDto){
        List<CategoryResponseDto> result = new ArrayList<>();

        Long userId = SecurityContextHolderUtil.getUserId();

        String[] tagNames = reqDto.getTagName().split("/");
        List<Tag> tagList = new ArrayList<>();

        for (String tagName : tagNames) {
            List<Tag> tags = tagRepository.findByUserAndTagName(userId, tagName);
            tagList.addAll(tags);
        }

        for (Tag tag : tagList) {
            Long categoryId = tag.getCategory().getId();
            Category category = categoryRepository.findById(categoryId).get();
            result.add(CategoryResponseDto.of(category));
        }

        return result;
    }

    public List<TagResponseDto> searchUserTags() {

        Long userId = SecurityContextHolderUtil.getUserId();

        List<TagResponseDto> result = new ArrayList<>();
        Set<String> tagNameSet = new HashSet<>();

        List<Tag> tagsOpt = tagRepository.findByUserId(userId);

        for (Tag tag : tagsOpt)
            tagNameSet.add(tag.getTagName());

        for (String s : tagNameSet)
            result.add(TagResponseDto.of(s));

        return result;
    }
}
