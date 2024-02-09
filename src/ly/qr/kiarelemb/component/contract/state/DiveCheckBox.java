package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.component.YesOrNoCheckBox;
import ly.qr.kiarelemb.data.Keys;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 22:55
 **/
public class DiveCheckBox extends YesOrNoCheckBox {
	public static final DiveCheckBox diveCheckBox = new DiveCheckBox();

	private DiveCheckBox() {
		super(Keys.TYPE_DIVE_MODEL);
	}
}
