package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.text.tip.StandardTipWindow;
import swing.qr.kiarelemb.component.basic.QRRoundButton;

import java.awt.event.ActionEvent;

public class StandardCodeLengthBtn extends QRRoundButton {
	public StandardCodeLengthBtn() {
		super("查看标顶打法");
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		StandardTipWindow stw = new StandardTipWindow();
		stw.setVisible(true);
	}
}