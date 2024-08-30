package hello.pubg_stat;

import hello.pubg_stat.config.DiscordBotToken;
import hello.pubg_stat.util.TestDiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PubgStatApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PubgStatApplication.class, args);
		DiscordBotToken discordBotTokenEntity = context.getBean(DiscordBotToken.class);
		String discordBotToken = discordBotTokenEntity.getDiscordBotToken();

		JDA jda = JDABuilder.createDefault(discordBotToken)
				.setActivity(Activity.playing("메세지 기다리는 중..."))
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(context.getBean(TestDiscordListener.class))
				.build();
	}

}
