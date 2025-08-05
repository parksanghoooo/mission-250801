package com.ll.wisesaying.domain.wiseSaying.repository;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.global.constant.ErrorMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.ll.wisesaying.global.db.config.DBConfig.LAST_ID_FILE;
import static com.ll.wisesaying.global.db.config.DBConfig.getWiseSayingFilePath;
import static com.ll.wisesaying.global.db.util.JsonUtil.objectMapper;

public class WiseSayingRepository {

    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private long lastId;

    public WiseSayingRepository() {
        // 저장된 정보 가져오기
        loadAll();
        loadLastId();
    }

    public WiseSaying create(String content, String author) {
        long id = ++lastId;
        WiseSaying wiseSaying = new WiseSaying(id, content, author);
        wiseSayings.add(wiseSaying);

        saveWiseSaying(wiseSaying);
        saveLastId(id);

        return wiseSaying;
    }

    public List<WiseSaying> findAll() {
        // 안전 복사
        return new ArrayList<>(wiseSayings);
    }

    public void saveWiseSaying(WiseSaying wiseSaying) {
        try {
            Files.createDirectories(getWiseSayingFilePath(0).getParent());
            objectMapper.writeValue(getWiseSayingFilePath(wiseSaying.getId()).toFile(), wiseSaying);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_SAVE_FILE, e);
        }
    }

    public void saveLastId(long lastId) {
        try {
            Files.writeString(LAST_ID_FILE, String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_WRITE_LAST_ID, e);
        }
    }

    public void loadAll() {
        try {
            Files.createDirectories(getWiseSayingFilePath(0).getParent());

            File dir = getWiseSayingFilePath(0).getParent().toFile();
            File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));

            if (files == null) return;

            for (File file : files) {
                try {
                    WiseSaying ws = objectMapper.readValue(file, WiseSaying.class);
                    wiseSayings.add(ws);
                } catch (IOException e) {
                    System.err.println(ErrorMessage.FAIL_TO_LOAD_FILE + file.getName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_LOAD_DIRECTORY, e);
        }
    }

    private void loadLastId() {
        if (!Files.exists(LAST_ID_FILE)) {
            lastId = 0;
            return;
        }

        try {
            String content = Files.readString(LAST_ID_FILE).trim();
            lastId = Long.parseLong(content);
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_LOAD_LAST_ID, e);
        }
    }

}
