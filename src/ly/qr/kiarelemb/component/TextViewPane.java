package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.data.*;
import ly.qr.kiarelemb.dl.DangLangManager;
import ly.qr.kiarelemb.key.ActionLibrary;
import ly.qr.kiarelemb.menu.send.NextParaTextItem;
import ly.qr.kiarelemb.qq.SendText;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.send.TextSendManager;
import ly.qr.kiarelemb.text.tip.AbstractTextTip;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.basic.QRTextPane;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ly.qr.kiarelemb.component.TyperTextPane.keyCheck;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:33
 **/
public class TextViewPane extends QRTextPane {
    private static final Logger logger = QRLoggerUtils.getLogger(TextViewPane.class);
    //    private static final ThreadPoolExecutor typingEndThread = QRThreadBuilder.singleThread("typingEnd");
    private boolean writeBlock = false;
    private final LinkedList<QRActionRegister<Object>> setTextBeforeActions = new LinkedList<>();
    private final LinkedList<QRActionRegister<Object>> setTextFinishedActions = new LinkedList<>();
    public static final TextViewPane TEXT_VIEW_PANE = new TextViewPane();
    private final TextPanelEditorKit textPanelEditorKit;

    private TextViewPane() {
        addMouseListener();
        addKeyListener();
        textPanelEditorKit = new TextPanelEditorKit(this);
        setEditorKit(textPanelEditorKit);
        setLineSpacing(Keys.floatValue(Keys.TEXT_LINE_SPACE));
        setOpaque(false);
        this.caret.setVisible(Keys.boolValue(Keys.TYPE_SILKY_MODEL) && !Keys.boolValue(Keys.TYPE_MODEL_LOOK));

        this.setTextBeforeActions.add(ActionLibrary.TEXT_VIEW_PANE_UPDATE_ACTION);
        this.setTextFinishedActions.add(e -> {
            TextViewPane.TEXT_VIEW_PANE.setCaretPosition(0);
            TextViewPane.TEXT_VIEW_PANE.addScrollPane().locationFresh();
            MainWindow.INSTANCE.grabFocus();
            QRComponentUtils.runLater(200, es -> indexesUpdate());
        });
    }

    public void setTypeText(String text) {
        if (text == null) {
            return;
        }
        TextLoad tls = new TextLoad(text);
        if (!tls.isText()) {
            return;
        }
        SplitPane.SPLIT_PANE.switchTipPanel();
        if (TextLoad.TEXT_LOAD == null) {
            TextLoad.TEXT_LOAD = tls;
        } else if (!TextLoad.TEXT_LOAD.textNotIsChanged(tls.formattedActualText())) {
            boolean isUpdate = TextLoad.TEXT_LOAD.isEnglish() != tls.isEnglish();
            TextLoad.TEXT_LOAD = tls;
            if (isUpdate) {
                TextStyleManager.updateAll();
            }
        }
        String[] lines = String.format("[%s]", text).split("\n");
        int length = lines.length;
        if (length == 1) {
            logger.info(String.format("已载入文本：[%s]", text));
        } else {
            logger.info(String.format("已载入文本：[%s]", text.replaceAll("\n", "\\\\n")));
        }
        textFresh();
    }

    public void textFresh() {
        SwingUtilities.invokeLater(() -> QRComponentUtils.runActions(TextViewPane.TEXT_VIEW_PANE.setTextBeforeActions, null));
        super.setText(null);
        // 消除 alt+3 带来的瞬间红字
        print(TextLoad.TEXT_LOAD.formattedActualText(), TextStyleManager.getDefaultStyle(), 0);
        printTextStyleAfterSetText();
        SwingUtilities.invokeLater(() -> QRComponentUtils.runActions(TextViewPane.TEXT_VIEW_PANE.setTextFinishedActions, null));
    }

    private void printTextStyleAfterSetText() {
        if (!TypingData.tipEnable || !AbstractTextTip.TEXT_TIP.loaded() || TextLoad.TEXT_LOAD.isEnglish()) {
            return;
        }
        if (Keys.boolValue(Keys.TEXT_TIP_PAINT_COLOR)) {
            textPanelEditorKit.changeFontColor();
        }
    }

    // 累积的字符
    private final StringBuilder accumulatedChars = new StringBuilder();
    // 用于同步的锁对象
    private final Object lock = new Object();
    // 创建一个调度器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    // 用于取消未执行的延迟任务
    private Future<?> futureTask = null;
    private final AtomicLong lastInputTime = new AtomicLong(0);

