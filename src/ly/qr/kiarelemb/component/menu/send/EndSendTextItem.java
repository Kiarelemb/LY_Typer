package ly.qr.kiarelemb.component.menu.send;

import ly.qr.kiarelemb.component.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.TextSendManager;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className EndSendTextItem
 * @description TODO
 * @create 2024/4/19 23:21
 */
public class EndSendTextItem extends MenuItem {
	public static final EndSendTextItem END_SEND_TEXT_ITEM = new EndSendTextItem();

	private EndSendTextItem() {
		super("结束发文", Keys.QUICK_KEY_SEND_END);
		setEnabled(false);
	}

	/**
	 * 已自动添加监听器，可直接重写
	 *
	 * @param o
	 */
	@Override
	protected void actionEvent(ActionEvent o) {
		TextSendManager.endSendText();
	}
}