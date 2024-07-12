package ly.qr.kiarelemb;

import ly.qr.kiarelemb.component.LogTextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.setting.SettingWindow;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.QRSwing;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @apiNote: 揽月跟打器主方法类
 * @create 2023-01-12 22:51
 **/
public class Enter {

    private static Logger logger;

    public static void main(String[] args) {
        // 初始化 JDK 自带的 Logger
        QRLoggerUtils.prefix = "ly";
        QRLoggerUtils.initLogger(Level.INFO, Level.CONFIG);
        QRLoggerUtils.classMsgMaxLength = 120;
        logger = QRLoggerUtils.getLogger(Enter.class);
        LogTextPane.LOG_TEXT_PANE.init();
        logger.info("************************************** 揽月开始启动 **************************************");
        QRTimeCountUtil qcu = new QRTimeCountUtil();
        QRSwing.start("res/settings/setting.properties", "res/settings/window.properties");
        logger.info("QRSwing 框架加载完毕，" + qcu.endAndGet());
        // 设置窗口图标
        QRSwing.windowIcon = Info.loadImage(Info.ICON_PNG_PATH);

        FlashLoadingWindow flw = new FlashLoadingWindow();
        flw.setVisible(true);

        variousLoad();

        logger.config("当前系统：" + QRSystemUtils.getSystemName());
        if (Info.IS_WINDOWS) {
            QRSwing.setGlobalKeyEventsListener(TyperTextPane.TYPER_TEXT_PANE.globalKeyListener, MainWindow.INSTANCE);
        } else {
            QRSwing.setGlobalKeyEventsListener(TyperTextPane.TYPER_TEXT_PANE.keyboardFocusManager, MainWindow.INSTANCE);
        }

        flw.setVisible(false);

        logger.info("-------------------------------------- 配置加载完毕 --------------------------------------");
        QRSystemUtils.setWindowShowSlowly(MainWindow.INSTANCE, QRSwing.windowTransparency);
    }

    private static void variousLoad() {

        //region 全局界面字体
        Font font = null;
        boolean fontEnable = Keys.boolValue(Keys.TEXT_FONT_NAME_GLOBAL_ENABLE);
        if (fontEnable) {
            String fontNameOrPath = Keys.strValue(Keys.TEXT_FONT_NAME_GLOBAL);
            if (QRFileUtils.fileExists(fontNameOrPath)) {
                logger.config("加载字体：" + fontNameOrPath);
                font = QRFontUtils.loadFontFromFile(10, fontNameOrPath);
            } else {
                String[] names = QRFontUtils.getSystemFontNames();
                if (QRArrayUtils.objectIndexOf(names, fontNameOrPath) != -1) {
                    font = QRFontUtils.getFont(fontNameOrPath, 10);
                    logger.config("加载字体：" + fontNameOrPath);
                }
            }
        }
        if (font == null) {
            //全局默认字体即为阿里巴巴普惠体
            font = TextStyleManager.DEFAULT_FONT;
            logger.config("加载默认字体：" + font.getFontName());
        }
        QRSwing.customFontName(font);
        //endregion 全局界面字体

        //提前加载一遍试试
        SettingWindow.INSTANCE.setVisible(false);
        logger.config("设置窗口预加载完毕。");

        //加载一下词提
        TextTip.TEXT_TIP.load();
    }
}