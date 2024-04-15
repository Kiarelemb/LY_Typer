package ly.qr.kiarelemb.component.setting.panel;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.component.menu.type.SettingsItem;
import ly.qr.kiarelemb.component.setting.SettingWindow;
import ly.qr.kiarelemb.data.Keys;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.assembly.QRBackgroundBorder;
import swing.qr.kiarelemb.component.basic.*;
import swing.qr.kiarelemb.component.utils.QRFilePathTextField;
import swing.qr.kiarelemb.component.utils.QRFileSelectButton;
import swing.qr.kiarelemb.component.utils.QRLineSeparatorLabel;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.resource.QRSwingInfo;
import swing.qr.kiarelemb.window.basic.QRDialog;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-31 14:28
 **/
public class WindowPanel extends SettingPanel {
	public WindowPanel(SettingWindow window) {
		super(window, "窗体...");
		QRLabel mainWindowTipLabel = new QRLabel("主窗体");
		CheckBox windowRoundCheckBox = new CheckBox("启用窗体圆角", QRSwing.WINDOW_ROUND);
		CheckBox windowOnTopCheckBox = new CheckBox("启用窗体置顶", QRSwing.WINDOW_ALWAYS_TOP);
		QRLineSeparatorLabel splitSeparator = new QRLineSeparatorLabel(0.8d);
		CheckBox windowTransCheckBox = new CheckBox("启用窗体透明", Keys.WINDOW_TRANSPARENCY_ENABLE);
		QRSlider windowTransSlider = new QRSlider();
		CheckBox windowScreenAdsorbCheckBox = new CheckBox("启用屏幕吸附", QRSwing.WINDOW_ABSORB);
		CheckBox windowBackgroundImageSetCheckBox = new CheckBox("启用背景图", QRSwing.WINDOW_IMAGE_PATH);
		QRRoundButton backgroundImageSetBtn = new QRRoundButton("背景图设置");

		windowRoundCheckBox.setToolTipText("启用后，包括禅出的对话框在内，窗体都将采用圆角。");
		windowOnTopCheckBox.setToolTipText("启用后，主窗体将置顶。可能对于一些中文输入法来说，该功能不太支持。");
		windowTransCheckBox.setToolTipText("在未设置主窗体背景图的情况下，该功能适用。取消勾选以启用背景图设置。");
		windowScreenAdsorbCheckBox.setToolTipText("该功能指对屏幕四边的吸附。");
		windowBackgroundImageSetCheckBox.setToolTipText("启用后，主窗体的透明功能失效。");

		QRActionRegister windowTransAction = es -> {
			if (windowTransCheckBox.checked()) {
				QRSwing.setWindowTransparency(windowTransSlider.getValue() / 100f);
				QRSystemUtils.setWindowTrans(MainWindow.INSTANCE, Keys.floatValue(QRSwing.WINDOW_TRANSPARENCY));
			} else {
				QRSwing.setWindowTransparency(0.99f);
				QRSystemUtils.setWindowNotTrans(MainWindow.INSTANCE);
			}
		};

		windowTransCheckBox.addClickAction(event -> {
			boolean checked = windowTransCheckBox.checked();
			windowTransSlider.setEnabled(checked);
			windowBackgroundImageSetCheckBox.setEnabled(!checked);
			backgroundImageSetBtn.setEnabled(!checked);
//			if (checked) {
			SettingsItem.SAVE_ACTIONS.put("window.trans.set", windowTransAction);
//			} else {
//				SettingsItem.saveActions.remove("window.trans.set", windowTransAction);
//			}
		});

		windowBackgroundImageSetCheckBox.addClickAction(e -> {
			boolean checked = windowBackgroundImageSetCheckBox.checked();
			backgroundImageSetBtn.setEnabled(checked);
			windowTransCheckBox.setEnabled(!checked);
			windowTransSlider.setEnabled(!checked);
		});
		backgroundImageSetBtn.addClickAction(event -> {
			BackgroundImageSelectDialog dialog = new BackgroundImageSelectDialog();
			dialog.setVisible(true);
		});

		windowTransSlider.setBoundValue(30, 100);
		windowTransSlider.setValue((int) (100 * QRSwing.windowTransparency));
		windowTransSlider.addChangeListener(e -> {
			int value = windowTransSlider.getValue();
			float alpha = value / 100f;
			QRSystemUtils.setWindowTrans(window, alpha);
			SettingsItem.CHANGE_MAP.put(QRSwing.WINDOW_TRANSPARENCY, String.valueOf(alpha));
			SettingsItem.SAVE_ACTIONS.put("window.trans.set", windowTransAction);
		});

		boolean set = MainWindow.INSTANCE.backgroundImageSet();
		windowBackgroundImageSetCheckBox.setEnabled(set);
//		windowBackgroundImageSetCheckBox.setSelected(set);
		windowTransCheckBox.setEnabled(!set);
//		windowTransCheckBox.setSelected(!set);
		backgroundImageSetBtn.setEnabled(set && windowBackgroundImageSetCheckBox.checked());
		windowTransSlider.setEnabled(!set && windowTransCheckBox.checked());

		QRComponentUtils.setBoundsAndAddToComponent(this, windowRoundCheckBox, 25, 30, 140, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, windowOnTopCheckBox, 25, 75, 140, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, splitSeparator, 25, 120, 450, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, mainWindowTipLabel, 25, 160, 75, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, windowTransCheckBox, 50, 205, 140, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, windowTransSlider, 200, 205, 125, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, windowScreenAdsorbCheckBox, 50, 250, 140, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, windowBackgroundImageSetCheckBox, 50, 295, 140, 30);
		QRComponentUtils.setBoundsAndAddToComponent(this, backgroundImageSetBtn, 200, 295, 125, 30);
	}

