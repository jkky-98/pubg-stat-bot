package hello.pubg_stat.repository.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AllStatDto {

    private Integer teamMateCount;
    private String mapName;
    private BigDecimal firstDeathRatio;
    private BigDecimal teamTopTenRatio;
    private BigDecimal teamWinRatio;
    private BigDecimal avgRevives;
    private BigDecimal avgDealt;
    private BigDecimal avgDbnos;

    @Override
    public String toString() {
        return "AllStatDto{" +
                "teamMateCount=" + teamMateCount +
                ", mapName='" + mapName + '\'' +
                ", firstDeathRatio=" + firstDeathRatio +
                ", teamTopTenRatio=" + teamTopTenRatio +
                ", teamWinRatio=" + teamWinRatio +
                ", avgRevives=" + avgRevives +
                ", avgDealt=" + avgDealt +
                ", avgDbnos=" + avgDbnos +
                '}';
    }

    // 기본 생성자
    public AllStatDto() {
    }

    // 모든 필드를 포함한 생성자
    public AllStatDto(Integer teamMateCount, String mapName, BigDecimal firstDeathRatio, BigDecimal teamTopTenRatio,
                      BigDecimal teamWinRatio, BigDecimal avgRevives, BigDecimal avgDealt, BigDecimal avgDbnos) {
        this.teamMateCount = teamMateCount;
        this.mapName = mapName;
        this.firstDeathRatio = firstDeathRatio;
        this.teamTopTenRatio = teamTopTenRatio;
        this.teamWinRatio = teamWinRatio;
        this.avgRevives = avgRevives;
        this.avgDealt = avgDealt;
        this.avgDbnos = avgDbnos;
    }
}
