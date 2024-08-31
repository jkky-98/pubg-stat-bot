package hello.pubg_stat.util;

import hello.pubg_stat.domain.Member;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.service.discord.message.MessageService;
import hello.pubg_stat.service.discord.table.TableSender;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class TestDiscordListener extends ListenerAdapter {

    @Autowired private MessageService messageService;
    @Autowired private TableSender tableSender;
    @Autowired private MemberRepository memberRepository;

    @Override
    @Transactional
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel textChannel = event.getChannel().asTextChannel();
        Message message = event.getMessage();

        log.info(" get message : {}", message.getContentDisplay());

        if (user.isBot()) {
            return;
        } else if (message.getContentDisplay().equals("")) {
            log.info("디스코드 Message 문자열 공백");
        }

        String messageString = message.getContentDisplay();
        String discordId = user.getName();

        Optional<Member> byDiscordId = memberRepository.findByDiscordId(discordId);
        Member member = byDiscordId.get();
        String userName = member.getUserName();

        switch (messageString) {
            case "딜량" : tableSender.sendTableGetDealt(textChannel, messageService.getDealt(user.getName()), userName);
            case "전체" : tableSender.sendAllStatResults(textChannel, messageService.getAllStat());
        }

    }

    private String sendMessage(MessageReceivedEvent event, String message) {
        User user = event.getAuthor();
        String returnMessage = "";

        switch (message) {
            case "안녕" : returnMessage = user.getName() + "님 안녕하세요 당신만을 위한 봇이에요.";
            break;
            case "Hi" : returnMessage = "영어는 몰른다 짜식아;;;";
        }

        return returnMessage;
    }
}
