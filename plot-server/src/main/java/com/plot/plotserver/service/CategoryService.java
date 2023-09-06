package com.plot.plotserver.service;


import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.category.NewCategoryReqDto;
import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.category.CategoryTodosResponseDto;
import com.plot.plotserver.exception.category.*;
import com.plot.plotserver.exception.categorygroup.CategoryGroupAlreadyExistException;
import com.plot.plotserver.exception.categorygroup.CategoryGroupNotFoundException;
import com.plot.plotserver.exception.todo.TodoSearchFailException;
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

    private final TagRepository tagRepository;

    private final TagCategoryRepository tagCategoryRepository;


    @Transactional
    public CategoryResponseDto save(Long categoryGroupId,NewCategoryReqDto reqDto) {

        Optional<CategoryGroup> categoryGroupOptional = categoryGroupRepository.findById(categoryGroupId);
        if (!categoryGroupOptional.isPresent()) {
            throw new CategoryGroupNotFoundException("카테고리 그룹이 존재하지 않습니다.");
        }

        Optional<Category> categoryOptional = categoryRepository.findByNameAndCategoryGroupId(reqDto.getCategoryName(),categoryGroupId);
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
        categoryGroupOptional.get().addCategory(category);//양방향 연관관계 매핑.

        String tagList = reqDto.getTags().trim();

        if(!tagList.isEmpty())
            addTag(tagList, category);

        return CategoryResponseDto.of(category, tagList);
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
    public void update(Long categoryGroupId,Long categoryId, UpdateCategoryReqDto reqDto) {//이동할 카테고리 그룹, update하는 현재 카테고리 id이다.


        try {

            CategoryGroup newCategoryGroup = categoryGroupRepository.findById(categoryGroupId).orElseThrow(() -> new CategoryGroupNotFoundException("새로운 카테고리 그룹을 찾을 수 없습니다."));
            Category currentCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotExistException("해당 카테고리를 찾을 수 없습니다."));
            CategoryGroup currentCategoryGroup = currentCategory.getCategoryGroup();


            log.info("category.getCategoryGroup={}", currentCategoryGroup);
            log.info("categoryGroup={}",newCategoryGroup);

            if (!newCategoryGroup.equals(currentCategoryGroup)) {//카테고리 변경에서, 그룹이 변경 된 경우,
                categoryRepository
                        .findByNameAndCategoryGroupId(reqDto.getCategoryName(),categoryGroupId)
                        .ifPresent(category -> {
                            throw new CategoryAlreadyExistException("이미 존재하는 카테고리 입니다.");
                        });

//                그룹이 바뀐경우
//                currentCategoryGroup.getCategories().remove(currentCategory); // 현재 그룹에서 카테고리 제거
                currentCategory.setCategoryGroup(newCategoryGroup); // 카테고리 그룹 변경
//                newCategoryGroup.getCategories().add(currentCategory); // 새 그룹에 카테고리 추가

                currentCategory.updateCategory(reqDto);
            }

            else{//그룹이 변경 안된 경우.
                currentCategory.updateCategory(reqDto);
            }

            updateTagCategories(categoryId, reqDto, currentCategory);

        }catch(CategoryAlreadyExistException e){
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            throw new CategoryUpdateFailException("Category 수정에 실패했습니다.");
        }
    }

    @Transactional
    private void updateTagCategories(Long categoryId, UpdateCategoryReqDto reqDto, Category category) {
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
            else tag = tagOpt.get();

            // 태그 카테고리 생성
            tagCategoryRepository.save(
                    TagCategory.builder()
                            .category(category)
                            .tag(tag)
                            .build()
            );
        }
    }


    @Transactional
    public void delete(Long categoryId) {

        try {
            Optional<Category> category = categoryRepository.findById(categoryId);
            categoryRepository.delete(category.get());
            CategoryGroup categoryGroup = category.get().getCategoryGroup();
            categoryGroup.deleteCategory(category.get());

        }catch (Exception e){
            throw new CategoryDeleteFailException("Category 삭제에 실패했습니다.");
        }
    }

    public CategoryTodosResponseDto searchByCategoryId(Long categoryId){//최적화 어려움.

        try {
            Category category = categoryRepository.findById(categoryId).get();
            CategoryTodosResponseDto result = CategoryTodosResponseDto.of(category);
            return result;
        } catch (Exception e) {
            throw new CategoryNotExistException("Category 를 조회할 수 없습니다.");
        }

    }

}
