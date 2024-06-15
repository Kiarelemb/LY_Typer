package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.send.panel.InnerArticleTextTabbedPanel;
import ly.qr.kiarelemb.component.menu.send.panel.LocalTextSendTextTabbedPanel;
import ly.qr.kiarelemb.component.menu.send.panel.SingleSendTextTabbedPanel;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.send.SendWindow;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.component.combination.QRTabbedPane;

import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className SendTextTabbedPane
 * @description TODO
 * @create 2024/3/31 22:10
 */
public class SendTextTabbedPane extends QRTabbedPane {
	public SendTextTabbedPane(SendWindow window) {
        super(BorderLayout.NORTH, FlowLayout.CENTER);
		QRTabbedContentPanel singlePanel = new SingleSendTextTabbedPanel(window);
		QRTabbedContentPanel innerArticlePanel = new InnerArticleTextTabbedPanel(window);
		QRTabbedContentPanel localTextPanel = new LocalTextSendTextTabbedPanel(window);

		int singleIndex = addTab("单字发文", singlePanel);
		int articleIndex = addTab("内置发文", innerArticlePanel);
		int localTextIndex = addTab("本地发文", localTextPanel);


		setSelectedTab(Keys.intValue(Keys.SEND_TEXT_NEW_WINDOW_TAB_INDEX));

        addTabSelectChangedAction(event -> QRSwing.setGlobalSetting(Keys.SEND_TEXT_NEW_WINDOW_TAB_INDEX, SendTextTabbedPane.this.getSelectedTabIndex()));
	}
}