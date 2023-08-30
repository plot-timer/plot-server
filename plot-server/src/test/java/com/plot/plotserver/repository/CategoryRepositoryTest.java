package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.util.ColorEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@Slf4j
class CategoryRepositoryTest {

    @Autowired UserRepository userRepository;

    @Autowired CategoryGroupRepository categoryGroupRepository;

    @Autowired CategoryRepository categoryRepository;


    @Test
    public void saveCategory(){

        //given

        User user=User.builder()
                .username("gntjd135@naver.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);//user부터 저장.

        CategoryGroup categoryGroup1=CategoryGroup.builder()
                .name("카테고리그룹 1번")
                .color(ColorEnum.BROWN)
                .user(savedUser)
                .build();

        CategoryGroup categoryGroup2=CategoryGroup.builder()
                .name("카테고리그룹 2번")
                .color(ColorEnum.YELLOW)
                .user(savedUser)
                .build();

        CategoryGroup savedCategoryGroup1 = categoryGroupRepository.save(categoryGroup1);//
        CategoryGroup savedCategoryGroup2 = categoryGroupRepository.save(categoryGroup2);//카테고리 그룹 2개 저장.

        Category category1=Category.builder()
                .name("카테고리 1번")
                .star(false)
                .emoji("🇮🇷")
                .categoryGroup(savedCategoryGroup1)
                .build();

        Category category2=Category.builder()
                .name("카테고리 2번")
                .star(true)
                .emoji("♄")
                .categoryGroup(savedCategoryGroup2)
                .build();

        //when
        Category savedCategory1 = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);

        savedCategoryGroup1.addCategory(savedCategory1);
        savedCategoryGroup2.addCategory(savedCategory2);

        //then

        Optional<Category> find_category1 = categoryRepository.findByNameAndCategoryGroupId(savedCategory1.getName(), savedCategory1.getCategoryGroup().getId());
        Optional<Category> find_category2 = categoryRepository.findByNameAndCategoryGroupId(savedCategory2.getName(), savedCategory2.getCategoryGroup().getId());

        log.info("category.emoji={}", find_category1.get().getEmoji());

        Assertions.assertThat(find_category1.get()).isEqualToComparingFieldByFieldRecursively(savedCategory1);//객체의 내용 비교.
        Assertions.assertThat(find_category2.get()).isEqualToComparingFieldByFieldRecursively(savedCategory2);


        List<Category> categories = savedCategoryGroup1.getCategories();
        for (Category category : categories) {
            log.info("category={}", category.getName());
        }

    }

