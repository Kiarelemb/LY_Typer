package ly.qr.kiarelemb;

import ly.qr.kiarelemb.component.ContractiblePanel;
import ly.qr.kiarelemb.component.SplitPane;
import ly.qr.kiarelemb.component.TextViewPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.dl.DangLangWindow;
import ly.qr.kiarelemb.menu.about.HotMapItem;
import ly.qr.kiarelemb.menu.send.*;
import ly.qr.kiarelemb.menu.type.LoadTextItem;
import ly.qr.kiarelemb.menu.type.RetypeItem;
import ly.qr.kiarelemb.menu.type.SettingsItem;
import ly.qr.kiarelemb.menu.type.TextMixItem;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.tip.TipWindow;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import method.qr.kiarelemb.utils.QRSleepUtils;
import method.qr.kiarelemb.utils.QRThreadBuilder;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRButton;
import swing.qr.kiarelemb.window.basic.QRFrame;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:17
 **/
public class MainWindow extends QRFrame {
    private static final Logger logger = QRLoggerUtils.getLogger(MainWindow.class);

    public static final MainWindow INSTANCE = new MainWindow();

    private MainWindow() {
        super("揽月 " + Info.SOFTWARE_VERSION);
        this.mainPanel.setLayout(new BorderLayout());
        setTitlePanel();

        //菜单
        menuInit();

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

    private void menuInit() {
        this.titleMenuPanel.setAutoExpend(true);
        QRButton typeMenu = this.titleMenuPanel.add("跟打");
        QRButton sendMenu = this.titleMenuPanel.add("发文");
        QRButton windowMenu = this.titleMenuPanel.add("窗口");
        QRButton toolMenu = this.titleMenuPanel.add("工具");
        QRButton aboutMenu = this.titleMenuPanel.add("关于");

        typeMenu.add(LoadTextItem.LOAD_TEXT_ITEM);
        typeMenu.add(RetypeItem.RETYPE_ITEM);
        typeMenu.add(TextMixItem.TEXT_MIX_ITEM);
        typeMenu.add(SettingsItem.SETTINGS_ITEM);

        sendMenu.add(NewSendTextItem.NEW_SEND_TEXT_ITEM);
        sendMenu.add(ContinueSendTextItem.CONTINUE_SEND_TEXT_ITEM);
        sendMenu.add(EndSendTextItem.END_SEND_TEXT_ITEM);
        sendMenu.add(ForeParaTextItem.FORE_PARA_TEXT_ITEM);
        sendMenu.add(NextParaTextItem.NEXT_PARA_TEXT_ITEM);

        aboutMenu.add(HotMapItem.HOT_MAP_ITEM);

    }


    /**
     * 加载快捷键
     */
    public void quickKeyLoad() {
        //当量显示器
        QRSwing.registerGlobalAction(Keys.strValue(Keys.QUICK_KEY_DANG_LIANG_WINDOW), event -> {
            DangLangWindow window = DangLangWindow.dangLangWindow();
            window.setVisible(!window.isVisible());
        }, true);
        //内置输入法，功能未成形，取消使用
        //QRSwing.registerGlobalAction(Keys.strValue(Keys.QUICK_KEY_INNER_INPUT_WINDOW), event -> InputManager.INPUT_MANAGER.init(), true);
        //保存分割面板的分割比例
        addActionBeforeDispose(e -> {
            QRSwing.setGlobalSetting(Keys.WINDOW_SPLIT_WEIGHT, SplitPane.SPLIT_PANE.getDividerLocation());
            logger.info("************************************** 您已退出揽月 **************************************");
        });
    }

    private ThreadPoolExecutor WINDOW_FRESH_THREAD = QRThreadBuilder.singleThread("window.fresh");

    public void startFreshening() {
        if (!Keys.boolValue(Keys.WINDOW_BACKGROUND_FRESH_ENHANCEMENT)) {
            return;
        }
        stopFreshening();
        WINDOW_FRESH_THREAD = QRThreadBuilder.singleThread("window.fresh");
        if (WINDOW_FRESH_THREAD.getQueue().isEmpty()) {
            Future<Object> fresh = WINDOW_FRESH_THREAD.submit(() -> {
                logger.config("进入高速刷新模式");
                while ("true".equals(QRSwing.GLOBAL_PROP.get(Keys.WINDOW_BACKGROUND_FRESH_ENHANCEMENT))) {
                    try {
                        QRSleepUtils.sleep(2);
                        TypingData.windowFresh();
                    } catch (Exception ignore) {
                    }
                }
                return null;
            });
        }
    }

    public void stopFreshening() {
        if (!WINDOW_FRESH_THREAD.getQueue().isEmpty()) {
            WINDOW_FRESH_THREAD.shutdownNow();
            logger.config("退出高速刷新模式");
        }
    }

    public void grabFocus() {
        TyperTextPane.TYPER_TEXT_PANE.grabFocus();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //加载一下词提窗口
        TipWindow.TIP_WINDOW.updateTipWindowLocation();
        startFreshening();
    }

    @Override
    public void componentFresh() {
        super.componentFresh();
        TextStyleManager.updateAll();
        TextViewPane.TEXT_VIEW_PANE.simpleRestart();
        if (MainWindow.INSTANCE.backgroundImageSet() && QRSwing.windowBackgroundImagePath != null) {
            String path = QRSwing.windowBackgroundImagePath;
            MainWindow.INSTANCE.setBackgroundImage(null);
            MainWindow.INSTANCE.setBackgroundImage(path);
        }
        logger.info("全局窗口刷新完成");
    }
}