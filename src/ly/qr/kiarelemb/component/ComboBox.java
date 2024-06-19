package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.event.QRItemEvent;
import swing.qr.kiarelemb.component.utils.QRFontComboBox;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 14:35
 **/
public class ComboBox extends QRComboBox {

    private static Logger logger = QRLoggerUtils.getLogger(ComboBox.class);

    /**
     * 在设置中使用的
     *
     * @param key   {@code int} 键值
     * @param array 数组
     */
    public ComboBox(String key, String... array) {
        super(array);
        addItemChangeListener(e -> {
            QRItemEvent event = (QRItemEvent) e;
            int index = QRArrayUtils.objectIndexOf(array, event.after());
            SettingsItem.CHANGE_MAP.put(key, String.valueOf(index));
        });
        int index = Keys.intValue(key);
        if (index >= array.length) {
            setSelectedIndex(Integer.parseInt(Keys.DEFAULT_MAP.get(key)));
        } else {
            setSelectedIndex(index);
        }
    }

    public ComboBox(int i, String key, String... array) {
        super(array);
        addItemChangeListener(e -> {
            QRItemEvent event = (QRItemEvent) e;
            int index = QRArrayUtils.objectIndexOf(array, event.after());
            QRSwing.setGlobalSetting(key, String.valueOf(index));
        });
        int index = Keys.intValue(key);
        if (index >= array.length) {
            setSelectedIndex(Integer.parseInt(Keys.DEFAULT_MAP.get(key)));
        } else {
            setSelectedIndex(index);
        }
    }


    public static class FontComboBox extends QRFontComboBox {
        public FontComboBox(String key) {
            super(false);
            String value = Keys.strValue(key);
            if (value != null) {
                setText(value);
            }
            addItemChangeListener(e -> {
                QRItemEvent event = (QRItemEvent) e;
                SettingsItem.CHANGE_MAP.put(key, event.after());
                logger.log(Level.CONFIG, "已选择字体: %s", event.after());
            });
        }
    }
}