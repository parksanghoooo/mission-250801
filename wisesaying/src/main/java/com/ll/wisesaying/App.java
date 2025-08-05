package com.ll.wisesaying;

import com.ll.wisesaying.global.constant.Command;
import com.ll.wisesaying.global.constant.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void run() throws IOException {

        System.out.println(Message.APP_TITLE);

        while (true) {

            System.out.println(Message.INPUT);
            String cmd = br.readLine().trim();

            // 종료
            if (cmd.equals(Command.QUIT)) break;

        }

    }

}
