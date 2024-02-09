package ly.qr.kiarelemb.component.menu.type;

import ly.qr.kiarelemb.component.menu.MenuItem;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.inter.QRActionRegister;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-21 20:51
 **/
public class SettingsItem extends MenuItem {

	public static final Map<String, String> changeMap = new TreeMap<>();
	public static final Map<String, QRActionRegister> saveActions = new TreeMap<>();
	public static final Map<String, QRActionRegister> cancelActions = new TreeMap<>();
	public static final SettingsItem SETTINGS_ITEM = new SettingsItem();

	private SettingsItem() {
		super("设置", Keys.QUICK_KEY_SETTING_WINDOW);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		changeMap.clear();
		saveActions.clear();
		cancelActions.clear();
		SettingWindow window = new SettingWindow();
		window.setVisible(true);
		if (window.save()) {
			changeMap.forEach(QRSwing::setGlobalSetting);
			saveActions.forEach((s, e) -> e.action(null));
		} else {
			cancelActions.forEach((s, e) -> e.action(null));
		}
	}
}
