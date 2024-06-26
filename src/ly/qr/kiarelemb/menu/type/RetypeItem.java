package ly.qr.kiarelemb.menu.type;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;

import java.awt.event.ActionEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2024-04-18 19:13
 **/
public class RetypeItem extends MenuItem {
	public static final RetypeItem RETYPE_ITEM = new RetypeItem();

	private RetypeItem() {
		super("重打", Keys.QUICK_KEY_RESTART);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		TextPane.TEXT_PANE.restart();
	}
}