package com.example.game.config;

import com.example.game.service.TextAdventureService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GameRunner implements CommandLineRunner {

    private final TextAdventureService textAdventureService;

    public GameRunner(TextAdventureService textAdventureService) {
        this.textAdventureService = textAdventureService;
    }

    @Override
    public void run(String... args) throws Exception {

        textAdventureService.play(args);
    }
}
