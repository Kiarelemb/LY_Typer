package ly.qr.kiarelemb.test;

import ly.qr.kiarelemb.type.KeyTypedRecordWindow;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.QRSwing;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-13 11:43
 **/
public class Test {
    public static void main(String[] args) {
        QRSwing.start("res/settings/setting.properties", "res/settings/window.properties");
        QRSwing.globalFont = QRFontUtils.getFont("微软雅黑", 15);
        KeyTypedRecordWindow window = new KeyTypedRecordWindow();
        QRSwing.registerGlobalKeyEvents(window);
        window.setSize(1600, 600);
        window.setLocationRelativeTo(null);
        //设置窗体可见
        window.setVisible(true);
    }
}