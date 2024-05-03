package ly.qr.kiarelemb.test;

import ly.qr.kiarelemb.type.KeyTypedRecordWindow;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.QRSwing;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-13 11:43
 **/
public class Test {
    public static void mains(String[] args) {
        QRSwing.start("res/settings/setting.properties", "res/settings/window.properties");
        QRSwing.globalFont = QRFontUtils.getFont("微软雅黑", 15);
        KeyTypedRecordWindow window = new KeyTypedRecordWindow();
        QRSwing.registerGlobalKeyEvents(window);
        window.setSize(1600, 600);
        window.setLocationRelativeTo(null);
        //设置窗体可见
        window.setVisible(true);
    }

    public static void main(String[] args) {
        AtomicInteger index = new AtomicInteger();
        String[] chong = QRStringUtils.splitToCharStr("_234567890");
        AtomicReference<String> prev = new AtomicReference<>();
        LinkedList<String> list = new LinkedList<>();
        QRFileUtils.fileReaderWithUtf8("D:\\backup\\smb.txt", "\t", (lineText, split) -> {
            String word = split[0];
            String code = split[1];
            if (code.length() == 3) {
                if (Objects.equals(prev.get(), code)) {
                    index.getAndIncrement();
                } else {
                    index.set(0);
                }
                code = code + chong[index.get()];
            }
            list.add(word + "\t" + code);
            prev.set(split[1]);
        });
        QRFileUtils.fileWriterWithUTF8("D:\\backup\\[TIP]smb.txt", list);
    }

}