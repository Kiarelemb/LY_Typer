package ly.qr.kiarelemb;

import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import swing.qr.kiarelemb.QRSwing;

import java.awt.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @apiNote: 揽月跟打器主方法类
 * @create 2023-01-12 22:51
 **/
public class Enter {
	public static void main(String[] args) {
		QRSwing.start("res/settings/setting.properties", "res/settings/window.properties");
		QRSwing.windowIcon = Info.loadImage(Info.ICON_PNG_PATH);

		FlashLoadingWindow flw = new FlashLoadingWindow();
		flw.setVisible(true);

		variousLoad();

		if (QRSystemUtils.IS_WINDOWS) {
			QRSwing.setGlobalKeyEventsListener(TyperTextPane.TYPER_TEXT_PANE.globalKeyListener, MainWindow.INSTANCE);
		}

		if (Keys.boolValue(Keys.WINDOW_BACKGROUND_IMAGE_ENABLE)) {
			MainWindow.INSTANCE.setBackgroundImage(Keys.strValue(QRSwing.WINDOW_IMAGE_PATH));
            MainWindow.INSTANCE.setBackgroundBorderAlpha(QRSwing.windowBackgroundImageAlpha);
		}

		flw.setVisible(false);
		QRSystemUtils.setWindowShowSlowly(MainWindow.INSTANCE, QRSwing.windowTransparency);
	}

	private static void variousLoad() {

		//region 全局界面字体
		Font font = null;
		String[] names = QRFontUtils.getSystemFontNames();
		boolean fontEnable = Keys.boolValue(Keys.TEXT_FONT_NAME_GLOBAL_ENABLE);
		if (fontEnable) {
			String fontNameOrPath = Keys.strValue(Keys.TEXT_FONT_NAME_GLOBAL);
			if (QRFileUtils.fileExists(fontNameOrPath)) {
				font = QRFontUtils.loadFontFromFile(10, fontNameOrPath);
			} else if (QRArrayUtils.objectIndexOf(names, fontNameOrPath) != -1) {
				font = QRFontUtils.getFont(fontNameOrPath, 10);
			}
		}
		if (font == null) {
			//全局默认字体即为阿里巴巴普惠体
			font = TextStyleManager.DEFAULT_FONT;
		}
		QRSwing.customFontName(font);
		//endregion 全局界面字体

		//提前加载一遍试试
		SettingWindow w = new SettingWindow();
		w.setVisible(false);

		//加载一下词提
		TextTip.TEXT_TIP.load();
		TextStyleManager.updateAll();
	}
}