package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.menu.type.SettingsItem;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import swing.qr.kiarelemb.basic.QRSpinner;
import swing.qr.kiarelemb.utils.QRComponentUtils;

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
			SettingsItem.SAVE_ACTIONS.putIfAbsent("tip.update", es -> {
				if (TextLoad.TEXT_LOAD != null) {
					TextStyleManager.updateAll();
					TextLoad.TEXT_LOAD.updateTipsWithoutEnable();
					// 延迟一秒后刷新文本
					QRComponentUtils.runLater(1000, ee -> TextViewPane.TEXT_VIEW_PANE.simpleRestart());
				}
            });
		});
	}
}