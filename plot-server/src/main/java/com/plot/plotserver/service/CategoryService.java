package com.plot.plotserver.service;


import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.category.NewCategoryReqDto;
import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.exception.category.CategoryAlreadyExistException;
import com.plot.plotserver.exception.category.CategoryDeleteFailException;
import com.plot.plotserver.exception.category.CategorySavedFailException;
import com.plot.plotserver.exception.category.CategoryUpdateFailException;
import com.plot.plotserver.exception.categorygroup.CategoryGroupAlreadyExistException;
import com.plot.plotserver.exception.categorygroup.CategoryGroupNotFoundException;
import com.plot.plotserver.repository.*;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryGroupRepository categoryGroupRepository;

    private final TodoRepository todoRepository;

    private final TagRepository tagRepository;

    private final TagCategoryRepository tagCategoryRepository;


    @Transactional
    public void save(NewCategoryReqDto reqDto) {

        Long userId = SecurityContextHolderUtil.getUserId();

        Optional<CategoryGroup> categoryGroupOptional = categoryGroupRepository.findByUserIdAndName(userId, reqDto.getCategoryGroup());
        if (!categoryGroupOptional.isPresent()) {
            throw new CategoryGroupNotFoundException("카테고리 그룹이 존재하지 않습니다.");
        }

        Optional<Category> categoryOptional = categoryRepository.findByNameAndCategoryGroupId(reqDto.getCategoryName(), categoryGroupOptional.get().getId());
        if (categoryOptional.isPresent()) {
            throw new CategoryAlreadyExistException("이미 존재하는 카테고리입니다.");
        }

        Category category=Category.builder()
                .name(reqDto.getCategoryName())
                .star(false)
                .emoji(reqDto.getEmoji())
                .categoryGroup(categoryGroupOptional.get())
                .build();

        categoryRepository.save(category);

        String tagList = reqDto.getTags().trim();

        if(!tagList.isEmpty())
            addTag(tagList, category);
    }

    @Transactional
    public void addTag(String tagList, Category category) {
        String[] tagNames = tagList.split("/");
        List<Tag> tags = new ArrayList<>();

        for (String tagName : tagNames) {
            // 해당 태그 이름의 태그가 존재하는지, 확인
            Optional<Tag> tagOpt = tagRepository.findByTagName(tagName);

            if (!tagOpt.isPresent()) {
                Tag tag = Tag.builder().tagName(tagName).build();
                tagRepository.save(tag);
                tags.add(tag);
            }
            else
                tags.add(tagOpt.get());
        }

        tags.forEach(tag -> {
            TagCategory tagCategory = TagCategory.builder()
                    .category(category)
                    .tag(tag)
                    .build();
            tagCategoryRepository.save(tagCategory);
        });
    }


    @Transactional
    public void update(Long categoryId, UpdateCategoryReqDto reqDto) {

        System.out.println(reqDto.getCategoryName());
        System.out.println(reqDto.getEmoji());
        System.out.println(reqDto.getCategoryGroup());
        System.out.println(reqDto.isStar());
        System.out.println(reqDto.getTags());

        try {
            Long userId = SecurityContextHolderUtil.getUserId();

            Optional<CategoryGroup> categoryGroupOptional = categoryGroupRepository.findByUserIdAndName(userId, reqDto.getCategoryGroup());//이동할 그룹 찾기.
            if(!categoryGroupOptional.isPresent())
                throw new CategoryGroupNotFoundException("해당하는 카테고리 그룹이 존재하지 않습니다.");

            CategoryGroup categoryGroup = categoryGroupOptional.get();

            Optional<Category> updateCategoryOptional = categoryRepository.findByNameAndCategoryGroupId(reqDto.getCategoryName(), categoryGroup.getId());//이동할 그룹에 동일한 이름으로 존재하는지 확인.
            if (updateCategoryOptional.isPresent())
                throw new CategoryAlreadyExistException("이미 존재하는 카테고리 입니다.");

            Category category = categoryRepository.findById(categoryId).get();
            category.updateCategory(reqDto, categoryGroup);

            List<TagCategory> tagCategories = tagCategoryRepository.findByCategoryId(categoryId);
            if(!tagCategories.isEmpty()){
                tagCategories.forEach(tagCategory -> tagCategoryRepository.delete(tagCategory));
            }

            String tags = reqDto.getTags();
            String[] tagNames = tags.split("/");

            for (String tagName : tagNames) {

                // 해당 태그이름을 갖는 태그가 존재하는가?
                Optional<Tag> tagOpt = tagRepository.findByTagName(tagName);
                Tag tag = null;

                if(!tagOpt.isPresent()){
                    tag = Tag.builder().tagName(tagName).build();
                    tagRepository.save(tag);
                }
                else
                    tag = tagOpt.get();

                // 태그 카테고리 생성
                tagCategoryRepository.save(
                        TagCategory.builder()
                                .category(category)
                                .tag(tag)
                                .build()
                );
            }

        }catch(CategoryAlreadyExistException e){
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            throw new CategoryUpdateFailException("Category 수정에 실패했습니다.");
        }
    }


    @Transactional
    public void delete(Long categoryId) {

        try {
            Optional<Category> category = categoryRepository.findById(categoryId);
            categoryRepository.delete(category.get());
        }catch (Exception e){
            throw new CategoryDeleteFailException("Category 삭제에 실패했습니다.");
        }
    }

}
