package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.component.YesOrNoCheckBox;
import ly.qr.kiarelemb.data.Keys;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 22:33
 **/
public class SilkyModelCheckBox extends YesOrNoCheckBox {
	public static final SilkyModelCheckBox silkyCheckBox = new SilkyModelCheckBox();

	private SilkyModelCheckBox() {
		super(Keys.TYPE_SILKY_MODEL);
	}
}
