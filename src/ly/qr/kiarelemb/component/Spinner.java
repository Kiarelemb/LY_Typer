package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.menu.type.SettingsItem;
import swing.qr.kiarelemb.basic.QRSpinner;

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
			SettingsItem.SAVE_ACTIONS.putIfAbsent(key, es -> TextViewPane.TEXT_VIEW_PANE.restart());
		});
	}
}