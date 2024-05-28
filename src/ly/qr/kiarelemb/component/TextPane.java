package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.contract.state.LookModelCheckBox;
import ly.qr.kiarelemb.component.contract.state.SilkyModelCheckBox;
import ly.qr.kiarelemb.component.contract.state.WordLabel;
import ly.qr.kiarelemb.component.menu.send.NextParaTextItem;
import ly.qr.kiarelemb.data.GradeData;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.dl.DangLangManager;
import ly.qr.kiarelemb.qq.SendText;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.AbstractTextTip;
import ly.qr.kiarelemb.text.tip.TextTip;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import ly.qr.kiarelemb.text.tip.data.TipCharStyleData;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRScrollPane;
import swing.qr.kiarelemb.component.basic.QRTextPane;
import swing.qr.kiarelemb.inter.QRActionRegister;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:33
 **/
public class TextPane extends QRTextPane {
    private static final ThreadPoolExecutor typingEndThread = QRThreadBuilder.singleThread("typingEnd");
    public static final int TYPING_FINISH_MARK_OVER_DO_NOTING = 0;
    public static final int TYPING_FINISH_MARK_OVER_NO_WRONG = 1;
    public static final int TYPING_FINISH_MARK_OVER_NO_WRONG_INTELLIGENCE = 2;
    public static final int TYPING_FINISH_MARK_OVER_CAN_WRONG_INTELLIGENCE = 3;
    private boolean writeBlock = false;
    private final LinkedList<QRActionRegister> setTextBeforeActions = new LinkedList<>();
    private final LinkedList<QRActionRegister> setTextFinishedActions = new LinkedList<>();
    public static final TextPane TEXT_PANE = new TextPane();
    private final TextPanelEditorKit textPanelEditorKit;

    private TextPane() {
        addMouseListener();
        addKeyListener();
//		addMouseMotionListener();
        textPanelEditorKit = new TextPanelEditorKit(this);
        setEditorKit(textPanelEditorKit);
        setLineSpacing(Keys.floatValue(Keys.TEXT_LINE_SPACE));
        this.caret.setVisible(Keys.boolValue(Keys.TYPE_SILKY_MODEL) && !Keys.boolValue(Keys.TYPE_MODEL_LOOK));
        this.setTextBeforeActions.add(e -> {
            //更新数据
            TypingData.dataUpdate();
            //重置数据
            TypingData.clear();
            this.caret.setVisible(SilkyModelCheckBox.silkyCheckBox.checked() && !LookModelCheckBox.lookModelCheckBox.checked());
        });
        this.setTextFinishedActions.add(e -> {
            setCaretPosition(0);
            indexesUpdate();
            TextPane.TEXT_PANE.scrollPane.locationFresh();
            MainWindow.INSTANCE.grabFocus();
        });
    }


    @Deprecated
    @Override
    public void setText(String text) {
    }

    public void setTypeText(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        if (getTextLoadUpdate(text)) {
            return;
        }
        textFresh();
    }

    public void textFresh() {
        QRComponentUtils.runActions(this.setTextBeforeActions);
        super.setText(null);
        super.setText(TextLoad.TEXT_LOAD.formattedActualText());
        printTextStyleAfterSetText();
        SwingUtilities.invokeLater(() -> QRComponentUtils.runActions(TextPane.TEXT_PANE.setTextFinishedActions));
    }

    private void printTextStyleAfterSetText() {
        if (TypingData.tipEnable && AbstractTextTip.TEXT_TIP.loaded() && !TextLoad.TEXT_LOAD.isEnglish()) {
            if (Keys.boolValue(Keys.TEXT_TIP_PAINT_COLOR)) {
                textPanelEditorKit.changeFontColor(TextLoad.TEXT_LOAD.tipData.tpsd);
                return;
            }
        }
        changeTextsStyle(0, TextLoad.TEXT_LOAD.wordsLength(), TextStyleManager.getDefaultStyle(), true);
    }


    /**
     * 已被取代方法 {@link #printTextStyleAfterSetText()} 取代
     */
    @Deprecated()
    private void printProcessAfterSetText() {
        if (TypingData.tipEnable && AbstractTextTip.TEXT_TIP.loaded() && !TextLoad.TEXT_LOAD.isEnglish()) {
            final ArrayList<TipPhraseStyleData> tpsd = TextLoad.TEXT_LOAD.tipData.tpsd;
            if (TextLoad.TEXT_LOAD.singleOnly() && !TypingData.charEnable && tpsd == null) {
                //如果都是单字，则不渲染
                final SimpleAttributeSet defaultStyle = TextStyleManager.getDefaultStyle();
                StyleConstants.setForeground(defaultStyle, QRColorsAndFonts.TEXT_COLOR_FORE);
                defaultStyle.addAttribute("UnderlineOpen", false);
                print(TextLoad.TEXT_LOAD.formattedActualText(), defaultStyle, 0);
            } else {
                if (Keys.boolValue(Keys.TEXT_TIP_PAINT_COLOR)) {
                    for (TipPhraseStyleData tp : tpsd) {
                        if (tp == null) {
                            continue;
                        }
                        print(tp);
                    }
                } else {
                    print(TextLoad.TEXT_LOAD.formattedActualText(), TextStyleManager.getDefaultStyle(), 0);
                }
                textPanelEditorKit.changeFontColor(tpsd);
            }
        } else {
            if (TextLoad.TEXT_LOAD.isEnglish()) {
                ContractiblePanel.STANDARD_LEN_LABEL.setText("1.0");
            }
            final SimpleAttributeSet defaultStyle = TextStyleManager.getDefaultStyle();
            StyleConstants.setForeground(defaultStyle, QRColorsAndFonts.TEXT_COLOR_FORE);
            defaultStyle.addAttribute("UnderlineOpen", false);
            print(TextLoad.TEXT_LOAD.formattedActualText(), defaultStyle, 0);
        }
    }

