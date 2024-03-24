package ly.qr.kiarelemb.component.setting;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.component.ComboBox;
import ly.qr.kiarelemb.component.RGBColorSelectPane;
import ly.qr.kiarelemb.component.Spinner;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRTimeCountUtil;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRComboBox;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.basic.QRRoundButton;
import swing.qr.kiarelemb.component.event.QRItemEvent;
import swing.qr.kiarelemb.component.utils.QRFontComboBox;
import swing.qr.kiarelemb.component.utils.QRLineSeparatorLabel;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import java.awt.*;
import java.io.File;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-30 14:09
 **/
public class AppearancePanel extends SettingPanel {
	public AppearancePanel(SettingWindow window) {
		super(window, "外观...");
		final String themeBackup = QRSwing.theme;
		final String fontNameBackup = QRSwing.globalFont == null ? "阿里巴巴普惠体 R" : QRSwing.globalFont.getFamily();
		QRLabel themeTipLabel = new QRLabel("主题：");
		QRComboBox themeComboBox = new QRComboBox(QRColorsAndFonts.BASIC_THEMES);
		QRRoundButton themeDesignerBtn = new QRRoundButton("打开设计器");
		CheckBox customFontCheckBox = new CheckBox("自定义界面字体", Keys.TEXT_FONT_NAME_GLOBAL_ENABLE);
		QRFontComboBox frameFontsComboBox = new QRFontComboBox(fontNameBackup);
		QRRoundButton fontSelectBtn = new QRRoundButton("选择字体文件");
		QRLineSeparatorLabel splitLabel = new QRLineSeparatorLabel(0.8d);
		QRLabel lookFontTipLabel = new QRLabel("看打区字体：");
		QRComboBox lookFontsComboBox = new ComboBox.FontComboBox(Keys.TEXT_FONT_NAME_LOOK) {

			@Override
			protected void itemChangedAction(QRItemEvent e) {
				SettingsItem.saveActions.put("look.font", ar -> {
					TextStyleManager.PREFERRED_CHINESE_FONT_NAME = e.after();
					TextStyleManager.updateAll();
				});
			}
		};
		QRLabel lookFontSizeTip = new QRLabel("大小：");
		Spinner lookSizeSpinner = new Spinner(Keys.TEXT_FONT_SIZE_LOOK);
		QRLabel typeFontTipLabel = new QRLabel("跟打区字体：");
		QRComboBox typeFontsComboBox = new ComboBox.FontComboBox(Keys.TEXT_FONT_NAME_TYPE);
//		QRRoundButton typeFontSelectBtn = new QRRoundButton("选择字体文件");
		QRLabel typeFontSizeTip = new QRLabel("大小：");
		Spinner typeSizeSpinner = new Spinner(Keys.TEXT_FONT_SIZE_TYPE);
		QRLineSeparatorLabel splitLabel2 = new QRLineSeparatorLabel(0.8d);
		QRLabel oneTipLabel = new QRLabel("一简颜色：");
		RGBColorSelectPane oneRgbPanel = new RGBColorSelectPane(TextStyleManager.SIMPLIFIED_ONE, Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_ONE);
		QRLabel twoTipLabel = new QRLabel("二简颜色：");
		RGBColorSelectPane twoRgbPanel = new RGBColorSelectPane(TextStyleManager.SIMPLIFIED_TWO, Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_TWO);
		QRLabel threeTipLabel = new QRLabel("三简颜色：");
		RGBColorSelectPane threeRgbPanel = new RGBColorSelectPane(TextStyleManager.SIMPLIFIED_THREE, Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_THREE);
		QRLabel fullTipLabel = new QRLabel("全码颜色：");
		RGBColorSelectPane fullRgbPanel = new RGBColorSelectPane(TextStyleManager.Full_FOUR, Keys.TEXT_TIP_COLOR_CODE_ALL);

		//region 设置主题
		themeComboBox.setText(QRSwing.theme);
		themeComboBox.addItemChangeListener(e -> {
			setCursorWait();
			QRItemEvent event = (QRItemEvent) e;
			String after = event.after();
			QRSwing.setTheme(after);
			MainWindow.INSTANCE.componentFresh();
			//如果取消，则恢复之前的主题
			SettingsItem.cancelActions.putIfAbsent("window.fresh", es -> {
				if (!QRSwing.theme.equals(themeBackup)) {
					QRSwing.setTheme(themeBackup);
					MainWindow.INSTANCE.componentFresh();
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
			SettingsItem.saveActions.putIfAbsent("window.font.default", es -> {
				if (!customFontCheckBox.checked()) {
					QRSwing.customFontName(TextStyleManager.DEFAULT_FONT);
					MainWindow.INSTANCE.componentFresh();
				}
			});
		});


		QRTimeCountUtil qcu = new QRTimeCountUtil((short) 100);
		frameFontsComboBox.addItemChangeListener(e -> {
			//去重
			if (qcu.isPassedMmTime()) {
				QRItemEvent event = (QRItemEvent) e;
				SettingsItem.changeMap.put(Keys.TEXT_FONT_NAME_GLOBAL, event.after());
				QRSwing.customFontName(event.after());
				MainWindow.INSTANCE.componentFresh();
				qcu.startTimeUpdate();
				//如果取消，则恢复之前的字体
				SettingsItem.cancelActions.putIfAbsent("window.font", es -> {
					if (!fontNameBackup.equals(event.after())) {
						QRSwing.customFontName(fontNameBackup);
						MainWindow.INSTANCE.componentFresh();
					}
				});
			}
		});

		fontSelectBtn.addClickAction(e -> {
			File file = QRFileUtils.fileSelect(window, "字体文件", "ttf", "ttc");
			Font font = QRFontUtils.loadFontFromFile(10, file);
			QRSwing.customFontName(font);
			MainWindow.INSTANCE.componentFresh();
			SettingsItem.changeMap.put(Keys.TEXT_FONT_NAME_GLOBAL, file.getAbsolutePath());
			frameFontsComboBox.setText(font.getFamily());
//			System.out.println("----------------------------------------------------------------------");
		});
		//endregion 全局字体设置

		themeTipLabel.setBounds(25, 30, 60, 30);
		themeDesignerBtn.setBounds(280, 30, 115, 30);
		customFontCheckBox.setBounds(25, 85, 150, 30);
		themeComboBox.setBounds(100, 30, 155, 30);
		frameFontsComboBox.setBounds(190, 85, 165, 30);
		fontSelectBtn.setBounds(365, 85, 125, 30);
		splitLabel.setBounds(25, 130, 450, 30);
		lookFontTipLabel.setBounds(30, 175, 115, 30);
		lookFontsComboBox.setBounds(190, 175, 165, 30);
		lookFontSizeTip.setBounds(78, 225, 55, 30);
		typeFontTipLabel.setBounds(30, 275, 115, 30);
		typeFontsComboBox.setBounds(190, 275, 165, 30);
		typeFontSizeTip.setBounds(78, 325, 55, 30);
		lookSizeSpinner.setBounds(190, 225, 90, 30);
		typeSizeSpinner.setBounds(190, 325, 88, 30);
		splitLabel2.setBounds(25, 370, 450, 22);
//		typeFontSelectBtn.setBounds(365, 275, 125, 30);

		oneTipLabel.setBounds(46, 410, 90, 30);
		twoTipLabel.setBounds(46, 455, 90, 30);
		threeTipLabel.setBounds(46, 500, 90, 30);
		fullTipLabel.setBounds(46, 545, 90, 30);

		oneRgbPanel.setLocation(190, 410);
		twoRgbPanel.setLocation(190, 455);
		threeRgbPanel.setLocation(190, 500);
		fullRgbPanel.setLocation(190, 545);

		add(themeTipLabel);
		add(themeDesignerBtn);
		add(customFontCheckBox);
		add(themeComboBox);
		add(frameFontsComboBox);
		add(fontSelectBtn);
		add(splitLabel);
		add(lookFontTipLabel);
		add(lookFontsComboBox);
		add(lookFontSizeTip);
		add(typeFontTipLabel);
		add(typeFontsComboBox);
		add(typeFontSizeTip);
		add(lookSizeSpinner);
		add(typeSizeSpinner);
		add(splitLabel2);
//		add(typeFontSelectBtn);
		add(oneTipLabel);
		add(oneRgbPanel);
		add(twoTipLabel);
		add(twoRgbPanel);
		add(threeTipLabel);
		add(threeRgbPanel);
		add(fullTipLabel);
		add(fullRgbPanel);

		setPreferredSize(505, 620);
	}
}