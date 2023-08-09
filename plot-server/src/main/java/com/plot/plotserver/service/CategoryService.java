package com.plot.plotserver.service;


import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.dto.request.category.NewCategoryReqDto;
import com.plot.plotserver.exception.Category.CategorySavedFailException;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryGroupRepository categoryGroupRepository;


    @Transactional
    public void save(NewCategoryReqDto categoryReqDto) {

        try {


            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findByUserIdAndName(userId, categoryReqDto.getCategory_group());


           Category category=Category.builder()
                   .name(categoryReqDto.getCategoryName())
                   .star(false)
                   .emoji(categoryReqDto.getEmoji())
                   .categoryGroup(categoryGroup.get())
                   .build();

            categoryRepository.save(category);

        } catch (Exception e) {
            throw new CategorySavedFailException("Category 생성에 실패하였습니다.");
        }
    }

}
