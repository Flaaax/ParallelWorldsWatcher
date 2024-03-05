package ParallelWatcher.util;

import java.util.ArrayList;
import java.util.List;

public class MyArrayUtil {
    public static <T> T safeGet(ArrayList<T> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            return null;
        }
    }

    public static <T> T safeGet(List<T> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            return null;
        }
    }
}
