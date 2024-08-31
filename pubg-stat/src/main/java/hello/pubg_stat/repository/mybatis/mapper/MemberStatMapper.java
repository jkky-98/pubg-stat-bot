package hello.pubg_stat.repository.mybatis.mapper;

import hello.pubg_stat.repository.dto.AllStatDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberStatMapper {

    List<Map<String, Object>> getAverageDealtByMapName(String name);

    List<AllStatDto> getAllStat();
}
