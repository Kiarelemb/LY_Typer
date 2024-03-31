package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.component.menu.send.LocalTextTabbedPanel;
import ly.qr.kiarelemb.component.menu.send.SingleTabbedPanel;
import ly.qr.kiarelemb.component.menu.send.TabbedContentPanel;
import ly.qr.kiarelemb.data.Keys;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.component.combination.QRTabbedPane;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TabbedPane
 * @description TODO
 * @create 2024/3/31 22:10
 */
public class TabbedPane extends QRTabbedPane {
	public TabbedPane() {

		TabbedContentPanel singlePanel = new SingleTabbedPanel();
		QRTabbedContentPanel articlePanel = new QRTabbedContentPanel();
		TabbedContentPanel localTextPanel = new LocalTextTabbedPanel();

		int singleIndex = addTab("单字发文", singlePanel);
		int articleIndex = addTab("文章发文", articlePanel);
		int localTextIndex = addTab("本地发文", localTextPanel);

		setSelectedTab(Keys.intValue(Keys.SEND_TEXT_NEW_WINDOW_TAB_INDEX));

		addTabSelectChangedAction(event -> {
			int index = TabbedPane.this.getSelectedTabIndex();
			QRSwing.setGlobalSetting(Keys.SEND_TEXT_NEW_WINDOW_TAB_INDEX, index);
		});
	}
}