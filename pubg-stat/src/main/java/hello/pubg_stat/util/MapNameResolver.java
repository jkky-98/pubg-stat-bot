package hello.pubg_stat.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapNameResolver {

    private Map<String, String> mapMap = new ConcurrentHashMap<>() {{
        put("Baltic_Main", "Erangel");
        put("Chimera_Main", "Paramo");
        put("Desert_Main", "Miramar");
        put("DihorOtok_Main", "Vikendi");
        put("Erangel_Main", "Erangel");
        put("Heaven_Main", "Haven");
        put("Kiki_Main", "Deston");
        put("Range_Main", "Camp Jackal");
        put("Savage_Main", "Sanhok");
        put("Summerland_Main", "Karakin");
        put("Tiger_Main", "Taego");
        put("Neon_Main", "Rondo");
    }};

    public Boolean valid(String prevName) {
        return mapMap.containsKey(prevName);
    }

    public String process(String prevName) {
        if (valid(prevName)) {
            return mapMap.get(prevName);
        } else {
            return null;
        }
    }
}
