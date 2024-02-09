package ly.qr.kiarelemb.text.tip;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRPanel;
import swing.qr.kiarelemb.component.basic.QRTextPane;
import swing.qr.kiarelemb.window.basic.QRDialog;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StandardTipWindow extends QRDialog {
	public StandardTipWindow() {
		super(MainWindow.INSTANCE);
		setTitle("标顶打法");
		setTitlePlace(QRDialog.CENTER);
		setSize(400, 300);
		this.mainPanel.setLayout(new BorderLayout(5, 0));
		this.mainPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

		//顶部面板

		QRPanel topPanel = new QRPanel();
		topPanel.setLayout(new BorderLayout());

		String methodName = Keys.strValue(Keys.TYPE_METHOD_INPUT);
		QRLabel method = new QRLabel("方案：" + (methodName == null || methodName.isBlank() ? "未定义" : methodName));
		topPanel.add(method, BorderLayout.WEST);

		this.mainPanel.add(topPanel, BorderLayout.NORTH);
		//中间面板

		QRTextPane centerTextPane = new QRTextPane();
		this.mainPanel.add(centerTextPane, BorderLayout.CENTER);

		//底部面板
		
	}
}