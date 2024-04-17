package ly.qr.kiarelemb.component.menu.send;

import ly.qr.kiarelemb.component.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.SendWindow;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className NewSendTextItem
 * @description TODO
 * @create 2024/3/31 21:40
 */
public class NewSendTextItem extends MenuItem {

	public static final NewSendTextItem NEW_SEND_TEXT_ITEM = new NewSendTextItem();

	private NewSendTextItem() {
		super("新建发文", Keys.QUICK_KEY_NEW_SEND);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		SendWindow sendWindow = new SendWindow();
		sendWindow.setVisible(true);
	}
}