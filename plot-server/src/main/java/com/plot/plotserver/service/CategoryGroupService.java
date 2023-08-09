package com.plot.plotserver.service;


import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.categorygroup.NewCategoryGroupReqDto;
import com.plot.plotserver.exception.categorygroup.CategoryGroupSavedFailException;
import com.plot.plotserver.exception.todo.TodoSavedFailException;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;

    private final UserRepository userRepository;

    @Transactional
    public void saveCategoryGroup(NewCategoryGroupReqDto newCategoryGroupReqDto) {

        try {
            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<User> user = userRepository.findById(userId);

            CategoryGroup categoryGroup = CategoryGroup.builder()
                    .name(newCategoryGroupReqDto.getGroupName())
                    .color(newCategoryGroupReqDto.getColor())
                    .user(user.get())
                    .build();

            categoryGroupRepository.save(categoryGroup);

        }catch(Exception e){
            throw new CategoryGroupSavedFailException("CategoryGroup 생성에 실패하였습니다.");
        }
    }



}
