package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import swing.qr.kiarelemb.component.examples.QRRGBColorSelectPane;

import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-01 11:47
 **/
public class RGBColorSelectPane extends QRRGBColorSelectPane {

	private final String key;

	public RGBColorSelectPane(Color color, String key) {
		super(color);
		this.key = key;
	}

	@Override
	protected void colorChangedAction(Color from, Color to) {
		SettingsItem.CHANGE_MAP.put(this.key, QRRGBColorSelectPane.getColor(to));
	}
}