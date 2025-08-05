package com.ll.wisesaying.global.constant;

public class ErrorMessage {

    /**
     * [404 Not Found]
     * - 존재하지 않는 자원
     */
    public static final String NOT_EXIST_WISE_SAYING = "%d번 명언은 존재하지 않습니다.\n";

    /**
     * [500 INTERNAL_SERVER_ERROR]
     * - 서버 오류
     */
    public static final String FAIL_TO_SAVE_FILE = "명언 파일 저장 실패";
    public static final String FAIL_TO_DELETE_FILE = "[명언 파일 삭제 실패] 파일명 : { %s.json }\n";
    public static final String FAIL_TO_LOAD_LAST_ID_FILE = "lastId.txt 로딩 실패";
    public static final String FAIL_TO_SAVE_LAST_ID_FILE = "lastId.txt 저장 실패";
    public static final String FAIL_TO_LOAD_BUILD_FILE = "data.json 로딩 실패";
    public static final String FAIL_TO_SAVE_BUILD_FILE = "data.json 생성 실패";

}
