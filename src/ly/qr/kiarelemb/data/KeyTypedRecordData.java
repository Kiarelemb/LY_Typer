package ly.qr.kiarelemb.data;

import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRTimeUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className KeyTypedRecordData
 * @description TODO
 * @create 2024/4/13 14:04
 */
public class KeyTypedRecordData {
    public static final KeyTypedRecordData KEY_TOTAL_RECORD_DATA =
            new KeyTypedRecordData(KeyTypedRecordData.keyTotalPath);
    public static final KeyTypedRecordData KEY_TODAY_RECORD_DATA =
            new KeyTypedRecordData(KeyTypedRecordData.keyTodayPath);
    private static final String keyTotalPath = "res/settings/keyTotal.properties";
    private static final String keyTodayPath = "res/settings/keyToday.properties";

    public final Map<Character, Integer> keyMap;
    private final String keyPath;

    private KeyTypedRecordData(String keyPath) {
        this.keyPath = keyPath;
        keyMap = new HashMap<>();
        QRFileUtils.fileCreate(keyPath);
        String today = QRTimeUtils.getDateNow();
        if (keyTodayPath.equals(keyPath)) {
            String text = QRFileUtils.getFileLineTextWithUtf8(keyPath, 1);
            if (!today.equals(text)) {
                return;
            }
        }
        QRFileUtils.fileReaderWithUtf8(keyPath, "=", ((lineText, split) -> {
            if (split.length != 2) {
                return;
            }
            char code = split[0].charAt(0);
            int times = Integer.parseInt(split[1]);
            keyMap.put(code, times);
        }));
    }

    public static void fresh(String typedKey) {
        KEY_TODAY_RECORD_DATA.flush(typedKey);
        KEY_TOTAL_RECORD_DATA.flush(typedKey);
    }

    private void flush(String typedKey) {
        char[] chars = typedKey.toCharArray();
        for (char c : chars) {
            Integer i = keyMap.get(c);
            keyMap.put(c, i != null ? i + 1 : 1);
        }
        save();
    }

    private void save() {
        List<String> list = new LinkedList<>();
        list.add(QRTimeUtils.getDateNow());
        keyMap.forEach((c, i) -> {
            list.add(c + "=" + i);
        });
        QRFileUtils.fileWriterWithUTF8(this.keyPath, list);
    }
}