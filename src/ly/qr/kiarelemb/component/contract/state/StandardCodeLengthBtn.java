package ly.qr.kiarelemb.component.contract.state;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.StandardTipWindow;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.window.enhance.QROpinionDialog;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.awt.event.ActionEvent;

public class StandardCodeLengthBtn extends QRRoundButton {
	public StandardCodeLengthBtn() {
		super("查看标顶打法");
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		if (TextLoad.TEXT_LOAD == null) {
			QRSmallTipShow.display("请先载文！", 2000);
			return;
		}
		if (TextLoad.TEXT_LOAD.tipData == null) {
			QROpinionDialog.messageErrShow(MainWindow.INSTANCE,"请先设置词提文件！" );
			return;
		}
		if (TextLoad.TEXT_LOAD.isText()) {
			if (TextLoad.TEXT_LOAD.isEnglish()) {
				QROpinionDialog.messageInfoShow(MainWindow.INSTANCE, "英文文本没有词提！");
				return;
			}
			StandardTipWindow stw = new StandardTipWindow();
			stw.setVisible(true);
		}
	}
}