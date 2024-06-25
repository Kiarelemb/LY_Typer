package ly.qr.kiarelemb.dl;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRPropertiesUtils;
import method.qr.kiarelemb.utils.QRStringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Kiarelemb QR
 */
public class DangLangManager {

    private final ArrayList<DangLangData> list;
    private final File typedRecordFile;
    private final Set<String> paraMd5Set;
    public static final DangLangManager DANG_LANG_MANAGER = new DangLangManager();

    private DangLangManager() {
        list = new ArrayList<>(1000);
        typedRecordFile = new File(Info.DL_DIRECTORY + "typed.txt");
        QRFileUtils.fileCreate(typedRecordFile);
        paraMd5Set = new HashSet<>(QRFileUtils.fileReaderWithUtf8(typedRecordFile));
        TypingData.addStartTypingAction(event -> {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            DangLangWindow.dangLangWindow().logTextPane.setText("按键 - 时间间隔/总计时");
        });
    }

    public void clear() {
        list.clear();
    }

    public void put(char c, long time) {
        if (!MainWindow.INSTANCE.isFocused()) {
            return;
        }
        if (!TypingData.typing) {
            return;
        }
        if (c == '⊗') {
            return;
        }
        list.add(new DangLangData(c, time));
        String data;
        int size = list.size();
        if (size > 1) {
            data = c + " - " + (time - list.get(list.size() - 2).time()) + " / " + time;
        } else {
            data = c + " - " + time;
        }
        printAction(data);
    }

    public void save(String paraMd5) {
        if (list.isEmpty()) {
            return;
        }
        ArrayList<String> paraOriginalData = new ArrayList<>();
        Map<String, PartData> map = new HashMap<>();
        for (int i = 1, listSize = list.size(); i < listSize; i++) {
            DangLangData thisData = list.get(i);
            DangLangData foreData = list.get(i - 1);
            String combo = foreData.c() + "" + thisData.c();
            int diff = Math.toIntExact(thisData.time() - foreData.time());
            paraOriginalData.add(combo + "=" + diff);
            PartData data = map.getOrDefault(combo, new PartData(0, 0));
            map.put(combo, new PartData(data.time() + diff, data.times() + 1));
        }
        Properties prop;
        File propFile;
        if (failPara(paraMd5)) {
            propFile = new File(Info.DL_DIRECTORY + "首打.properties");
        } else {
            propFile = new File(Info.DL_DIRECTORY + "重打.properties");
        }
        QRFileUtils.fileCreate(propFile);
        prop = QRPropertiesUtils.loadProp(propFile.getAbsolutePath());
        map.forEach((combo, partData) -> {
            String p = QRPropertiesUtils.getPropInString(prop, combo, "0/0");
            int[] values = QRStringUtils.splitWithNotNumber(p);
            values[0] += partData.time();
            values[1] += partData.times();

            String data = values[0] + "/" + values[1] + "/" + QRMathUtils.doubleFormat(values[0] / (0.0 + values[1]));
            prop.put(combo, data);
        });
        String split = "-".repeat(22);
        printAction(split);

        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> {
            PartData values = e.getValue();
            String data = e.getKey() + " = " + values.time() + " / " + values.times() + " / " + QRMathUtils.doubleFormat(values.time() / (0.0 + values.times()));
            printAction(data);
        });

//		System.out.println("组合键=累积间隔/频率/均时");
        DangLangWindow.dangLangWindow().logTextPane.chinesePrintln("组合键=累积间隔/频率/均时");
        ArrayList<String> arr = new ArrayList<>(prop.size());
        prop.keySet().stream().sorted().forEach(o -> {
            String text = o + "=" + prop.get(o.toString());
            arr.add(text);
        });
        QRFileUtils.fileWriterWithUTF8(propFile.getAbsolutePath(), arr);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dirPatter = DateTimeFormatter.ofPattern("yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator);
        DateTimeFormatter fileNamePatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String filePath = Info.DL_DIRECTORY + now.format(dirPatter) + File.separator + now.format(fileNamePatter) + "-" + TextLoad.TEXT_LOAD.textMD5Short() + ".txt";
        QRFileUtils.fileCreate(filePath);
        QRFileUtils.fileWriterWithUTF8(filePath, paraOriginalData);
        clear();
    }

    private static void printAction(String data) {
//		System.out.println(data);
        if (DangLangWindow.dangLangWindow().isVisible()) {
            DangLangWindow.dangLangWindow().logTextPane.noneChinesePrintln(data);
        }
    }

    /**
     * 判断文本是否是首打
     *
     * @param paraMd5 文本长md5值
     * @return {@code true} 则是首打，否则是重打
     */
    public boolean failPara(String paraMd5) {
        if (paraMd5Set.add(paraMd5)) {
            QRFileUtils.fileWriterWithUTF8(typedRecordFile.getAbsolutePath(), paraMd5Set);
            return true;
        }
        return false;
    }

}