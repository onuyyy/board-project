package com.web.board_project.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    // 리스트 형태 숫자로 페이징 반환하기
    // 현재 페이지 번호, 마지막 페이지 번호
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {

        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0); // 중앙 값으로 찾아가기 위해
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