    /**
     * 统计打词的延时上屏算法
     *
     * @param c 程序内部单次接受的字符
     *          （20毫秒内未再次输入则算单字，否则，算多次。拓展字情况已顺带解决。
     */
    public void insertUpdates(char c) {
        final long time = System.currentTimeMillis();
        // 更新滚动条
        scrollUpdate();
        // 确保线程安全
        synchronized (lock) {
            // 取消之前的延迟任务（如果存在）
            if (futureTask != null && !futureTask.isDone()) {
                futureTask.cancel(false);
            }
            // 连续的英文输入，即使是按着一个键不动，最短时间间隔也有20+毫秒
            int shortestInputTimeDiff = 15;
            //累积字符
            accumulatedChars.append(c);
            // 设置延迟任务在 15 毫秒后执行
            futureTask = scheduler.schedule(() -> {
//                synchronized (lock) {
                // 执行累积的字符操作
                if (accumulatedChars.isEmpty()) {
                    return;
                }
                try {
                    lastInputTime.set(time);
                    insertUpdateExecute(accumulatedChars.toString());
                    // 重置累积的字符
                    accumulatedChars.setLength(0);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "insertUpdateExecute", e);
                }
//                }
            }, shortestInputTimeDiff, TimeUnit.MILLISECONDS);
        }
    }

    public void insertUpdateExecute(String text) {
        if (this.writeBlock) {
            return;
        }
        int index = TypingData.currentTypedIndex;
        final String originText;
        String[] textParts = QRStringUtils.getChineseExtraPhrase(text);
        originText = TextLoad.TEXT_LOAD.wordPartsCopyRange(index, textParts.length);
        int length = originText.length();
        boolean thisWordIsRight = text.equals(originText);
        if (thisWordIsRight) {
            if (!TextLoad.TEXT_LOAD.isEnglish() || QRStringUtils.A_WHITE_SPACE.equals(text)) {
                WordLabel.typePlus(length);
            }
            if (!ContractiblePanel.SILKY_MODEL_CHECK_BOX.checked() && !ContractiblePanel.LOOK_MODEL_CHECK_BOX.checked()) {
                changeTextsStyle(TypingData.currentTypedIndex, length, TextStyleManager.getCorrectStyle(), true);
            }
            TypeRecordData.putWords(lastInputTime.get(), length);
        } else {
            if (length == 1) {
                //否则判错，背景颜色改成红色
                if (!ContractiblePanel.LOOK_MODEL_CHECK_BOX.checked()) {
                    changeTextsStyle(TypingData.currentTypedIndex, length, TextStyleManager.getWrongStyle(), true);
                }
                TypingData.WRONG_WORDS_INDEX.add(index);
            } else {
                for (int i = 0; i < length; i++) {
                    int currentIndex = TypingData.currentTypedIndex + i;
                    String rightWord = TextLoad.TEXT_LOAD.getWordPartsAtIndex(currentIndex);
                    if (Objects.equals(rightWord, textParts[i])) {
                        if (!ContractiblePanel.SILKY_MODEL_CHECK_BOX.checked() && !ContractiblePanel.LOOK_MODEL_CHECK_BOX.checked()) {
                            changeTextsStyle(currentIndex, rightWord.length(), TextStyleManager.getCorrectStyle(), true);
                        }
                        TypeRecordData.putWords(lastInputTime.get(), 1);
                        WordLabel.typedOneWord();
                    } else {
                        if (!ContractiblePanel.LOOK_MODEL_CHECK_BOX.checked()) {
                            changeTextsStyle(currentIndex, rightWord.length(), TextStyleManager.getWrongStyle(), true);
                        }
                        TypingData.WRONG_WORDS_INDEX.add(currentIndex);
                    }
                }
            }
        }
        setCaretBlock();
        //更新位置
        TypingData.currentTypedIndex += length;
        setCaretPosition(TypingData.currentTypedIndex);
        TyperTextPane.TYPER_TEXT_PANE.runTypedActions(TypingData.currentTypedIndex);
        setCaretUnblock();
        // 未结束
        if (TextLoad.TEXT_LOAD.wordsLength() != TypingData.currentTypedIndex) {
            return;
        }
        if (!Keys.boolValue(Keys.TYPE_END_CONDITION_NO_WRONG) || TypingData.WRONG_WORDS_INDEX.isEmpty()) {
            //打字结束
            TypingData.typing = false;
            TypingData.typeEnd = true;
            TypingData.shutdown();
            QRComponentUtils.runLater(10, e -> typeEnding());
        }
    }

    public void deleteUpdates(KeyEvent e) {
        e.consume();
        int index = this.caret.getDot();
        if (index <= 1 || TypingData.backspaceAutoRestart) {
            restart();
            return;
        }
        String deleteText = TextLoad.TEXT_LOAD.getWordPartsAtIndex(TypingData.currentTypedIndex - 1);
        if (deleteText.isEmpty()) {
            return;
        }
        writeBlock = true;
        int preIndex = index - 1;
        setCaretBlock();
        TypingData.currentTypedIndex--;
        int length = deleteText.length();
        TypingData.WRONG_WORDS_INDEX.remove(Integer.valueOf(preIndex));
        if (!TextTip.TEXT_TIP.loaded() || TextLoad.TEXT_LOAD.tipData == null) {
            changeTextsStyle(preIndex, length, TextStyleManager.getDefaultStyle(), true);
        } else {
            TipPhraseStyleData tipPhraseStyleData = TextLoad.TEXT_LOAD.tipData.tpsd.get(preIndex);
            if (tipPhraseStyleData != null) {
                SimpleAttributeSet attributeSet = tipPhraseStyleData.getStyle();
                replaceText(preIndex, tipPhraseStyleData.phrase(), attributeSet);
            } else {
                SimpleAttributeSet attributeSet = TextLoad.TEXT_LOAD.tipData.tcsd.get(preIndex).getStyle();
                changeTextsStyle(preIndex, length, attributeSet, true);
            }
        }
        setCaretPosition(preIndex);
        TypingData.backDeleteCount++;
        setCaretUnblock();
        TyperTextPane.TYPER_TEXT_PANE.runTypedActions(TypingData.currentTypedIndex);
        writeBlock = false;
    }

    public void typeEnding() {
        long time = TypingData.endTime - TypingData.startTime;
        double totalTimeInSec = time / 1000.0D;
        String totalTimeInSecs = String.format("%.2f", totalTimeInSec);
        //用时_分
        double totalTimeInMin = totalTimeInSec / 60.0D;
        //速度
        double s = (!TypingData.WRONG_WORDS_INDEX.isEmpty() ?
                TextLoad.TEXT_LOAD.wordsLength() - 5 * TypingData.WRONG_WORDS_INDEX.size() :
                TextLoad.TEXT_LOAD.wordsLength()) / totalTimeInMin;
        //速度过小，不统计
        if (totalTimeInSec <= 0 || s < 1) {
            QRLoggerUtils.log(logger, Level.WARNING, "速度 %f 过小，不统计", s);
            QRSmallTipShow.display(MainWindow.INSTANCE, "继续重打吧！");
            return;
        }
        double speed = QRMathUtils.doubleFormat(s);
        //英打字数
        if (TextLoad.TEXT_LOAD.isEnglish()) {
            WordLabel.typedOneWord();
        }
        //击键
        double keyStroke = QRMathUtils.doubleFormat(TypingData.keyCounts / totalTimeInSec);
        //码长
        double codeLength = QRMathUtils.doubleFormat(TypingData.keyCounts / (TextLoad.TEXT_LOAD.wordsLength() + 0.00));
        //用时
        String timeCost = QRTimeUtils.getTimeCost(totalTimeInSec, totalTimeInMin);
        ContractiblePanel.TIME_LABEL.setText(totalTimeInSecs);
        ContractiblePanel.SPEED_LABEL.setText(String.valueOf(speed));
        ContractiblePanel.KEY_STROKE_LABEL.setText(String.valueOf(keyStroke));
        ContractiblePanel.CODE_LEN_LABEL.setText(String.valueOf(codeLength));
        GradeData gradeData = new GradeData(totalTimeInMin, speed, keyStroke, codeLength, timeCost);
        String grade = gradeData.getSetGrade();

        QRLoggerUtils.log(logger, Level.INFO, "跟打按键：[%s]", QRStringUtils.toLowerCase(TypingData.typedKeyRecord.toString()));
        QRLoggerUtils.log(logger, Level.INFO, "本段成绩：[%s]", gradeData.getFullGrade());
        HistoryGradePane.HISTORY_GRADE_PANE.println(gradeData.toString());

        //将成绩存放至剪贴板
        QRSystemUtils.putTextToClipboard(grade);
        DangLangManager.DANG_LANG_MANAGER.save(TextLoad.TEXT_LOAD.textMD5Long());

        //发送成绩
        SendText.gradeSend();
        logger.info("********** 本段结束 **********");
        if (TextSendManager.sendingText()) {
            TextSendManager.data().addTypedTimes();
            NextParaTextItem.NEXT_PARA_TEXT_ITEM.clickInvokeLater();
            logger.config(TextSendManager.data().toString());
        } else if (Keys.boolValue(Keys.TYPE_END_MIX_RESTART)) {
            if (TextLoad.TEXT_LOAD.singleOnly() || TextLoad.TEXT_LOAD.isEnglishPhrase()) {
                TextLoad.TEXT_LOAD.actualContentMix();
            }
            restart();
        }
    }

    private int preLine = -1;

    private void scrollUpdate() {
        var verticalScrollBar = addScrollPane().verticalScrollBar();
        if (!verticalScrollBar.isVisible()) {
            return;
        }
        // 要注意 currentLineAndRow 方法的传入参数
        final int[] lineAndRow = currentLineAndRow(TypingData.currentTypedIndex);
        final int currentLine = lineAndRow[0];
        final double currentRow = lineAndRow[1];
        var updateCondition = currentLine > preLine;
        preLine = currentLine;
        //行尾更新
        if (!updateCondition) {
            return;
        }
        final int lineWords = lineWords();
        double startUpdateLine = 3;
        if (currentLine >= startUpdateLine) {
            QRComponentUtils.runLater(100, e -> {
                int max = verticalScrollBar.getMaximum() - verticalScrollBar.getHeight();
                double value = ((currentLine - startUpdateLine) + currentRow / lineWords) * linePerHeight();
                verticalScrollBar.setValue((int) (Math.min(value, max)));
            });
        }
    }

    /**
     * 调用后重打次数增加
     */
    public void restart() {
        if (TextLoad.TEXT_LOAD == null) {
            return;
        }
        TextLoad.TEXT_LOAD.reTypeTimesAdd();
        QRLoggerUtils.log(logger, Level.INFO, "手动重打，当前第 %s 次", TextLoad.TEXT_LOAD.reTypeTimes());
        simpleRestart();
    }

    /**
     * 该方法不增加重打次数
     */
    public void simpleRestart() {
        if (TextLoad.TEXT_LOAD == null) {
            return;
        }
        TEXT_VIEW_PANE.setTypeText(TextLoad.TEXT_LOAD.currentText());
        DangLangManager.DANG_LANG_MANAGER.save(TextLoad.TEXT_LOAD.textMD5Long());
        MainWindow.INSTANCE.grabFocus();
    }

    public void caretPositionAdjust() {
        if (this.caretBlock) {
            return;
        }
        int caretPosition = getCaretPosition();
        if (TypingData.currentTypedIndex != caretPosition) {
            this.caretBlock = true;
            setCaretPosition(TypingData.currentTypedIndex);
            this.caretBlock = false;
        }
    }

    public void addSetTextBeforeAction(QRActionRegister<Object> ar) {
        this.setTextBeforeActions.add(ar);
    }

    public void addSetTextFinishedAction(QRActionRegister<Object> ar) {
        this.setTextFinishedActions.add(ar);
    }

    //region 方法重写

    @Deprecated
    @Override
    public void setText(String text) {
    }

    @Override
    protected void keyPress(KeyEvent e) {
//        if (!TypingData.typing) {
//            e.consume();
//        }
        if (e.getKeyChar() == '\b') {
            e.consume();
            return;
        }
        if (keyCheck(e)) {
            e.consume();
        }
    }

    @Override
    protected void keyType(KeyEvent e) {
        e.consume();
    }


    @Override
    protected void pasteAction() {
        String text = QRSystemUtils.getSysClipboardText();
        if (text == null || text.isBlank()) {
            return;
        }
        text = text.trim();
        final int lastIndexOf = text.lastIndexOf(QRStringUtils.AN_ENTER);
        int diIndex = text.indexOf(TextLoad.DI, lastIndexOf);
        int duanIndex = text.indexOf(TextLoad.DUAN, diIndex + 1);
        if (diIndex == -1 || duanIndex <= diIndex + 1 || !QRStringUtils.isNumber(text.substring(diIndex + 1, duanIndex))) {
            if (lastIndexOf != -1 && text.indexOf(QRStringUtils.AN_ENTER) < lastIndexOf) {
                text = QRStringUtils.lineSeparatorClear(text, true);
            }
            text += "\n-----第" + QRRandomUtils.getRandomInt(999999) + "段";
        }
        if (QRStringUtils.markCount(text, '\n') == 1) {
            text = "剪贴板\n" + text;
        }
        TextViewPane.TEXT_VIEW_PANE.setTypeText(text);
    }

    @Override
    protected void mousePress(MouseEvent e) {
        caretPositionAdjust();
    }

    private boolean paintLock = false;

    @Override
    public synchronized void paint(Graphics g) {
        // 用于减少重绘 bug 的产生
        if (paintLock) {
            return;
        }
        try {
            paintLock = true;
            super.paint(g);
        } catch (Exception ignore) {
        } finally {
            paintLock = false;
        }
    }

    @Override
    public void setLineSpacing(float lineSpacing) {
        if (lineSpacing > 1.5f || lineSpacing < 0.5f) {
            lineSpacing = 0.8f;
        }
        super.setLineSpacing(lineSpacing);
    }

    @Override
    public void componentFresh() {
        this.textFont = QRColorsAndFonts.STANDARD_FONT_TEXT.deriveFont((float) Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
        super.componentFresh();
    }
    //endregion
}