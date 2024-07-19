package ly.qr.kiarelemb.setting.panel;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.CheckBox;
import ly.qr.kiarelemb.component.LineSeparatorLabel;
import ly.qr.kiarelemb.menu.type.SettingsItem;
import ly.qr.kiarelemb.setting.SettingWindow;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRRoundButton;
import swing.qr.kiarelemb.basic.QRSlider;
import swing.qr.kiarelemb.combination.QRBorderContentPanel;
import swing.qr.kiarelemb.listener.QRMouseListener;
import swing.qr.kiarelemb.resource.QRSwingInfo;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.utils.QRFilePathTextField;
import swing.qr.kiarelemb.utils.QRFileSelectButton;
import swing.qr.kiarelemb.window.basic.QRDialog;

import java.awt.*;
import java.io.File;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-31 14:28
 **/
public class WindowPanel extends SettingPanel {
    private boolean isSure = false;

    public WindowPanel(SettingWindow window) {
        super(window, "窗体...");
        QRLabel mainWindowTipLabel = new QRLabel("主窗体");
        CheckBox windowRoundCheckBox = new CheckBox("启用窗体圆角", QRSwing.WINDOW_ROUND);
        CheckBox windowOnTopCheckBox = new CheckBox("启用窗体置顶", QRSwing.WINDOW_ALWAYS_TOP);
        CheckBox windowTitleMenu = new CheckBox("菜单置于窗体标题", QRSwing.WINDOW_TITLE_MENU);
        QRLabel splitSeparator = new LineSeparatorLabel();
        //WINDOW_TRANSPARENCY_ENABLE
        QRLabel windowTransLabel = new QRLabel("设置窗体透明");
        QRSlider windowTransSlider = new QRSlider();
        CheckBox windowScreenAdsorbCheckBox = new CheckBox("启用屏幕吸附", QRSwing.WINDOW_ABSORB);
        CheckBox windowBackgroundImageSetCheckBox = new CheckBox("启用背景图", QRSwing.WINDOW_IMAGE_ENABLE);
        QRRoundButton backgroundImageSetBtn = new QRRoundButton("背景图设置");

        windowRoundCheckBox.setToolTipText("启用后，包括禅出的对话框在内，窗体都将采用圆角。");
        windowOnTopCheckBox.setToolTipText("启用后，主窗体将置顶。可能对于一些中文输入法来说，该功能不太支持。");
        windowTitleMenu.setToolTipText("启用后，菜单将置于窗体标题栏。重启生效。");
        windowTransLabel.setToolTipText("在未设置主窗体背景图的情况下，该功能适用。取消勾选以启用背景图设置。");
        windowScreenAdsorbCheckBox.setToolTipText("该功能指对屏幕四边的吸附。");
        windowBackgroundImageSetCheckBox.setToolTipText("启用后，主窗体的透明功能失效。");


        windowBackgroundImageSetCheckBox.addClickAction(e -> {
            boolean checked = windowBackgroundImageSetCheckBox.checked();
            backgroundImageSetBtn.setEnabled(checked);
            windowTransSlider.setEnabled(!checked);
            SettingsItem.SAVE_ACTIONS.putIfAbsent("window.image.path", es -> MainWindow.INSTANCE.setBackgroundImage(QRSwing.windowBackgroundImagePath));
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
            SettingsItem.SAVE_ACTIONS.put("window.alpha", es -> QRSystemUtils.setWindowTrans(MainWindow.INSTANCE, alpha));
        });

        windowTransSlider.setEnabled(!windowBackgroundImageSetCheckBox.checked());
        backgroundImageSetBtn.setEnabled(windowBackgroundImageSetCheckBox.checked());

        QRComponentUtils.setBoundsAndAddToComponent(this, windowRoundCheckBox, 25, 30, 200, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, windowOnTopCheckBox, 25, 75, 200, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, windowTitleMenu, 25, 120, 200, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, splitSeparator, 25, 160, 450, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, mainWindowTipLabel, 25, 205, 75, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, windowTransLabel, 72, 250, 140, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, windowTransSlider, 200, 250, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, windowScreenAdsorbCheckBox, 50, 295, 140, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, windowBackgroundImageSetCheckBox, 50, 340, 140, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this, backgroundImageSetBtn, 200, 340, 125, 30);

        setPreferredSize(480, 400);
    }

    class BackgroundImageSelectDialog extends QRDialog {
        private final QRFilePathTextField textField;
        private final String backgroundImagePathBackup = QRSwing.windowBackgroundImagePath;
        private final QRBorderContentPanel backgroundImagePanel;

