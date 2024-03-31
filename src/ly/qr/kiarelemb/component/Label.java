package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.component.basic.QRLabel;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-08 15:23
 **/
public class Label extends QRLabel {
	private final String key;

	public Label(String key) {
		super(Keys.strValue(key));
		this.key = key;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		if (this.key != null) {
			SettingsItem.CHANGE_MAP.put(this.key, text);
		}
	}
}