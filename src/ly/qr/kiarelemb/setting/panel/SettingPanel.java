package ly.qr.kiarelemb.setting.panel;

import ly.qr.kiarelemb.setting.SettingWindow;
import swing.qr.kiarelemb.component.basic.QRPanel;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-31 15:11
 **/
public abstract class SettingPanel extends QRPanel {
	public final SettingWindow window;

	public SettingPanel(SettingWindow window, String name) {
		setLayout(null);
		this.window = window;
		setName(name);
	}
}