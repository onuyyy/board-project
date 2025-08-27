package com.web.board_project.dto.request;

import com.web.board_project.dto.ArticleDto;
import com.web.board_project.dto.HashtagDto;
import com.web.board_project.dto.UserAccountDto;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {

    public static ArticleRequest of(String title, String content, String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        Set<HashtagDto> hashtagDtos = (hashtag != null && !hashtag.isBlank()) ?
                Arrays.stream(hashtag.split("\\s+"))
                        .filter(tag -> !tag.isBlank())
                        .map(tag -> tag.startsWith("#") ? tag.substring(1) : tag)
                        .map(HashtagDto::of)
                        .collect(Collectors.toSet()) :
                Set.of();
        
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtagDtos
        );
    }

}