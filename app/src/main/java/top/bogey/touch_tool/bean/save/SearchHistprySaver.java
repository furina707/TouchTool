package top.bogey.touch_tool.bean.save;

import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchHistprySaver {
    private static SearchHistprySaver instance;

    public static SearchHistprySaver getInstance() {
        synchronized (SearchHistprySaver.class) {
            if (instance == null) {
                instance = new SearchHistprySaver();
            }
        }
        return instance;
    }

    private final MMKV mmkv = MMKV.mmkvWithID("SEARCH_HISTORY_DB", MMKV.SINGLE_PROCESS_MODE);

    public void addSearchHistory(String history) {
        mmkv.encode(history, true);
    }

    public void removeSearchHistory(String history) {
        mmkv.remove(history);
    }

    public void cleanSearchHistory() {
        mmkv.clearAll();
    }

    public List<String> getSearchHistory() {
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        String[] keys = mmkv.allKeys();
        if (keys == null) return list;
        for (String key : keys) {
            if (mmkv.decodeBool(key) && !set.contains(key)) {
                list.add(key);
                set.add(key);
            }
            if (list.size() >= 10) break;
        }
        return list;
    }
}
