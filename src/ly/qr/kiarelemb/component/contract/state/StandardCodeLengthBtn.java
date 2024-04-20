package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.StandardTipWindow;
import swing.qr.kiarelemb.component.basic.QRRoundButton;

import java.awt.event.ActionEvent;

public class StandardCodeLengthBtn extends QRRoundButton {
	public static final StandardCodeLengthBtn STANDARD_CODE_LENGTH_BTN = new StandardCodeLengthBtn();

	private StandardCodeLengthBtn() {
		super("查看标顶打法");
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		if (TextLoad.TEXT_LOAD == null) {
			return;
		}
		if (TextLoad.TEXT_LOAD.tipData == null) {
			return;
		}
		if (TextLoad.TEXT_LOAD.isText()) {
			if (TextLoad.TEXT_LOAD.isEnglish()) {
				return;
			}
			StandardTipWindow stw = new StandardTipWindow();
			stw.setVisible(true);
		}
	}
}