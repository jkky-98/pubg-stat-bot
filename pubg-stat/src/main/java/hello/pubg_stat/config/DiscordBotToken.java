package hello.pubg_stat.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DiscordBotToken {

    @Value("${discord.bot.token}")
    private String discordBotToken;

}
