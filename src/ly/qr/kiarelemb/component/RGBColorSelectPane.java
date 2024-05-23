package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import swing.qr.kiarelemb.component.event.QRColorChangedEvent;
import swing.qr.kiarelemb.component.utils.QRRGBColorPane;

import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 11:47
 **/
public class RGBColorSelectPane extends QRRGBColorPane {

    public RGBColorSelectPane(Color color, String key) {
        super(SettingWindow.INSTANCE, color,
                event -> SettingsItem.CHANGE_MAP.put(key, QRRGBColorPane.getColor(((QRColorChangedEvent) event).to())));
    }
}