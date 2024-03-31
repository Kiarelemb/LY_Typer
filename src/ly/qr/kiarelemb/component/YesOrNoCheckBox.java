package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRCheckBox;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 22:50
 **/
public class YesOrNoCheckBox extends QRCheckBox {
	private final String key;
	private boolean checked;

	public YesOrNoCheckBox(String key) {
		super("否");
		this.key = key;
		this.checked = Keys.boolValue(this.key);
		setSelected(this.checked);
		setText(this.checked ? "是" : "否");
	}

	@Override
	protected final void actionEvent(ActionEvent o) {
		this.checked = !this.checked;
		setText(this.checked ? "是" : "否");
		setSelected(this.checked);
		QRSwing.setGlobalSetting(this.key, this.checked);
	}

	public boolean checked() {
		return this.checked;
	}
}