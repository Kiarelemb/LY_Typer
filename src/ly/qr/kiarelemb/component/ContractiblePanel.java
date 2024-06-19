package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.contract.state.*;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TipData;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRMathUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.combination.QRContractiblePanel;
import swing.qr.kiarelemb.component.utils.QRLineSeparatorLabel;
import swing.qr.kiarelemb.window.utils.QRResizableTextShowDialog;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-16 15:15
 **/
public class ContractiblePanel extends QRContractiblePanel {

    //region 跟打数据
    public static final LCDNumberLabel SPEED_LABEL = new LCDNumberLabel();
    public static final LCDNumberLabel KEY_STROKE_LABEL = new KeyStrokeLabel();
    public static final LCDNumberLabel CODE_LEN_LABEL = new LCDNumberLabel();
    public static final LCDNumberLabel STANDARD_LEN_LABEL = new StandardLengthLabel();
    public static final LCDNumberLabel TIME_LABEL = new LCDNumberLabel();
    //endregion 跟打数据

    //region 本段信息
    private static final QRLabel paraLabel = new QRLabel("0");
    private static final QRLabel wordNumLabel = new QRLabel("0");
    private static final QRLabel langLabel = new QRLabel("无");
    private static final QRLabel numberLabel = new QRLabel("0");
    private static final QRLabel markLabel = new QRLabel("0");
    private static final QRLabel codeLabel = new QRLabel("无");
    private static final QRLabel paraForeLabel = new QRLabel("无");
    private static final QRLabel paraBackLabel = new QRLabel("无");
    //endregion 本段信息

    //region 标顶数据
    private static final DoubleNumLabel stanSingleLabel = new DoubleNumLabel();
    private static final DoubleNumLabel firstMultiLabel = new ContractiblePanel.DoubleNumLabel();
    private static final DoubleNumLabel singlePhraseLabel = new ContractiblePanel.DoubleNumLabel();
    private static final DoubleNumLabel oneLabel = new DoubleNumLabel();
    private static final DoubleNumLabel twoLabel = new DoubleNumLabel();
    private static final DoubleNumLabel threeLabel = new DoubleNumLabel();
    private static final DoubleNumLabel fourLabel = new DoubleNumLabel();
    private static final DoubleNumLabel allSpaceLabel = new DoubleNumLabel();
    private static final DoubleNumLabel leftRightLabel = new DoubleNumLabel();
    //endregion

    public final QRColumnContentPanel typingStatisticsPanel = addColumn("跟打数据", 360);
    public final QRColumnContentPanel stateInfoPanel = addColumn("当前状态", 270);
    public final QRColumnContentPanel paraInfoPanel = addColumn("本段信息", 350);
    public final QRColumnContentPanel standardDataPanel = addColumn("标顶数据", 475);
    public static final ContractiblePanel CONTRACTIBLE_PANEL = new ContractiblePanel();

    private ContractiblePanel() {
        super(250);

        //跟打数据
        init_typingStatisticsPanel();
        //endregion

        //region 当前状态
        init_stateInfoPanel();
        //endregion

        //region 本段信息
        init_paraInfoPanel();
        //endregion

        //region 标顶数据
        init_standardDataPanel();
        //endregion

        //预设
        this.typingStatisticsPanel.column().setFold(Keys.boolValue(Keys.WINDOW_COLUMN_FOLD_TYPING_STATISTICS));
        this.stateInfoPanel.column().setFold(Keys.boolValue(Keys.WINDOW_COLUMN_FOLD_STATE_INFO));
        this.paraInfoPanel.column().setFold(Keys.boolValue(Keys.WINDOW_COLUMN_FOLD_PARA_INFO));
        this.standardDataPanel.column().setFold(Keys.boolValue(Keys.WINDOW_COLUMN_FOLD_STANDARD_STATISTICS));
        //保存
        this.typingStatisticsPanel.column().addFoldAction(e -> QRSwing.setGlobalSetting(Keys.WINDOW_COLUMN_FOLD_TYPING_STATISTICS, e));
        this.stateInfoPanel.column().addFoldAction(e -> QRSwing.setGlobalSetting(Keys.WINDOW_COLUMN_FOLD_STATE_INFO,
                e));
        this.paraInfoPanel.column().addFoldAction(e -> QRSwing.setGlobalSetting(Keys.WINDOW_COLUMN_FOLD_PARA_INFO, e));
        this.standardDataPanel.column().addFoldAction(e -> QRSwing.setGlobalSetting(Keys.WINDOW_COLUMN_FOLD_STANDARD_STATISTICS, e));

        TextPane.TEXT_PANE.addSetTextBeforeAction(e -> paraInfoUpdate());
        TextPane.TEXT_PANE.addSetTextBeforeAction(e -> standardDataUpdate());
    }

