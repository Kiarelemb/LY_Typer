package ly.qr.kiarelemb;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.SplitPane;
import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.component.contract.state.GroupButton;
import ly.qr.kiarelemb.component.menu.type.LoadTextItem;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.tip.TipWindow;
import method.qr.kiarelemb.utils.QRMathUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRButton;
import swing.qr.kiarelemb.window.basic.QRFrame;

import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:17
 **/
public class MainWindow extends QRFrame {

	public static final MainWindow INSTANCE = new MainWindow();

	private MainWindow() {
		super("揽月 " + Info.SOFTWARE_VERSION);
		this.mainPanel.setLayout(new BorderLayout());

		//region 菜单
		QRButton typeMenu = this.titleMenuPanel.add("跟打");
		QRButton sendMenu = this.titleMenuPanel.add("发文");
		QRButton windowMenu = this.titleMenuPanel.add("窗口");
		QRButton toolMenu = this.titleMenuPanel.add("工具");
		QRButton aboutMenu = this.titleMenuPanel.add("关于");

		typeMenu.add(LoadTextItem.LOAD_TEXT_ITEM);
		typeMenu.add(SettingsItem.SETTINGS_ITEM);
		//endregion

		//region 中心面板
		this.mainPanel.add(SplitPane.SPLIT_PANE, BorderLayout.CENTER);
		//endregion

		//region 左侧
		this.mainPanel.add(ContractiblePanel.CONTRACTIBLE_PANEL, BorderLayout.WEST);
		//endregion

		setTitleCenter();
		setCloseButtonSystemExit();
		quickKeyLoad();
	}


	/**
	 * 加载快捷键
	 */
	public void quickKeyLoad() {
		QRSwing.registerGlobalAction(Keys.strValue(Keys.QUICK_KEY_RESTART), e -> TextPane.TEXT_PANE.restart(), true);
		QRSwing.registerGlobalAction(Keys.strValue(Keys.QUICK_KEY_GROUP), e -> GroupButton.groupBtn.doClick(), true);
		addActionBeforeDispose(e -> QRSwing.setGlobalSetting(Keys.WINDOW_SPLIT_WEIGHT, QRMathUtils.toString(SplitPane.SPLIT_PANE.getResizeWeight(), 3, false)));
	}

	public void grabFocus() {
		TyperTextPane.TYPER_TEXT_PANE.grabFocus();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		//加载一下词提窗口
		TipWindow.TIP_WINDOW.updateTipWindowLocation();
	}

	@Override
	public void componentFresh() {
		super.componentFresh();
		if (MainWindow.INSTANCE.backgroundImageSet() && QRSwing.windowBackgroundImagePath != null) {
			String path = QRSwing.windowBackgroundImagePath;
			MainWindow.INSTANCE.setBackgroundImage(null);
			MainWindow.INSTANCE.setBackgroundImage(path);
		}
	}
}