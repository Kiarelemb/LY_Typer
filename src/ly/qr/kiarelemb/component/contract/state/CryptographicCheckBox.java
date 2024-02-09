package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.component.YesOrNoCheckBox;
import ly.qr.kiarelemb.data.Keys;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-24 00:19
 **/
public class CryptographicCheckBox extends YesOrNoCheckBox {
	public static final CryptographicCheckBox cryptographicCheckBox = new CryptographicCheckBox();

	private CryptographicCheckBox() {
		super(Keys.SEND_CRYPTOGRAPHIC);
	}
}
