package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
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
	private boolean checked;

	public CheckBox(String text, String key) {
		super(text);
		this.key = key;
		this.checked = Keys.boolValue(this.key);
		setSelected(this.checked);
	}

	@Override
	public void setSelected(boolean b) {
		super.setSelected(b);
		this.checked = b;
		SettingsItem.changeMap.put(this.key, String.valueOf(this.checked));
	}

	@Override
	protected final void actionEvent(ActionEvent o) {
		this.checked = !this.checked;
		setSelected(this.checked);
		SettingsItem.changeMap.put(this.key, String.valueOf(this.checked));
	}

	public boolean checked() {
		return this.checked;
	}
}
