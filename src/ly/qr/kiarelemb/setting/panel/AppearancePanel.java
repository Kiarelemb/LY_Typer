package ly.qr.kiarelemb.setting.panel;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.*;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.menu.type.SettingsItem;
import ly.qr.kiarelemb.setting.SettingWindow;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRLoggerUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRComboBox;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRRoundButton;
import swing.qr.kiarelemb.event.QRItemEvent;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.theme.QRSwingThemeDesigner;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.utils.QRFontComboBox;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-30 14:09
 **/
public class AppearancePanel extends SettingPanel {
    private static final Logger logger = QRLoggerUtils.getLogger(AppearancePanel.class);

    public AppearancePanel(SettingWindow window) {
        super(window, "外观...");
        final String themeBackup = QRSwing.theme;
        final Font fontBackup = QRSwing.globalFont == null ? TextStyleManager.DEFAULT_FONT : QRSwing.globalFont;

        QRActionRegister updateAction = event -> TextStyleManager.updateAll();

        QRLabel themeTipLabel = new QRLabel("主题：");
        QRComboBox themeComboBox = new QRComboBox(QRColorsAndFonts.BASIC_THEMES);
        QRRoundButton themeDesignerBtn = new QRRoundButton("打开设计器");
        CheckBox customFontCheckBox = new CheckBox("自定义界面字体", Keys.TEXT_FONT_NAME_GLOBAL_ENABLE);
        QRFontComboBox frameFontsComboBox = new QRFontComboBox(fontBackup.getFontName(), false);
        QRRoundButton fontSelectBtn = new QRRoundButton("选择字体文件");
        LineSeparatorLabel splitLabel = new LineSeparatorLabel();
        QRLabel lookFontTipLabel = new QRLabel("看打区字体：");
        QRComboBox lookFontsComboBox = new ComboBox.FontComboBox(Keys.TEXT_FONT_NAME_LOOK) {

            @Override
            protected void itemChangedAction(QRItemEvent e) {
                SettingsItem.SAVE_ACTIONS.put("look.font", ar -> {
                    TextStyleManager.PREFERRED_CHINESE_FONT_NAME = e.after();
                    updateAction.action(null);
                    QRLoggerUtils.log(logger, Level.INFO, "设置看打区字体为： %s", e.after());
                });
            }
        };
        themeDesignerBtn.addClickAction(e -> {
            QRSwingThemeDesigner designer = new QRSwingThemeDesigner(MainWindow.INSTANCE);
            designer.setVisible(true);
        });

        QRLabel lookFontSizeTip = new QRLabel("大小：");
        Spinner lookSizeSpinner = new Spinner(Keys.TEXT_FONT_SIZE_LOOK);
        QRLabel typeFontTipLabel = new QRLabel("跟打区字体：");
        QRComboBox typeFontsComboBox = new ComboBox.FontComboBox(Keys.TEXT_FONT_NAME_TYPE);
        QRLabel typeFontSizeTip = new QRLabel("大小：");
        Spinner typeSizeSpinner = new Spinner(Keys.TEXT_FONT_SIZE_TYPE);
        LineSeparatorLabel splitLabel2 = new LineSeparatorLabel();
        QRLabel oneTipLabel = new QRLabel("一简颜色：");
        RGBColorSelectPane oneRgbPanel = new RGBColorSelectPane(TextStyleManager.SIMPLIFIED_ONE, Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_ONE, updateAction);
        QRLabel twoTipLabel = new QRLabel("二简颜色：");
        RGBColorSelectPane twoRgbPanel = new RGBColorSelectPane(TextStyleManager.SIMPLIFIED_TWO, Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_TWO, updateAction);
        QRLabel threeTipLabel = new QRLabel("三简颜色：");
        RGBColorSelectPane threeRgbPanel = new RGBColorSelectPane(TextStyleManager.SIMPLIFIED_THREE, Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_THREE, updateAction);
        QRLabel fullTipLabel = new QRLabel("全码颜色：");
        RGBColorSelectPane fullRgbPanel = new RGBColorSelectPane(TextStyleManager.Full_FOUR, Keys.TEXT_TIP_COLOR_CODE_ALL, updateAction);

        lookSizeSpinner.addChangeListener(e -> {
            SettingsItem.SAVE_ACTIONS.put("look.font.size", ar -> {
                TextPane.TEXT_PANE.componentFresh();
                updateAction.action(null);
                QRLoggerUtils.log(logger, Level.INFO, "设置看打区字体大小为： %s", lookSizeSpinner.getValue());
            });
        });

        typeSizeSpinner.addChangeListener(e -> {
            SettingsItem.SAVE_ACTIONS.put("type.font.size", ar -> {
                TyperTextPane.TYPER_TEXT_PANE.componentFresh();
                updateAction.action(null);
                QRLoggerUtils.log(logger, Level.INFO, "设置跟打区字体大小为： %s", typeSizeSpinner.getValue());
            });
        });

        //region 设置主题
        themeComboBox.setText(QRSwing.theme);
        themeComboBox.addItemChangeListener(e -> {
            setCursorWait();
            QRItemEvent event = (QRItemEvent) e;
            String after = event.after();
            if (QRSwing.theme.equals(after)) {
                setCursorDefault();
                return;
            }
            QRSwing.setTheme(after);
            MainWindow.INSTANCE.componentFresh();
            QRLoggerUtils.log(logger, Level.INFO, "设置主题为： %s", after);
            //如果取消，则恢复之前的主题
            SettingsItem.CANCEL_ACTIONS.putIfAbsent("window.fresh", es -> {
                if (!QRSwing.theme.equals(themeBackup)) {
                    QRSwing.setTheme(themeBackup);
                    MainWindow.INSTANCE.componentFresh();
                    SettingWindow.INSTANCE.componentFresh();
                    themeComboBox.setText(QRSwing.theme);
                    QRLoggerUtils.log(logger, Level.INFO, "恢复主题为： %s", themeBackup);
                }
            });
            setCursorDefault();
        });
        themeComboBox.setToolTipText("重启软件以获得更好的效果。");
        //endregion 设置主题

        //region 全局字体设置
        frameFontsComboBox.setEnabled(customFontCheckBox.checked());
        fontSelectBtn.setEnabled(customFontCheckBox.checked());

        customFontCheckBox.addClickAction(e -> {
            frameFontsComboBox.setEnabled(customFontCheckBox.checked());
            fontSelectBtn.setEnabled(customFontCheckBox.checked());
            SettingsItem.SAVE_ACTIONS.putIfAbsent("window.font.default", es -> {
                if (!customFontCheckBox.checked()) {
                    QRSwing.customFontName(TextStyleManager.DEFAULT_FONT);
                    MainWindow.INSTANCE.componentFresh();
                    QRLoggerUtils.log(logger, Level.INFO, "恢复默认字体为： %s", TextStyleManager.DEFAULT_FONT.getFontName());
                }
            });
        });

        frameFontsComboBox.addItemChangeListener(e -> {
            QRItemEvent event = (QRItemEvent) e;
            SettingsItem.CHANGE_MAP.put(Keys.TEXT_FONT_NAME_GLOBAL, event.after());
            QRLoggerUtils.log(logger, Level.INFO, "设置默认字体为： %s", event.after());
            QRSwing.customFontName(event.after());
            MainWindow.INSTANCE.componentFresh();
            //如果取消，则恢复之前的字体
            SettingsItem.CANCEL_ACTIONS.putIfAbsent("window.font", es -> {
                if (!fontBackup.getFontName().equals(event.after())) {
                    QRSwing.customFontName(fontBackup);
                    MainWindow.INSTANCE.componentFresh();
                }
            });
        });

        fontSelectBtn.addClickAction(e -> QRFileUtils.fileSelect(window, "字体文件", file -> {
            Font font = QRFontUtils.loadFontFromFile(10, file);
            QRSwing.customFontName(font);
            MainWindow.INSTANCE.componentFresh();
            SettingsItem.CHANGE_MAP.put(Keys.TEXT_FONT_NAME_GLOBAL, file.getAbsolutePath());
            QRLoggerUtils.log(logger, Level.INFO, "设置默认字体为： %s", font.getFontName());
            frameFontsComboBox.setText(font.getFamily());
        }, "ttf", "ttc"));
        //endregion 全局字体设置

        QRComponentUtils.setBoundsAndAddToComponent(this, themeTipLabel, 25, 30, 60, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, themeDesignerBtn, 280, 30, 115, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, customFontCheckBox, 25, 85, 150, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, themeComboBox, 100, 30, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, frameFontsComboBox, 190, 85, 165, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, fontSelectBtn, 365, 85, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, splitLabel, 25, 130, 450, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, lookFontTipLabel, 30, 175, 115, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, lookFontsComboBox, 190, 175, 165, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, lookFontSizeTip, 78, 225, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, typeFontTipLabel, 30, 275, 115, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, typeFontsComboBox, 190, 275, 165, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, typeFontSizeTip, 78, 325, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, lookSizeSpinner, 190, 225, 90, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, typeSizeSpinner, 190, 325, 88, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, splitLabel2, 25, 370, 450, 22);

        QRComponentUtils.setBoundsAndAddToComponent(this, oneTipLabel, 46, 410, 90, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, twoTipLabel, 46, 455, 90, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, threeTipLabel, 46, 500, 90, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, fullTipLabel, 46, 545, 90, 30);

        QRComponentUtils.setBoundsAndAddToComponent(this, oneRgbPanel, 190, 410);
        QRComponentUtils.setBoundsAndAddToComponent(this, twoRgbPanel, 190, 455);
        QRComponentUtils.setBoundsAndAddToComponent(this, threeRgbPanel, 190, 500);
        QRComponentUtils.setBoundsAndAddToComponent(this, fullRgbPanel, 190, 545);

        setPreferredSize(505, 620);
    }
}