        private BackgroundImageSelectDialog() {
            super(WindowPanel.this.window);
            setTitle("选择背景图片");
            setTitlePlace(QRDialog.CENTER);
            setSize(640, 515);

            this.mainPanel.addMouseListener();
            this.mainPanel.addMouseAction(QRMouseListener.TYPE.CLICK, e -> BackgroundImageSelectDialog.this.mainPanel.grabFocus());

            QRRoundButton sureBtn = new QRRoundButton("确定");

            backgroundImagePanel = new QRBorderContentPanel(null);
            QRLabel area = new QRLabel();
            area.setText("在工作或学习中遇到不开心的时候，不妨静下来好好想想，自己到底是对是错。生活中不是你对别人好，别人就该对你好，你要明白这个道理，每个人都有自己的原则，有人功利，有人善良，你不可能要求别人什么。有时间的话，不妨到处走走，在雄伟的高山之间，放声大喊，一吐心中的阴郁，在浪漫的大海之间，看潮起潮落，感悟人生的起伏跌宕，在落日余晖中感受天地的宁静，洗涤心中的贪念。");
            area.setSize(569, 343);
            area.setPreferredSize(new Dimension(569, 243));
            area.setLocation(5, 10);
            area.wrapText();
            backgroundImagePanel.add(area);

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
                backgroundImagePanel.setImage(image);
                backgroundImagePanel.setAlpha(QRSwing.windowBackgroundImageAlpha);
                this.textField.setText(this.backgroundImagePathBackup);
            }

            sureBtn.addClickAction(e -> sureBtnAction());
            sureBtn.setEnabled(fileExists || this.backgroundImagePathBackup == null || this.backgroundImagePathBackup.isEmpty());

            QRFileSelectButton selectBtn = new QRFileSelectButton(this, "图片文件", "jpg", "png", "jpeg", "jfif") {
                @Override
                public void successAction(File selectedFile, String selectedFilePath) {
                    backgroundImagePanel.setToolTipText(null);
                    Image image = QRSwingInfo.loadImage(selectedFilePath);
                    backgroundImagePanel.setImage(image);
                    BackgroundImageSelectDialog.this.textField.setText(selectedFilePath);
                    sureBtn.setEnabled(true);
                }

                @Override
                public void failedAction() {
                    backgroundImagePanel.setBorder(null);
                    sureBtn.setEnabled(false);
                }
            };
            QRSlider alphaSlider = getAlphaSlider();

            QRComponentUtils.setBoundsAndAddToComponent(this.mainPanel, backgroundImagePanel, 36, 10, 569, 343);
            QRComponentUtils.setBoundsAndAddToComponent(this.mainPanel, alphaSlider, 405, 360, 200, 40);
            QRComponentUtils.setBoundsAndAddToComponent(this.mainPanel, this.textField, 36, 420, 444, 30);
            QRComponentUtils.setBoundsAndAddToComponent(this.mainPanel, selectBtn, 490, 420, 32, 32);
            QRComponentUtils.setBoundsAndAddToComponent(this.mainPanel, sureBtn, 530, 420, 75, 30);
        }

        private QRSlider getAlphaSlider() {
            QRSlider alphaSlider = new QRSlider();
            alphaSlider.setBoundValue(40, 95);
            float alphaBackup = QRSwing.windowBackgroundImageAlpha;
            alphaSlider.setValue((int) (100 * QRSwing.windowBackgroundImageAlpha));
            alphaSlider.addChangeListener(e -> {
                int v = alphaSlider.getValue();
                float alpha = v / 100f;
                backgroundImagePanel.setAlpha(alpha);
                QRComponentUtils.windowFreshRightNow(backgroundImagePanel);
                SettingsItem.CHANGE_MAP.put(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA, String.valueOf(alpha));
                SettingsItem.CANCEL_ACTIONS.putIfAbsent(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA, es -> QRSwing.setWindowBackgroundImageAlpha(alphaBackup));
            });
            return alphaSlider;
        }

        private void sureBtnAction() {
            final String path = this.textField.getText();
            if (QRFileUtils.fileExists(path)) {
                QRSwing.windowBackgroundImagePath = path;
                SettingsItem.SAVE_ACTIONS.put("window.image.path", e -> MainWindow.INSTANCE.setBackgroundImage(path));
                //保存，以备再次打开
            }
            if (SettingsItem.CHANGE_MAP.containsKey(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA)) {
                float alpha = Float.parseFloat(SettingsItem.CHANGE_MAP.get(QRSwing.WINDOW_BACKGROUND_IMAGE_ALPHA));
                SettingsItem.SAVE_ACTIONS.put("window.image.alpha", e -> {
                    MainWindow.INSTANCE.setBackgroundBorderAlpha(alpha);
                });
                QRSwing.windowBackgroundImageAlpha = alpha;
            }
            isSure = true;
            super.dispose();
        }

        @Override
        public void dispose() {
            if (!isSure) {
                SettingsItem.CANCEL_ACTIONS.put("window.image.path",
                        e -> MainWindow.INSTANCE.setBackgroundImage(this.backgroundImagePathBackup));
            }
            super.dispose();
        }
    }
}