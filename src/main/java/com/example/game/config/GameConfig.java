package com.example.game.config;

import com.example.game.util.GameInitializer;
import com.example.game.world.GameWorld;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    @Bean
    public GameWorld getGameWorld() {

        GameWorld gameWorld = GameInitializer.initializeGame();
        gameWorld.setOutputStream(System.out, 60);
        return gameWorld;
    }
}
