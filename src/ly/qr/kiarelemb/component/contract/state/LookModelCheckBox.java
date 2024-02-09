package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.component.YesOrNoCheckBox;
import ly.qr.kiarelemb.data.Keys;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-24 09:26
 **/
public class LookModelCheckBox extends YesOrNoCheckBox {
	public static final LookModelCheckBox lookModelCheckBox = new LookModelCheckBox();

	private LookModelCheckBox() {
		super(Keys.TYPE_MODEL_LOOK);
	}
}