    private boolean getTextLoadUpdate(String text) {
        TextLoad tls = new TextLoad(text);
        if (!tls.isText()) {
            return true;
        }
        TextLoad.TEXT_LOAD = tls;
        return false;
    }

    // 累积的字符
    private final StringBuilder accumulatedChars = new StringBuilder();
    // 用于同步的锁对象
    private final Object lock = new Object();
    // 创建一个调度器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    // 用于取消未执行的延迟任务
    private Future<?> futureTask = null;

    /**
     * 统计打词的算法
     *
     * @param c 程序内部单次接受的字符
     *          （20毫秒内未再次输入则算单字，否则，算多次。拓展字情况已顺带解决。
     */
    public void insertUpdates(char c) {
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
            // 设置延迟任务在20毫秒后执行
            futureTask = scheduler.schedule(() -> {
                synchronized (lock) {
                    // 执行累积的字符操作
                    if (!accumulatedChars.isEmpty()) {
                        insertUpdateExecute(accumulatedChars.toString());
                        TyperTextPane.TYPER_TEXT_PANE.runTypedActions();
                        // 重置累积的字符
                        accumulatedChars.setLength(0);
                    }
                }
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
                WordLabel.typedOneWord();
            }
            if (!SilkyModelCheckBox.silkyCheckBox.checked() && !LookModelCheckBox.lookModelCheckBox.checked()) {
                changeTextsStyle(TypingData.currentTypedIndex, length, TextStyleManager.getCorrectStyle(), true);
            }
        } else {
            if (length == 1) {
                //否则判错，背景颜色改成红色
                if (!LookModelCheckBox.lookModelCheckBox.checked()) {
                    changeTextsStyle(TypingData.currentTypedIndex, length, TextStyleManager.getWrongStyle(), true);
                }
                TypingData.WRONG_WORDS_INDEX.add(index);
            } else {
                for (int i = 0; i < length; i++) {
                    int currentIndex = TypingData.currentTypedIndex + i;
                    String rightWord = TextLoad.TEXT_LOAD.getWordPartsAtIndex(currentIndex);
                    if (rightWord.equals(textParts[i])) {
                        if (!SilkyModelCheckBox.silkyCheckBox.checked() && !LookModelCheckBox.lookModelCheckBox.checked()) {
                            changeTextsStyle(currentIndex, rightWord.length(), TextStyleManager.getCorrectStyle(),
                                    true);
                        }
                    } else {
                        if (!LookModelCheckBox.lookModelCheckBox.checked()) {
                            changeTextsStyle(currentIndex, rightWord.length(),
                                    TextStyleManager.getWrongStyle(), true);
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
        setCaretUnblock();
        if (TextLoad.TEXT_LOAD.wordsLength() == TypingData.currentTypedIndex) {
            //0是打完即可，1是打完无错，2是无错智能，3是可错智能
            if (TypingData.finishModel == TYPING_FINISH_MARK_OVER_DO_NOTING || (TypingData.finishModel == TYPING_FINISH_MARK_OVER_CAN_WRONG_INTELLIGENCE) || TypingData.WRONG_WORDS_INDEX.isEmpty()) {
//				if (TextLoad.TEXT_LOAD.textNotIsChanged(getText())) {
                //打字结束
                if (!Info.IS_WINDOWS) {
                    MainWindow.INSTANCE.setAlwaysOnTop(false);
                }
                TypingData.typeEnd = true;
                TypingData.typing = false;
//				typeEnding();
                typingEndThread.execute(this::typeEnding);
//				endingScheduler.submit(this::typeEnding);
//				} else {
//					//Windows下可能因输入法的问题，而导致有人想在英打时打中文，进而使内容发生改变
//					QRSmallTipShow.display(MainWindow.INSTANCE, "当前文本内容被修改，将自动重打！");
//					QRSleepUtils.sleep(500);
//				}
            }
        }
    }

    public void deleteUpdates(KeyEvent e) {
        int index = this.caret.getDot();
        if (index <= 1 || TypingData.backspaceAutoRestart) {
            restart();
            return;
        }
        int length = 1;
        int preIndex = index - 1;
        setCaretBlock();
        TypingData.currentTypedIndex--;
        if (TextLoad.TEXT_LOAD.isExtra()) {
            String deleteText = TextLoad.TEXT_LOAD.getWordPartsAtIndex(TypingData.currentTypedIndex);
            length = deleteText.length();
            int offset = index - length;
//            replaceText(offset, deleteText, TextStyleManager.getDefaultStyle());
            changeTextsStyle(offset, length, TextStyleManager.getDefaultStyle(), true);
            setCaretPosition(offset);
            TypingData.WRONG_WORDS_INDEX.remove(Integer.valueOf(TypingData.currentTypedIndex));
        } else {
            TypingData.WRONG_WORDS_INDEX.remove(Integer.valueOf(preIndex));
            String deleteText;
            deleteText = String.valueOf(TextLoad.TEXT_LOAD.getTextAtIndex(preIndex));
            boolean empty = deleteText.isEmpty();
            if (empty) {
                setCaretUnblock();
                this.writeBlock = false;
                return;
            }
            if (TextTip.TEXT_TIP.loaded() && TextLoad.TEXT_LOAD.tipData != null) {
                SimpleAttributeSet attributeSet = TextLoad.TEXT_LOAD.tipData.getForeAttributeSetExact(preIndex);
                changeTextsStyle(preIndex, length, attributeSet, true);
            } else {
                changeTextsStyle(preIndex, length, TextStyleManager.getDefaultStyle(), true);
            }
//            changeTextsStyle(preIndex, length, TextStyleManager.getDefaultStyle(), true);
            setCaretPosition(preIndex);
        }
        TypingData.backDeleteCount++;
        setCaretUnblock();
        this.writeBlock = false;
        TyperTextPane.TYPER_TEXT_PANE.runTypedActions();
//		inputFollowedWindowsUpdate();
//		textTipUpdate(true);
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
        double speed = QRMathUtils.doubleFormat(s);
        if (speed < 1 || totalTimeInSec <= 0) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "继续重打吧！");
            return;
        }
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
        //将成绩存放至剪贴板
        QRSystemUtils.putTextToClipboard(grade);
        DangLangManager.DANG_LANG_MANAGER.save(TextLoad.TEXT_LOAD.textMD5Long());
        //发送成绩
        SendText.gradeSend();

        NextParaTextItem.NEXT_PARA_TEXT_ITEM.clickInvokeLater();
        TypingData.windowFresh();
    }

    /**
     * 调用后重打次数增加
     */
    public void restart() {
        if (TextLoad.TEXT_LOAD != null) {
            TextLoad.TEXT_LOAD.reTypeTimesAdd();
            simpleRestart();
        }
    }

    /**
     * 该方法不增加重打次数
     */
    public void simpleRestart() {
        if (TextLoad.TEXT_LOAD != null) {
            TypingData.typing = false;
            TypingData.typeEnd = true;
//            printTextStyleAfterSetText();
            TEXT_PANE.setTypeText(TextLoad.TEXT_LOAD.currentText());
            DangLangManager.DANG_LANG_MANAGER.save(TextLoad.TEXT_LOAD.textMD5Long());
            TypingData.windowFresh();
            MainWindow.INSTANCE.grabFocus();
        }
    }

    public void caretPositionAdjust() {
        if (!this.caretBlock) {
            int caretPosition = getCaretPosition();
            if (TypingData.currentTypedIndex != caretPosition) {
                setCaretPosition(TypingData.currentTypedIndex);
            }
        }
    }


    public void print(TipPhraseStyleData tpsd) {
        try {
            getDocument().insertString(tpsd.index(), tpsd.phrase(), tpsd.getStyle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print(TipPhraseStyleData tpsd, int index) {
        try {
            getDocument().insertString(index, tpsd.phrase(), tpsd.getStyle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print(TipCharStyleData tcsd) {
        try {
            getDocument().insertString(tcsd.index(), tcsd.word(), tcsd.getStyle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSetTextBeforeAction(QRActionRegister ar) {
        this.setTextBeforeActions.add(ar);
    }

    public void addSetTextFinishedAction(QRActionRegister ar) {
        this.setTextFinishedActions.add(ar);
    }

    @Override
    protected void cutAction() {
        copyAction();
    }

    @Override
    protected void pasteAction() {
        TyperTextPane.TYPER_TEXT_PANE.paste();
    }

    @Override
    protected void keyPress(KeyEvent e) {
        if (!TypingData.typing || TypingData.typeEnd) {
            e.consume();
        }
    }

    @Override
    protected void keyType(KeyEvent e) {
        e.consume();
    }

    @Override
    protected void mousePress(MouseEvent e) {
        caretPositionAdjust();
        MainWindow.INSTANCE.grabFocus();
    }

    private boolean paintLock = false;

    @Override
    public void paint(Graphics g) {
        try {
            if (paintLock) {
                return;
            }
            paintLock = true;
            super.paint(g);
        } catch (Exception e) {
            e.printStackTrace();
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
    public QRScrollPane addScrollPane() {
        return super.addScrollPane(1);
    }

    @Override
    public void componentFresh() {
        this.textFont = QRFontUtils.getFontInSize(Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
        super.componentFresh();
    }
}