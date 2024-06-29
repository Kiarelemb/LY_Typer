package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.menu.type.SettingsItem;
import swing.qr.kiarelemb.component.event.QRColorChangedEvent;
import swing.qr.kiarelemb.component.utils.QRRGBColorPane;
import swing.qr.kiarelemb.inter.QRActionRegister;

import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 11:47
 **/
public class RGBColorSelectPane extends QRRGBColorPane {

    public RGBColorSelectPane(Color color, String key) {
        super(color, event -> SettingsItem.CHANGE_MAP.put(key, QRRGBColorPane.getColor(((QRColorChangedEvent) event).to())));
    }

    public RGBColorSelectPane(Color color, String key, QRActionRegister e) {
        super(color, event -> {
            SettingsItem.CHANGE_MAP.put(key, QRRGBColorPane.getColor(((QRColorChangedEvent) event).to()));
            e.action(event);
        });
    }
}