	class BackgroundImageSelectDialog extends QRDialog {
		private final QRFilePathTextField textField;
		private final String backgroundImagePathBackup = QRSwing.windowBackgroundImagePath;

		private BackgroundImageSelectDialog() {
			super(WindowPanel.this.window);
			setTitle("选择背景图片");
			setTitlePlace(QRDialog.CENTER);
			setSize(640, 515);

			this.mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.mainPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					BackgroundImageSelectDialog.this.mainPanel.grabFocus();
				}
			});

			QRRoundButton sureBtn = new QRRoundButton("确定");

			QRPanel backgroundImagePanel = new QRPanel() {
				@Override
				public void setBorder(Border border) {

					super.setBorder(border);
					if (border != null) {
						QRComponentUtils.windowFresh(this);
					}
				}
			};
			backgroundImagePanel.setLayout(new BorderLayout());
			QRTextArea area = new QRTextArea();
			area.setEditable(false);
			backgroundImagePanel.add(area.addScrollPane());
			QRComponentUtils.loopComsForBackgroundSetting(backgroundImagePanel);

			this.textField = new QRFilePathTextField() {
				@Override
				protected boolean meetCondition() {
					final boolean meetCondition = super.meetCondition();
					if (!meetCondition) {
						backgroundImagePanel.setToolTipText("选择图片以预览");
						if (getText().isEmpty()) {
							sureBtn.setEnabled(true);
						}
					}
					return meetCondition;
				}
			};
			this.textField.setToolTipText("清空内容即取消背景图。");

			final boolean fileExists = QRFileUtils.fileExists(this.backgroundImagePathBackup);

			if (fileExists) {
				backgroundImagePanel.setToolTipText(null);
				Image image = QRSwingInfo.loadImage(this.backgroundImagePathBackup);
				QRBackgroundBorder backgroundBorder = new QRBackgroundBorder(image);
				backgroundImagePanel.setBorder(backgroundBorder);
				this.textField.setText(this.backgroundImagePathBackup);
			}

			sureBtn.addActionListener(e -> sureBtnAction());
			sureBtn.setEnabled(fileExists || this.backgroundImagePathBackup == null || this.backgroundImagePathBackup.isEmpty());

			QRFileSelectButton selectBtn = new QRFileSelectButton(this, "图片文件", "jpg", "png", "jpeg", "jfif") {
				@Override
				public void successAction(File selectedFile, String selectedFilePath) {
					backgroundImagePanel.setToolTipText(null);
					Image image = QRSwingInfo.loadImage(selectedFilePath);
					QRBackgroundBorder backgroundBorder = new QRBackgroundBorder(image);
					backgroundImagePanel.setBorder(backgroundBorder);
//					backgroundImagePanel.setSize(image.getWidth(null), image.getHeight(null));
					BackgroundImageSelectDialog.this.textField.setText(selectedFilePath);
					sureBtn.setEnabled(true);
				}

				@Override
				public void failedAction() {
					backgroundImagePanel.setBorder(null);
					sureBtn.setEnabled(false);
				}
			};
			QRSlider alphaSlider = new QRSlider();
			alphaSlider.setBoundValue(50, 100);
			alphaSlider.setValue((int) (100 * QRSwing.windowBackgroundImageAlpha));
			alphaSlider.addChangeListener(e -> {
				Border border = backgroundImagePanel.getBorder();
				if (border instanceof QRBackgroundBorder b) {
					int v = alphaSlider.getValue();
					float alpha = v / 100f;
					b.setAlpha(alpha);
					QRComponentUtils.windowFreshRightNow(backgroundImagePanel);
					SettingsItem.CHANGE_MAP.put(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA, String.valueOf(alpha));
				}
			});

			backgroundImagePanel.setBounds(36, 10, 569, 343);
			alphaSlider.setBounds(405, 360, 200, 40);
			this.textField.setBounds(36, 420, 444, 30);
			selectBtn.setBounds(490, 420, 32, 32);
			sureBtn.setBounds(530, 420, 75, 30);

			this.mainPanel.add(backgroundImagePanel);
			this.mainPanel.add(alphaSlider);
			this.mainPanel.add(this.textField);
			this.mainPanel.add(selectBtn);
			this.mainPanel.add(sureBtn);
		}

		private void sureBtnAction() {
			final String path = this.textField.getText();
			if (QRFileUtils.fileExists(path)) {
				SettingsItem.SAVE_ACTIONS.put("window.image.path", e -> MainWindow.INSTANCE.setBackgroundImage(path));
				//保存，以备再次打开
				QRSwing.windowBackgroundImagePath = path;
			}
			if (SettingsItem.CHANGE_MAP.containsKey(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA)) {
				float alpha = Float.parseFloat(SettingsItem.CHANGE_MAP.get(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA));
				SettingsItem.SAVE_ACTIONS.put("window.image.alpha", e -> {
					MainWindow.INSTANCE.setBackgroundBorderAlpha(alpha);
				});
				QRSwing.windowBackgroundImageAlpha = alpha;
			}
			super.dispose();
		}

		@Override
		public void dispose() {
			if (!SettingsItem.CANCEL_ACTIONS.containsKey("window.image.path")) {
				SettingsItem.CANCEL_ACTIONS.put("window.image.path",
						e -> MainWindow.INSTANCE.setBackgroundImage(this.backgroundImagePathBackup));
			}
			super.dispose();
		}
	}
}