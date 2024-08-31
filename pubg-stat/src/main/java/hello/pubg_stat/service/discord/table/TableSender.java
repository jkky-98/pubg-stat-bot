package hello.pubg_stat.service.discord.table;

import hello.pubg_stat.repository.dto.AllStatDto;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class TableSender {


//    public void sendTable(TextChannel channel, List<Map<String, Object>> data, String userName) {
//        EmbedBuilder embedBuilder = new EmbedBuilder();
//        embedBuilder.setTitle(userName + "지도별 평균 딜량");
//
//        // 설명 추가
//        embedBuilder.setDescription("8월 16일부터의 정보부터 보관이 시작되었습니다. 해당 시점 이후의 정보만 반영됩니다.");
//
//        // 열 제목 설정
//        String header = "MAP_NAME | AVERAGE_DEALT";
//        StringBuilder separator = new StringBuilder();
//        separator.append("--- | ---");
//
//        StringBuilder tableRows = new StringBuilder();
//        tableRows.append(header).append("\n").append(separator).append("\n");
//
//        // 데이터 행 추가
//        for (Map<String, Object> row : data) {
//            String mapName = (String) row.get("MAP_NAME");
//            Double averageDealt = (Double) row.get("AVERAGE_DEALT");
//            tableRows.append(mapName).append(" | ").append(String.format("%.2f", averageDealt)).append("\n");
//        }
//
//        embedBuilder.setDescription("```\n" + tableRows.toString() + "\n```"); // Markdown 코드 블록으로 표시
//
//        // Embed 메시지 전송
//        channel.sendMessageEmbeds(embedBuilder.build()).queue();
//    }
    public void sendTableGetDealt(TextChannel channel, List<Map<String, Object>> data, String userName) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(userName + " 지도별 평균 딜량");

        // 설명 추가
        embedBuilder.setDescription("8월 16일부터의 정보부터 보관이 시작되었습니다. 해당 시점 이후의 정보만 반영됩니다.");

        // 데이터 행 추가
        for (Map<String, Object> row : data) {
            String mapName = (String) row.get("MAP_NAME");
            Double averageDealt = (Double) row.get("AVERAGE_DEALT");
            embedBuilder.addField(mapName, String.format("%.2f", averageDealt), true);
        }

        // Embed 메시지 전송
        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public void sendAllStatResults(TextChannel channel, List<AllStatDto> results) {
        // 결과를 팀 인원 수별로 그룹화
        Map<Integer, List<AllStatDto>> groupedResults = results.stream()
                .collect(Collectors.groupingBy(AllStatDto::getTeamMateCount));

        // 각 그룹에 대해 메시지 전송
        for (Map.Entry<Integer, List<AllStatDto>> entry : groupedResults.entrySet()) {
            int teamMateCount = entry.getKey();
            List<AllStatDto> stats = entry.getValue();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("전체 스텟 : 팀 인원 수 " + teamMateCount);
            embedBuilder.setColor(Color.BLUE);

            // 각 결과를 필드로 추가
            for (AllStatDto result : stats) {
                embedBuilder.addField(
                        "맵: " + result.getMapName(),
                        String.format(
                                "팀원 초반 낙오 비율: %.3f\n" +
                                        "탑텐 비율: %.3f\n" +
                                        "승률: %.3f\n" +
                                        "소생률: %.3f\n" +
                                        "딜량 평균: %.3f\n" +
                                        "기절시킴 평균: %.3f",
                                result.getFirstDeathRatio(),
                                result.getTeamTopTenRatio(),
                                result.getTeamWinRatio(),
                                result.getAvgRevives(),
                                result.getAvgDealt(),
                                result.getAvgDbnos()
                        ),
                        false // Inline은 false로 설정
                );
            }

            // 디스코드 채널에 메시지 전송
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
