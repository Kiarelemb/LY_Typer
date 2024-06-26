package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.basic.QRTextField;

import javax.swing.event.DocumentEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 14:59
 **/
public class TextField extends QRTextField {
    private final String key;

    public TextField(String key) {
        String s = Keys.strValue(key);
        if (s != null) {
            setText(s);
        }
        addDocumentListener();
        this.key = key;
    }

    @Override
    protected void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    protected void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    protected void changedUpdate(DocumentEvent e) {
        SettingsItem.CHANGE_MAP.put(this.key, getText());
    }
}