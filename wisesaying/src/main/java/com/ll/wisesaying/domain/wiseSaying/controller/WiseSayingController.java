package com.ll.wisesaying.domain.wiseSaying.controller;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.domain.wiseSaying.service.WiseSayingService;

public class WiseSayingController {

    private final WiseSayingService service;

    public WiseSayingController() {
        this.service = new WiseSayingService();
    }

    public void create(String content, String author) {
        WiseSaying saved = service.create(content, author);
        System.out.printf("%d번 명언이 등록되었습니다.\n", saved.getId());
    }

}
