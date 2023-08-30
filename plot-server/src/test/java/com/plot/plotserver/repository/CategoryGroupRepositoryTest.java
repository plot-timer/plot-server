package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.categorygroup.UpdateCategoryGroupReqDto;
import com.plot.plotserver.util.ColorEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



//@SpringBootTest
@DataJpaTest
@Slf4j
class CategoryGroupRepositoryTest {

    @Autowired  UserRepository userRepository;

    @Autowired CategoryGroupRepository categoryGroupRepository;

    @Autowired CategoryRepository categoryRepository;

    @PersistenceContext
    EntityManager em;



    @BeforeEach
    void beforeEach(){



    }
    @Test
    public void saveCategoryGroup(){

        //given

        User user=User.builder()
                .username("gntjd135@naver.com")
                .password("password")
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);

        //when

        CategoryGroup categoryGroup=CategoryGroup.builder()
                .name("카테고리 1번")
                .color(ColorEnum.BROWN)
                .user(savedUser)
                .build();

        CategoryGroup savedCategoryGroup = categoryGroupRepository.save(categoryGroup);
        user.addCategoryGroup(savedCategoryGroup);

        //then

        Optional<CategoryGroup> findCategoryGroup = categoryGroupRepository.findByUserIdAndName(savedCategoryGroup.getId(), savedCategoryGroup.getName());
        Assertions.assertThat(savedCategoryGroup).isEqualTo(findCategoryGroup.get());//is equalto는 객체의 참조를 비교한다.
        log.info("categoryGroup.id={}", findCategoryGroup.get().getId());
        log.info("categoryGroup.name={}", findCategoryGroup.get().getName());
        log.info("categoryGroup.color={}", findCategoryGroup.get().getColor());
        log.info("categoryGroup.user={}", findCategoryGroup.get().getUser());

        User finduser = findCategoryGroup.get().getUser();
        List<CategoryGroup> categoryGroups = finduser.getCategoryGroups();
        for (CategoryGroup group : categoryGroups) {
            log.info("categorygroups={}",group.getName());
        }
    }

    @Test
    public void updateCategoryGroup(){

        //given
        User user=User.builder()
                .username("gntjd135@naver.com")
                .password("dsfs")
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);//user먼저 생성

        CategoryGroup categoryGroup=CategoryGroup.builder()
                .name("카테고리 그룹 1번")
                .color(ColorEnum.BROWN)
                .user(savedUser)
                .build();

        CategoryGroup savedCategoryGroup = categoryGroupRepository.save(categoryGroup);//카테고리 그룹 먼저 저장.
        log.info("categoryGroup.name={}", savedCategoryGroup.getName());

        //when
        UpdateCategoryGroupReqDto reqDto =UpdateCategoryGroupReqDto.builder()
                .groupName("카테고리 그룹 변경")
                .color(ColorEnum.GREEN)
                .build();

        savedCategoryGroup.updateCategoryGroup(reqDto);


        //then
        Optional<CategoryGroup> findCategoryGroup = categoryGroupRepository.findByUserIdAndName(savedCategoryGroup.getId(), savedCategoryGroup.getName());
        Assertions.assertThat(savedCategoryGroup).isEqualTo(findCategoryGroup.get());

        log.info("CategoryGroup.name={}", findCategoryGroup.get().getName());
        log.info("CategoryGroup.color={}", findCategoryGroup.get().getColor());
    }


    @Test
    public void deleteCategoryGroup(){
        //given
        User user=User.builder()
                .username("gntjd135@naver.com")
                .password("dsfs")
                .createdAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);//user먼저 생성

        CategoryGroup categoryGroup=CategoryGroup.builder()
                .name("카테고리 그룹 1번")
                .color(ColorEnum.BROWN)
                .user(savedUser)
                .build();

        CategoryGroup savedCategoryGroup = categoryGroupRepository.save(categoryGroup);//카테고리 그룹 먼저 저장.

        //when
        categoryGroupRepository.delete(savedCategoryGroup);//categoryGroup 삭제.

        //then
        Assertions.assertThat(categoryGroupRepository.findByUserIdAndName(savedCategoryGroup.getId(), savedCategoryGroup.getName()).isEmpty());

    }

    @Test
    public void findByUserIdWithCategories(){

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

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        //when
        em.flush();//db에 쿼리 날리기.
        em.clear();//영속성 컨텍스트 비우기.

        List<CategoryGroup> findGroups = categoryGroupRepository.findByUserIdWithCategories(1L);

        Category find_category1 = categoryRepository.findById(1L).get();
        Category find_category2 = categoryRepository.findById(2L).get();

        //then
        Assertions.assertThat(findGroups.get(0).getCategories().get(0)).isEqualTo(find_category1);
        Assertions.assertThat(findGroups.get(1).getCategories().get(0)).isEqualTo(find_category2);

    }

    @Test
    public void findByUserId(){//카테고리 그룹만 조회.

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

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        //when
        em.flush();//db에 쿼리 날리기.
        em.clear();//영속성 컨텍스트 비우기.

        List<CategoryGroup> findGroups = categoryGroupRepository.findByUserId(1L);

        Category find_category1 = categoryRepository.findById(1L).get();
        Category find_category2 = categoryRepository.findById(2L).get();

        //then
        Assertions.assertThat(findGroups.get(0).getCategories().get(0)).isEqualTo(find_category1);
        Assertions.assertThat(findGroups.get(1).getCategories().get(0)).isEqualTo(find_category2);

    }


}