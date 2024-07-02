package ly.qr.kiarelemb.text.send;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.SendTextTabbedPane;
import swing.qr.kiarelemb.combination.QRTabbedPane;
import swing.qr.kiarelemb.window.basic.QRDialog;

import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className SendWindow
 * @description TODO
 * @create 2024/3/31 10:34
 */
public class SendWindow extends QRDialog {
	public SendWindow() {
		super(MainWindow.INSTANCE);
		setTitle("新建发文");
		setSize(500, 300);

		QRTabbedPane tabbedPane = new SendTextTabbedPane(this);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		setParentWindowNotFollowMove();
	}


	@Override
	public void dispose() {
		super.dispose();
	}
}