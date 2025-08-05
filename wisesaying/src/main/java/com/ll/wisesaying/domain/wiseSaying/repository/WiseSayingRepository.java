package com.ll.wisesaying.domain.wiseSaying.repository;

import com.ll.wisesaying.domain.wiseSaying.model.entity.WiseSaying;
import com.ll.wisesaying.global.constant.ErrorMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ll.wisesaying.global.db.config.DBConfig.LAST_ID_FILE;
import static com.ll.wisesaying.global.db.config.DBConfig.getWiseSayingFilePath;
import static com.ll.wisesaying.global.db.util.JsonUtil.objectMapper;

public class WiseSayingRepository {

    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private long lastId;

    public WiseSayingRepository() {
        // 저장된 정보 가져오기
        loadLastId();
        loadAll();
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
        List<WiseSaying> descWiseSayings = new ArrayList<>(wiseSayings); // 데이터 복제 (안전 복사)
        descWiseSayings.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        return descWiseSayings;
    }

    public Optional<WiseSaying> findById(long id) {
        List<WiseSaying> allWiseSayings = new ArrayList<>(wiseSayings);

        for (WiseSaying wiseSaying : allWiseSayings) {
            if (wiseSaying.getId() == id)
                return Optional.of(wiseSaying);
        }

        return Optional.empty();
    }

    public boolean deleteById(long id) {
        Optional<WiseSaying> optional = findById(id);

        if (optional.isEmpty()) {
            return false;
        }

        wiseSayings.remove(optional.get());

        try {
            Files.deleteIfExists(getWiseSayingFilePath(id));
        } catch (IOException e) {
            System.err.printf(ErrorMessage.FAIL_TO_DELETE_FILE, id);
        }

        return true;
    }

    public void update(WiseSaying wiseSaying, String newContent, String newAuthor) {
        wiseSaying.setContent(newContent);
        wiseSaying.setAuthor(newAuthor);
        saveWiseSaying(wiseSaying); // 덮어쓰기
    }

    private void saveWiseSaying(WiseSaying wiseSaying) {
        try {
            Files.createDirectories(getWiseSayingFilePath(0).getParent());
            objectMapper.writeValue(getWiseSayingFilePath(wiseSaying.getId()).toFile(), wiseSaying);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_SAVE_FILE, e);
        }
    }

    private void saveLastId(long lastId) {
        try {
            Files.writeString(LAST_ID_FILE, String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.FAIL_TO_WRITE_LAST_ID, e);
        }
    }

    private void loadAll() {
        Path dirPath = getWiseSayingFilePath(0).getParent();

        if (!Files.exists(dirPath)) System.err.printf(ErrorMessage.FAIL_TO_LOAD_DIRECTORY, dirPath.getFileName());;

        File dir = dirPath.toFile();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));

        if (files == null) return;

        for (File file : files) {
            try {
                WiseSaying ws = objectMapper.readValue(file, WiseSaying.class);
                wiseSayings.add(ws);
            } catch (IOException e) {
                System.err.printf(ErrorMessage.FAIL_TO_LOAD_FILE, file.getName());
            }
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
