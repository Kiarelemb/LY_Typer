package ly.qr.kiarelemb.component.menu.type;

import ly.qr.kiarelemb.MainWindow;
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

	public static final Map<String, String> CHANGE_MAP = new TreeMap<>();
	public static final Map<String, QRActionRegister> SAVE_ACTIONS = new TreeMap<>();
	public static final Map<String, QRActionRegister> CANCEL_ACTIONS = new TreeMap<>();
	public static final SettingsItem SETTINGS_ITEM = new SettingsItem();

	private SettingsItem() {
		super("设置", Keys.QUICK_KEY_SETTING_WINDOW);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		CHANGE_MAP.clear();
		SAVE_ACTIONS.clear();
		CANCEL_ACTIONS.clear();
		SettingWindow window = new SettingWindow();
		window.setLocationRelativeTo(MainWindow.INSTANCE);
		window.setVisible(true);
		if (window.save()) {
			CHANGE_MAP.forEach(QRSwing::setGlobalSetting);
			SAVE_ACTIONS.forEach((s, e) -> e.action(null));
			return;
		}
		CANCEL_ACTIONS.forEach((s, e) -> e.action(null));
	}
}