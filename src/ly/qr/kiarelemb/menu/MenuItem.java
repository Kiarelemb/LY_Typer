package ly.qr.kiarelemb.menu;

import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.basic.QRMenuItem;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-25 15:16
 **/
public class MenuItem extends QRMenuItem {
    public MenuItem(String text, String key) {
        super(text, key == null ? null : Keys.strValue(key), true);
    }

    public MenuItem(String text, String key, boolean mainWindowFocus) {
        super(text, key == null ? null : Keys.strValue(key), mainWindowFocus);
    }
}