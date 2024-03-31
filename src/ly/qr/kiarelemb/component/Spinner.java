package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.component.basic.QRSpinner;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-04-05 11:13
 **/
public class Spinner extends QRSpinner {
	public Spinner(String key) {
		setValue(Keys.intValue(key));
		addChangeListener(e -> {
			String value = getValue().toString();
			SettingsItem.CHANGE_MAP.put(key, value);
			SettingsItem.SAVE_ACTIONS.putIfAbsent(key, es -> TextPane.TEXT_PANE.restart());
		});
	}
}