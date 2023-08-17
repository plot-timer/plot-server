package com.plot.plotserver.service;

import com.plot.plotserver.domain.Tag;
import com.plot.plotserver.dto.response.tag.TagResponseDto;
import com.plot.plotserver.exception.tag.TagNotFoundException;
import com.plot.plotserver.repository.TagRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    public Tag searchByTagName(String tagName) {

        Optional<Tag> tagNameOpt = tagRepository.findByTagName(tagName);

        if(!tagNameOpt.isPresent())
            throw new TagNotFoundException("해당 태그가 존재하지 않습니다.");

        return tagNameOpt.get();
    }
}