    @Test
    public void updateCategory(){

        //given

        User user=User.builder()
                .username("gntjd135@naver.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);//user부터 저장.

        CategoryGroup categoryGroup1=CategoryGroup.builder()
                .name("카테고리 그룹 1번")
                .color(ColorEnum.BROWN)
                .user(savedUser)
                .build();

        CategoryGroup categoryGroup2=CategoryGroup.builder()
                .name("카테고리 그룹 2번")
                .color(ColorEnum.YELLOW)
                .user(savedUser)
                .build();

        CategoryGroup savedCategoryGroup1 = categoryGroupRepository.save(categoryGroup1);//
        CategoryGroup savedCategoryGroup2 = categoryGroupRepository.save(categoryGroup2);//카테고리 그룹 2개 저장.

        Category category1=Category.builder()
                .name("카테고리 1번")
                .star(false)
                .emoji("🇮🇷")
                .categoryGroup(savedCategoryGroup1)
                .build();

        Category category2=Category.builder()
                .name("카테고리 2번")
                .star(true)
                .emoji("♄")
                .categoryGroup(savedCategoryGroup2)
                .build();


        Category savedCategory1 = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);

        savedCategoryGroup1.addCategory(savedCategory1);
        savedCategoryGroup2.addCategory(savedCategory2);


        //when

        UpdateCategoryReqDto req= UpdateCategoryReqDto.builder()
                .categoryName("카테고리 1번 이름 수정함")
                .star(false)
                .build();// 카테고리 그룹 1번에 속한 카테고리(카테고리 1번)을 카테고리 그룹2번으로 이동.

        Optional<CategoryGroup> changeCategoryGroup = categoryGroupRepository.findByUserIdAndName(savedCategoryGroup2.getUser().getId(), "카테고리 그룹 2번");//바뀔 카테고리 그룹.
        savedCategory1.updateCategory(req, changeCategoryGroup.get());//실제로 업데이트 됨.


        log.info("바뀐 카테고리의 새로운 그룹의 이름={}", savedCategory1.getCategoryGroup().getName());
        log.info("바뀐 카테고리의 새로운 이름={}", savedCategory1.getName());


        //then

        Optional<Category> find_category1 = categoryRepository.findByNameAndCategoryGroupId(savedCategory1.getName(), savedCategory1.getCategoryGroup().getId());
        Optional<Category> find_category2 = categoryRepository.findByNameAndCategoryGroupId(savedCategory2.getName(), savedCategory2.getCategoryGroup().getId());


        Assertions.assertThat(find_category1.get()).isEqualToComparingFieldByFieldRecursively(savedCategory1);//객체의 내용 비교.
        Assertions.assertThat(find_category2.get()).isEqualToComparingFieldByFieldRecursively(savedCategory2);


        List<Category> categories1 = savedCategoryGroup1.getCategories();
        for (Category category : categories1) {
            log.info("categoryGroup1={}", category.getName());
        }
        List<Category> categories2 = savedCategoryGroup2.getCategories();
        for (Category category : categories2) {
            log.info("categoryGroup2={}", category.getName());
        }


    }


    @Test
    public void deleteCategory(){

        //given
        User user=User.builder()
                .username("gntjd135@naver.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);//user부터 저장.

        CategoryGroup categoryGroup1=CategoryGroup.builder()
                .name("카테고리그룹 1번")
                .color(ColorEnum.BROWN)
                .user(savedUser)
                .build();

        CategoryGroup categoryGroup2=CategoryGroup.builder()
                .name("카테고리그룹 2번")
                .color(ColorEnum.YELLOW)
                .user(savedUser)
                .build();

        CategoryGroup savedCategoryGroup1 = categoryGroupRepository.save(categoryGroup1);//
        CategoryGroup savedCategoryGroup2 = categoryGroupRepository.save(categoryGroup2);//카테고리 그룹 2개 저장.

        Category category1=Category.builder()
                .name("카테고리 1번")
                .star(false)
                .emoji("🇮🇷")
                .categoryGroup(savedCategoryGroup1)
                .build();

        Category category2=Category.builder()
                .name("카테고리 2번")
                .star(true)
                .emoji("♄")
                .categoryGroup(savedCategoryGroup2)
                .build();

        Category savedCategory1 = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);

        savedCategoryGroup1.addCategory(savedCategory1);
        savedCategoryGroup2.addCategory(savedCategory2);

        //when
        categoryRepository.delete(savedCategory1);//categoryGroup 삭제.
        savedCategoryGroup1.deleteCategory(savedCategory1);

        //then

        Optional<Category> find_category1 = categoryRepository.findByNameAndCategoryGroupId(savedCategory1.getName(), savedCategory1.getCategoryGroup().getId());
        Optional<Category> find_category2 = categoryRepository.findByNameAndCategoryGroupId(savedCategory2.getName(), savedCategory2.getCategoryGroup().getId());

        List<Category> categories1 = savedCategoryGroup1.getCategories();
        for (Category category : categories1) {
            log.info("category 그룹 1 에 속한 카테고리들 ={}", category.getName());
        }

        List<Category> categories2 = savedCategoryGroup2.getCategories();
        for (Category category : categories2) {
            log.info("category 그룹 2 에 속한 카테고리들 ={}", category.getName());
        }

        Assertions.assertThat(categoryGroupRepository.findByUserIdAndName(savedCategoryGroup1.getUser().getId(), savedCategory1.getName()).isEmpty());



    }


}