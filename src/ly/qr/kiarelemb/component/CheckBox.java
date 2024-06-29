package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRCheckBox;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-31 15:39
 **/
public class CheckBox extends QRCheckBox {
    private final String key;
    private final boolean setting;
    private boolean checked;

    public CheckBox(String text, String key) {
        this(text, key, true);
    }

    public CheckBox(String text, String key, boolean setting) {
        super(text);
        this.key = key;
        this.setting = setting;
        this.checked = Keys.boolValue(this.key);
        setSelected(this.checked);
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        this.checked = b;
        update();
    }

    @Override
    protected final void actionEvent(ActionEvent o) {
        this.checked = !this.checked;
        setSelected(this.checked);
        update();
    }

    public boolean checked() {
        return this.checked;
    }

    private void update() {
        QRComponentUtils.windowFresh(this);
        if (setting) {
            SettingsItem.CHANGE_MAP.put(this.key, String.valueOf(this.checked));
        } else if (Keys.boolValue(this.key) != this.checked) {
            QRSwing.setGlobalSetting(this.key, String.valueOf(this.checked));
        }
    }
}