    private void init_typingStatisticsPanel() {
        QRLabel speedIconLabel = new QRLabel(Info.loadImage(Info.SPEED_ICON));
        QRLabel keyStrokeIconLabel = new QRLabel(Info.loadImage(Info.KEYSTROKE_PNG));
        QRLabel codeLenIconLabel = new QRLabel(Info.loadImage(Info.CODE_LEN_PNG));
        QRLabel standardLenIconLabel = new QRLabel(Info.loadImage(Info.STANDARD_LEN_PNG));
        QRLabel timeIconLabel = new QRLabel(Info.loadImage(Info.TIME_PNG));

        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, speedIconLabel, 25, 35, 32, 32);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, SPEED_LABEL, 75, 20, 170, 50);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, keyStrokeIconLabel, 25, 100, 32, 32);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, KEY_STROKE_LABEL, 75, 85, 170, 50);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, codeLenIconLabel, 25, 165, 32, 32);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, CODE_LEN_LABEL, 75, 150, 170, 50);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, standardLenIconLabel, 25, 230, 32, 32);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, STANDARD_LEN_LABEL, 75, 215, 170, 50);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, timeIconLabel, 25, 295, 32, 32);
        QRComponentUtils.setBoundsAndAddToComponent(this.typingStatisticsPanel, TIME_LABEL, 75, 280, 170, 50);
    }

    private void init_stateInfoPanel() {
        QRLabel wordLabelTip = new QRLabel("字数统计：");
        QRLabel groupLabelTip = new QRLabel("当前群聊：");
        QRLabel diveLabelTip = new QRLabel("是否潜水：");
        QRLabel silkyLabelTip = new QRLabel("丝滑模式：");
        QRLabel cryptographicLabelTip = new QRLabel("加密模式：");
        QRLabel lookLabelTip = new QRLabel("看打模式：");

        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, groupLabelTip, 15, 20, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, GroupButton.groupBtn, 100, 20, 135, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, wordLabelTip, 15, 60, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, WordLabel.wordLabel, 100, 60, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, diveLabelTip, 15, 100, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, DiveCheckBox.diveCheckBox, 100, 100, 60, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, silkyLabelTip, 15, 140, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, SilkyModelCheckBox.silkyCheckBox, 100, 140, 60, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, cryptographicLabelTip, 15, 180, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, CryptographicCheckBox.cryptographicCheckBox, 100, 180, 60, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, lookLabelTip, 15, 220, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.stateInfoPanel, LookModelCheckBox.lookModelCheckBox, 100, 220, 60, 30);
    }

    private void init_paraInfoPanel() {
        QRLabel paraLabelTip = new QRLabel("段号：");
        QRLabel wordNumLabelTip = new QRLabel("字数：");
        QRLabel langLabelTip = new QRLabel("语言：");
        QRLabel numberLabelTip = new QRLabel("数字：");
        QRLabel markLabelTip = new QRLabel("标点：");
        QRLabel codeLabelTip = new QRLabel("正文：");
        QRLabel paraForeLabelTip = new QRLabel("段前：");
        QRLabel paraBackLabelTip = new QRLabel("段尾：");

        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, paraLabelTip, 15, 20, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, paraLabel, 70, 20, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, wordNumLabelTip, 15, 60, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, wordNumLabel, 70, 60, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, langLabelTip, 15, 100, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, langLabel, 70, 100, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, numberLabelTip, 15, 140, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, numberLabel, 70, 140, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, markLabelTip, 15, 180, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, markLabel, 70, 180, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, codeLabelTip, 15, 220, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, codeLabel, 70, 220, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, paraForeLabelTip, 15, 260, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, paraForeLabel, 70, 260, 155, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, paraBackLabelTip, 15, 300, 55, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.paraInfoPanel, paraBackLabel, 70, 300, 155, 30);
    }

    private void init_standardDataPanel() {
        QRLabel stanSingleLabelTip = new QRLabel("标顶 : 单字");
        QRLabel firstMultiLabelTip = new QRLabel("首选 : 选重");
        QRLabel singlePhraseLabelTip = new QRLabel("单量 : 词量");
        QRLabel separator1 = new QRLineSeparatorLabel();
        QRLabel oneLabelTip = new QRLabel("一首 : 一重");
        QRLabel twoLabelTip = new QRLabel("二首 : 二重");
        QRLabel threeLabelTip = new QRLabel("三首 : 三重");
        QRLabel fourLabelTip = new QRLabel("四首 : 四重");
        QRLabel separator2 = new QRLineSeparatorLabel();
        QRLabel allSpaceLabelTip = new QRLabel("总键 : 空格");
        QRLabel leftRightLabelTip = new QRLabel("左手 : 右手");

        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, stanSingleLabelTip, 15, 20, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, stanSingleLabel, 110, 20, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, firstMultiLabelTip, 15, 60, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, firstMultiLabel, 110, 60, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, separator1, 15, 135, 220, 20);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, oneLabelTip, 15, 160, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, oneLabel, 110, 160, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, twoLabelTip, 15, 200, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, twoLabel, 110, 200, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, threeLabelTip, 15, 240, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, threeLabel, 110, 240, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, fourLabelTip, 15, 280, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, fourLabel, 110, 280, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, separator2, 15, 315, 220, 20);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, allSpaceLabelTip, 15, 340, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, allSpaceLabel, 110, 340, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, leftRightLabelTip, 15, 380, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, leftRightLabel, 110, 380, 125, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, StandardCodeLengthBtn.STANDARD_CODE_LENGTH_BTN, 15, 425, 220, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, singlePhraseLabelTip, 15, 100, 85, 30);
        QRComponentUtils.setBoundsAndAddToComponent(this.standardDataPanel, singlePhraseLabel, 110, 100, 125, 30);
    }

    private static void paraInfoUpdate() {
        paraLabel.setText(TextLoad.TEXT_LOAD.para());
        wordNumLabel.setText(TextLoad.TEXT_LOAD.isEnglish() ? TextLoad.TEXT_LOAD.englishWordsNum() :
                TextLoad.TEXT_LOAD.wordsLength());
        langLabel.setText(TextLoad.TEXT_LOAD.isEnglish() ? "英文" : "中文");
        numberLabel.setText(TextLoad.TEXT_LOAD.numberNum());
        markLabel.setText(TextLoad.TEXT_LOAD.markNum());
        codeLabel.setText(TextLoad.TEXT_LOAD.textMD5Short());
        paraForeLabel.setText(TextLoad.TEXT_LOAD.foreTextShort());
        paraForeLabel.setToolTipText(TextLoad.TEXT_LOAD.foreText());
        paraBackLabel.setText(TextLoad.TEXT_LOAD.endTextShort());
        paraBackLabel.setToolTipText(TextLoad.TEXT_LOAD.endText());
    }

    private static void standardDataUpdate() {
        TipData tipData = TextLoad.TEXT_LOAD.tipData;
        if (tipData == null) {
            return;
        }
        TipData.StandardData data = tipData.data;
        double stan = QRMathUtils.doubleFormat((tipData.codes.length() + 0.0) / TextLoad.TEXT_LOAD.wordsLength());
        double single = QRMathUtils.doubleFormat((tipData.singleCodeNum + 0.0) / TextLoad.TEXT_LOAD.wordsLength());
        double singleCounts =
                100 * QRMathUtils.doubleFormat((data.singleCounts() + 0.0) / TextLoad.TEXT_LOAD.wordsLength(), 4);
        stanSingleLabel.update(stan, single);
        firstMultiLabel.update(data.first(), data.multi());
        singlePhraseLabel.update(
                QRMathUtils.toString(singleCounts, 2, false) + "%",
                QRMathUtils.toString(data.phraseTypeCounts(), 2, false) + "%");
        oneLabel.update(data.oneFirst(), data.oneMulti());
        twoLabel.update(data.twoFirst(), data.twoMulti());
        threeLabel.update(data.threeFirst(), data.threeMulti());
        fourLabel.update(data.fourFirst(), data.fourMulti());
        allSpaceLabel.update(tipData.codes.length(), data.spaceCounts());
        leftRightLabel.update(data.leftCounts(), data.rightCounts());
    }

    public static class KeyStrokeLabel extends LCDNumberLabel {

        private KeyStrokeLabel() {
            addMouseListener();
        }


        @Override
        protected void mouseClick(MouseEvent e) {
            if (TypingData.typedKeyRecord.isEmpty()) {
                return;
            }
            String str = TypingData.typedKeyRecord.toString();
            QRResizableTextShowDialog dialog = new QRResizableTextShowDialog(MainWindow.INSTANCE, 400, 300, "按键数据",
                    str, true);
            dialog.setVisible(true);
        }

        @Override
        protected final void mouseEnter(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected final void mouseExit(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private static class StandardLengthLabel extends LCDNumberLabel {
        private StandardLengthLabel() {
            addMouseListener();
        }

        @Override
        protected void mouseClick(MouseEvent e) {
            StandardCodeLengthBtn.STANDARD_CODE_LENGTH_BTN.clickInvokeLater();
        }

        @Override
        protected final void mouseEnter(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected final void mouseExit(MouseEvent e) {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * 标顶数据栏使用的标签
     */
    public static class DoubleNumLabel extends QRLabel {

        public DoubleNumLabel() {
            update("0", "0");
        }

        public void update(Object a, Object b) {
            setText(a + " : " + b);
        }
    }
}