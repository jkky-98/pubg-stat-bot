package hello.pubg_stat.service.discord.table;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TableSender {

    public void sendTable(TextChannel channel, List<Map<String, Object>> data) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("지도별 평균 딜량");

        // 열 제목 설정
        String header = "MAP_NAME | AVERAGE_DEALT";
        StringBuilder separator = new StringBuilder();
        separator.append("--- | ---");

        StringBuilder tableRows = new StringBuilder();
        tableRows.append(header).append("\n").append(separator).append("\n");

        // 데이터 행 추가
        for (Map<String, Object> row : data) {
            String mapName = (String) row.get("MAP_NAME");
            Double averageDealt = (Double) row.get("AVERAGE_DEALT");
            tableRows.append(mapName).append(" | ").append(String.format("%.2f", averageDealt)).append("\n");
        }

        embedBuilder.setDescription("```\n" + tableRows.toString() + "\n```"); // Markdown 코드 블록으로 표시

        // Embed 메시지 전송
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
