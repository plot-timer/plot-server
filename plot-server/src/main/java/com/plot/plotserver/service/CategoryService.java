package com.plot.plotserver.service;


import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Tag;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.dto.request.category.NewCategoryReqDto;
import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.exception.category.CategoryAlreadyExistException;
import com.plot.plotserver.exception.category.CategoryDeleteFailException;
import com.plot.plotserver.exception.category.CategorySavedFailException;
import com.plot.plotserver.exception.category.CategoryUpdateFailException;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.TagRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryGroupRepository categoryGroupRepository;

    private final TagRepository tagRepository;

    private final TodoRepository todoRepository;


    @Transactional
    public void save(NewCategoryReqDto categoryReqDto) {

        try {
            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findByUserIdAndName(userId, categoryReqDto.getCategory_group());

            //category가 동일한 이름으로 존재하는 경우, 에러 반환 저장 ㄴㄴ
            Optional<Category> categoryOptional = categoryRepository.findByNameAndCategoryGroupId(categoryReqDto.getCategoryName(), categoryGroup.get().getId());
            if (categoryOptional.isPresent()) {
                throw new CategoryAlreadyExistException("이미 존재하는 카테고리 입니다.");
            }

            Category category=Category.builder()
                   .name(categoryReqDto.getCategoryName())
                   .star(false)
                    .user_id(userId)
                   .emoji(categoryReqDto.getEmoji())
                   .categoryGroup(categoryGroup.get())
                   .build();

            categoryRepository.save(category);

            addTag(categoryReqDto, userId, category);

        }catch(CategoryAlreadyExistException e){
            throw e;
        } catch (Exception e) {
            throw new CategorySavedFailException("Category 생성에 실패하였습니다.");
        }
    }

    private void addTag(NewCategoryReqDto categoryReqDto, Long userId, Category category) {
        String[] tagNames = categoryReqDto.getTags().split("/");

        for (String tagName : tagNames) {
            Tag tag = Tag.builder()
                    .tagName(tagName)
                    .category(category)
                    .userId(userId)
                    .build();
            tagRepository.save(tag);
        }
    }

    @Transactional
    public void update(Long categoryId, UpdateCategoryReqDto updateCategoryReqDto) {

        try {
            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findByUserIdAndName(userId, updateCategoryReqDto.getCategory_group());//이동할 그룹 찾기.

            //category가 동일한 이름으로 존재하는 경우, 에러 반환 저장 ㄴㄴ
            Optional<Category> updateCategoryOptional = categoryRepository.findByNameAndCategoryGroupId(updateCategoryReqDto.getCategoryName(), categoryGroup.get().getId());//이동할 그룹에 동일한 이름으로 존재하는지 확인.
            if (updateCategoryOptional.isPresent()) {
                throw new CategoryAlreadyExistException("이미 존재하는 카테고리 입니다.");
            }

            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            Category category = categoryOptional.get();

            updateCategoryGroup(updateCategoryReqDto, userId, category);

            String newCategoryName = updateCategoryReqDto.getCategoryName();

            List<Todo> todos = todoRepository.findByCategory(category.getId());
            todos.forEach( t -> t.setCategoryName(newCategoryName));

            eraseAndUpdateTags(updateCategoryReqDto, userId, category);

        }catch(CategoryAlreadyExistException e){
            throw e;
        }catch(Exception e){
            throw new CategoryUpdateFailException("Category 수정에 실패했습니다.");
        }
    }

    private void eraseAndUpdateTags(UpdateCategoryReqDto updateCategoryReqDto, Long userId, Category category) {
        // 원래 있던 태그들을 모두 삭제
        List<Tag> currentTagList = tagRepository.findByUserAndCategory(userId, category.getId());
        currentTagList.forEach(tag -> tagRepository.delete(tag));

        String[] tagList = updateCategoryReqDto.getTags().split("/");

        for (String tagName : tagList) {
            Tag tag = Tag.builder()
                    .tagName(tagName)
                    .category(category)
                    .userId(userId)
                    .build();
            tagRepository.save(tag);
        }
    }

    private void updateCategoryGroup(UpdateCategoryReqDto updateCategoryReqDto, Long userId, Category category) {
        //category의 category 그룹 바꾸기.
        Optional<CategoryGroup> categoryGroupOptional = categoryGroupRepository.findByUserIdAndName(userId, updateCategoryReqDto.getCategory_group());
        CategoryGroup updateCategoryGroup = categoryGroupOptional.get();

        category.setName(updateCategoryReqDto.getCategoryName());
        category.setStar(updateCategoryReqDto.isStar());
        category.setEmoji(updateCategoryReqDto.getEmoji());
        category.setCategoryGroup(updateCategoryGroup);//카테고리가 속한 그룹 바꾸기.
        categoryRepository.save(category);
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
