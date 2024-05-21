package ly.qr.kiarelemb.component.menu.send;

import ly.qr.kiarelemb.component.menu.MenuItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.TextSendManager;
import method.qr.kiarelemb.utils.QRFileUtils;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedList;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className ContinueSendTextItem
 * @description TODO
 * @create 2024/4/17 21:04
 */
public class ContinueSendTextItem extends MenuItem {
	public static final ContinueSendTextItem CONTINUE_SEND_TEXT_ITEM = new ContinueSendTextItem();

	private ContinueSendTextItem() {
		super("继续发文", Keys.QUICK_KEY_SEND_CONTINUE);
	}

	@Override
	protected void actionEvent(ActionEvent o) {
		LinkedList<File> list = new LinkedList<>();
		QRFileUtils.dirLoop(Info.TYPE_DIRECTORY, file -> {
			if (".bin".equals(QRFileUtils.getFileExtension(file))
					&& QRFileUtils.fileExists(Info.TYPE_DIRECTORY + QRFileUtils.getFileName(file) + ".txt")) {
				list.add(file);
			}
		});

		if (list.isEmpty()) {
			return;
		}

		int size = list.size();
		if (size == 1) {
			if (TextSendManager.loadSerializedData(list.getFirst().getName())) {
				TextSendManager.setTypedData(TextSendManager.data());
				System.out.println("TextSendManager.data() = " + TextSendManager.data());
				return;
			}
		}
	}
}