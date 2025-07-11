package com.web.board_project.domain.constant;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),

    CONTENT("본문"),

    ID("유저 ID"),

    NICKNAME("닉네임"),

    HASHTAG("해시태그");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }

    /*
        public static final SearchType TITLE = new SearchType("제목"); >> 이것과 마찬가지
        TITLE 상수를 생성하면서 "description" 을 생성자에 넘김

        System.out.println(SearchType.TITLE.getDescription()); // 출력 : 제목
        System.out.println(SearchType.TITLE); // 출력 : TITLE
     */
}
