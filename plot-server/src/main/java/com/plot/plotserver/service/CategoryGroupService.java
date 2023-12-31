package com.plot.plotserver.service;


import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.categorygroup.NewCategoryGroupReqDto;
import com.plot.plotserver.dto.request.categorygroup.UpdateCategoryGroupReqDto;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.category.CategoryTodosResponseDto;
import com.plot.plotserver.dto.response.category_group.CategoryGroupResponseDto;
import com.plot.plotserver.exception.categorygroup.CategoryGroupAlreadyExistException;
import com.plot.plotserver.exception.categorygroup.CategoryGroupDeleteFailException;
import com.plot.plotserver.exception.categorygroup.CategoryGroupSavedFailException;
import com.plot.plotserver.exception.categorygroup.CategoryGroupUpdateFailException;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.UserRepository;
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
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;


    @Transactional
    public CategoryGroupResponseDto saveCategoryGroup(NewCategoryGroupReqDto newCategoryGroupReqDto) {

        try {
            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<User> user = userRepository.findById(userId);
            //해당이름의 CategoryGroup이 존재하면, 에러
            Optional<CategoryGroup> optionalCategoryGroup = categoryGroupRepository.findByUserIdAndName(userId, newCategoryGroupReqDto.getGroupName());
            if (optionalCategoryGroup.isPresent()) {//이미 존재하는 카테고리 그룹이다.
                throw new CategoryGroupAlreadyExistException("이미 존재하는 CategoryGroup 입니다.");
            }
            CategoryGroup categoryGroup = CategoryGroup.builder()
                    .name(newCategoryGroupReqDto.getGroupName())
                    .color(newCategoryGroupReqDto.getColor())
                    .user(user.get())
                    .build();

            CategoryGroup save = categoryGroupRepository.save(categoryGroup);
            return CategoryGroupResponseDto.of(save);

        }catch(CategoryGroupAlreadyExistException e){
            throw e;
        }catch(Exception e){
            throw new CategoryGroupSavedFailException("CategoryGroup 생성에 실패하였습니다.");
        }
    }

    @Transactional
    public void update(Long categoryGroupId, UpdateCategoryGroupReqDto updateCategoryGroupReqDto) {

        try {

            Long userId = SecurityContextHolderUtil.getUserId();

            CategoryGroup categoryGroup = categoryGroupRepository.findById(categoryGroupId).get();

            //이름을 변경하려 하는 경우, 변경 하려는 이름이 존재하는지 확인해야함.
            if (!categoryGroup.getName().equals(updateCategoryGroupReqDto.getGroupName())) {

                Optional<CategoryGroup> optionalCategoryGroup = categoryGroupRepository.findByUserIdAndName(userId, updateCategoryGroupReqDto.getGroupName());
                if (optionalCategoryGroup.isPresent()) {//이미 존재하는 카테고리 그룹이다.
                    throw new CategoryGroupAlreadyExistException("이미 존재하는 CategoryGroup 입니다.");
                }
            }

            categoryGroup.updateCategoryGroup(updateCategoryGroupReqDto);


        }catch(CategoryGroupAlreadyExistException e){
            throw e;
        }catch (Exception e){
            throw new CategoryGroupUpdateFailException("Category Group 수정에 실패했습니다.");
        }
    }


    @Transactional
    public void delete(Long categoryGroupId) {
        try {

            Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findById(categoryGroupId);
            categoryGroupRepository.delete(categoryGroup.get());
        }catch (Exception e){
            throw new CategoryGroupDeleteFailException("Category Group 삭제에 실패했습니다.");
        }

    }


//    public List<CategoryGroupResponseDto> getAll(Long userId) {//카테고리 그룹, 카테고리, 카테고리 안의 태그 정보들도 가져옴, 이 부분 최적화 못하겠음.
//
//        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findByUserIdWithCategories(userId);//카테고리 그룹과 그 카테고리들을 가져온다.
//        List<CategoryGroupResponseDto> result = new ArrayList<>();
//
//        categoryGroupList.forEach(categoryGroup -> {
//            result.add(CategoryGroupResponseDto.of(categoryGroup));
//        });
//        return result;
//    }

    @Transactional
    public List<CategoryGroupResponseDto> getAll(Long userId) {//카테고리 그룹, 카테고리, 태그들까지.
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findByUserIdJoinCategories(userId);
        List<CategoryGroupResponseDto> result = new ArrayList<>();

        for (CategoryGroup categoryGroup : categoryGroupList) {
            List<CategoryResponseDto.Sub> categoryResponseList = new ArrayList<>();

            for (Category category : categoryGroup.getCategories()) {

                //category마다, 태그까지 join해서 가져오기
                categoryRepository.findByIdJoinTags(category.getId());
                CategoryResponseDto.Sub categoryResponse = CategoryResponseDto.Sub.of(category);
                categoryResponseList.add(categoryResponse);
            }

            CategoryGroupResponseDto categoryGroupResponse = CategoryGroupResponseDto.builder()
                    .category_group_id(categoryGroup.getId())
                    .category_group_name(categoryGroup.getName())
                    .color(categoryGroup.getColor().name())
                    .category_list(categoryResponseList)
                    .build();

            result.add(categoryGroupResponse);
        }

        return result;
    }

    @Transactional
    public List<CategoryGroupResponseDto.InDailyTodoAdd> getAllTodos(Long userId) {//카테고리 그룹, 카테고리, 투드들까지.
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findByUserIdJoinCategories(userId);
        List<CategoryGroupResponseDto.InDailyTodoAdd> result = new ArrayList<>();

        for (CategoryGroup categoryGroup : categoryGroupList) {
            List<CategoryTodosResponseDto> categoryTodosResponseList = new ArrayList<>();

            for (Category category : categoryGroup.getCategories()) {

                //category마다, 투드들까지.
                categoryRepository.findByIdJoinTodos(category.getId());
                CategoryTodosResponseDto categoryTodosResponse = CategoryTodosResponseDto.of(category);
                categoryTodosResponseList.add(categoryTodosResponse);
            }

            CategoryGroupResponseDto.InDailyTodoAdd categoryGroupResponse = CategoryGroupResponseDto.InDailyTodoAdd.builder()
                    .category_group_id(categoryGroup.getId())
                    .category_group_name(categoryGroup.getName())
                    .categoryList(categoryTodosResponseList)
                    .build();

            result.add(categoryGroupResponse);
        }

        return result;
    }


    @Transactional
    public List<CategoryGroupResponseDto.InCategoryAdd> getAllCategoryGroup(Long userId) {//완료

        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findByUserId(userId);
        List<CategoryGroupResponseDto.InCategoryAdd> result = new ArrayList<>();

        categoryGroupList.forEach(categoryGroup -> {
            result.add(CategoryGroupResponseDto.InCategoryAdd.of(categoryGroup));
        });
        return result;
    }

    public List<CategoryGroupResponseDto.InTodoAdd> getAllCategoryPath(Long userId) {//완료

        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findByUserIdJoinCategories(userId);
        List<CategoryGroupResponseDto.InTodoAdd> result = new ArrayList<>();

        categoryGroupList.forEach(categoryGroup -> {
            result.add(CategoryGroupResponseDto.InTodoAdd.of(categoryGroup));
        });
        return result;
    }
}
