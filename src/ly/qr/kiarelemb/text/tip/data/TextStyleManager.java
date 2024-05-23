package ly.qr.kiarelemb.text.tip.data;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kiarelemb QR
 * @date 2021/8/26 上午10:19
 * @apiNote 根据type，取得这个字或词的字体样式
 */
public class TextStyleManager {

    static {
        TextPane.TEXT_PANE.addSetTextBeforeAction(e -> updateAll());
        try {
            String oneStr = Keys.strValue(Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_ONE).trim();
            String twoStr = Keys.strValue(Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_TWO).trim();
            String threeStr = Keys.strValue(Keys.TEXT_TIP_COLOR_SIMPLIFIED_CODE_THREE).trim();
            String allStr = Keys.strValue(Keys.TEXT_TIP_COLOR_CODE_ALL).trim();
            int[] oneArr = QRStringUtils.splitWithNotNumber(oneStr);
            int[] twoArr = QRStringUtils.splitWithNotNumber(twoStr);
            int[] threeArr = QRStringUtils.splitWithNotNumber(threeStr);
            int[] allArr = QRStringUtils.splitWithNotNumber(allStr);
            SIMPLIFIED_ONE = new Color(oneArr[0], oneArr[1], oneArr[2]);
            SIMPLIFIED_TWO = new Color(twoArr[0], twoArr[1], twoArr[2]);
            SIMPLIFIED_THREE = new Color(threeArr[0], threeArr[1], threeArr[2]);
            Full_FOUR = new Color(allArr[0], allArr[1], allArr[2]);
        } catch (Exception e) {
            SIMPLIFIED_ONE = new Color(255, 255, 0);
            SIMPLIFIED_TWO = new Color(255, 51, 102);
            SIMPLIFIED_THREE = new Color(0, 220, 220);
            Full_FOUR = new Color(0, 153, 255);
            QRSwing.setGlobalSetting("simplified.code.one", "255,255,0");
            QRSwing.setGlobalSetting("simplified.code.two", "255,51,102");
            QRSwing.setGlobalSetting("simplified.code.three", "0,220,220");
            QRSwing.setGlobalSetting("code.all", "0,153,255");
        }
        STYLES = new ArrayList<>(14);
        String set = Keys.strValue(Keys.TEXT_FONT_NAME_LOOK);
        PREFERRED_CHINESE_FONT_NAME = set == null ? "黑体" : set;
        updateAll();
    }

    /**
     * 一简字前景色
     */
    public static Color SIMPLIFIED_ONE;
    /**
     * 二简字前景色
     */
    public static Color SIMPLIFIED_TWO;
    /**
     * 三简字前景色
     */
    public static Color SIMPLIFIED_THREE;
    /**
     * 全码字颜色
     */
    public static Color Full_FOUR;

    public static final int[] STYLE_TYPE = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    public static String PREFERRED_CHINESE_FONT_NAME;
    public static final String PREFERRED_ENGLISH_FONT_NAME = "Consolas";
    public static final boolean BOLD_FALSE = false;
    public static final Map<String, SimpleAttributeSet> EXTRA_STYLE = new HashMap<>();
    public static final Font DEFAULT_FONT = QRFontUtils.loadFontFromURL(Info.loadURL(Info.ALIBABA_FONT_NAME));
    private static final ArrayList<SimpleAttributeSet> STYLES;
    private static SimpleAttributeSet correctStyle;
    private static SimpleAttributeSet WrongStyle;
    private static SimpleAttributeSet defaultStyle;
    private static Font tipStyle;

    //region public getters
    public static SimpleAttributeSet getCorrectStyle() {
        return getCorrectStyle(Keys.strValue(Keys.TEXT_FONT_NAME_LOOK), false);
    }

    public static SimpleAttributeSet getCorrectStyle(String fontName, boolean english) {
        if (correctStyle == null) {
            correctStyle = new SimpleAttributeSet();
            StyleConstants.setFontFamily(correctStyle, fontName);
            StyleConstants.setFontSize(correctStyle, Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
            StyleConstants.setBackground(correctStyle, english ? QRColorsAndFonts.TEXT_COLOR_BACK : QRColorsAndFonts.CORRECT_COLOR_BACK);
            StyleConstants.setForeground(correctStyle, QRColorsAndFonts.CORRECT_COLOR_FORE);
        }
        return correctStyle;
    }

    public static SimpleAttributeSet getDefaultStyle() {
        return getDefaultStyle(Keys.strValue(Keys.TEXT_FONT_NAME_LOOK));
    }

    //endregion

    public static SimpleAttributeSet getDefaultStyle(String fontName) {
        if (defaultStyle == null) {
            defaultStyle = new SimpleAttributeSet();
            StyleConstants.setFontFamily(defaultStyle, fontName);
            StyleConstants.setFontSize(defaultStyle, Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
            StyleConstants.setBackground(defaultStyle, QRColorsAndFonts.TEXT_COLOR_BACK);
        }
        return defaultStyle;
    }

    public static SimpleAttributeSet getWrongStyle() {
        return getWrongStyle(Keys.strValue(Keys.TEXT_FONT_NAME_LOOK));
    }

    public static SimpleAttributeSet getWrongStyle(String fontName) {
        if (WrongStyle == null) {
            WrongStyle = new SimpleAttributeSet();
            StyleConstants.setFontFamily(WrongStyle, fontName);
            StyleConstants.setFontSize(WrongStyle, Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
            StyleConstants.setBackground(WrongStyle, Color.RED);
            StyleConstants.setForeground(WrongStyle, QRColorsAndFonts.TEXT_COLOR_FORE);
        }
        return WrongStyle;
    }

    public static Font getTipStyleFont() {
        if (tipStyle == null) {
            tipStyle = QRFontUtils.getFont(Keys.strValue(Keys.TEXT_FONT_NAME_LOOK), Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK) / 2);
        }
        return tipStyle;
    }

    public static SimpleAttributeSet createFontNameStyleFromCurrentStyle(String fontName, SimpleAttributeSet s) {
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, fontName);
        StyleConstants.setFontSize(sas, Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
        StyleConstants.setBackground(sas, QRColorsAndFonts.TEXT_COLOR_BACK);
        attributeCopy(s, sas, StyleConstants.Foreground);
        attributeCopy(s, sas, StyleConstants.Bold);
        attributeCopy(s, sas, "tip-color");
        return sas;
    }

    private static void attributeCopy(SimpleAttributeSet from, SimpleAttributeSet to, Object attributeName) {
        to.addAttribute(attributeName, from.getAttribute(attributeName));
    }

    private static SimpleAttributeSet getStyle(int type, boolean bold, String fontName) {
        SimpleAttributeSet sas = new SimpleAttributeSet();
        StyleConstants.setFontFamily(sas, fontName);
        StyleConstants.setFontSize(sas, Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
        StyleConstants.setBackground(sas, QRColorsAndFonts.TEXT_COLOR_BACK);
        StyleConstants.setBold(sas, bold);
        switch (type) {
            case 2, 1 -> {
                //全码首选，前景色为 灰色
                StyleConstants.setForeground(sas, Full_FOUR);
                sas.addAttribute("tip-color", Full_FOUR);
            }
            case 4, 3 -> {
                //三简，前景色为绿色
                StyleConstants.setForeground(sas, SIMPLIFIED_THREE);
                sas.addAttribute("tip-color", SIMPLIFIED_THREE);
            }
            case 6, 5 -> {
                //二简，前景色为红色
                StyleConstants.setForeground(sas, SIMPLIFIED_TWO);
                sas.addAttribute("tip-color", SIMPLIFIED_TWO);
            }
            case 7, 8 -> {
                //一简，前景色为橙色
                StyleConstants.setForeground(sas, SIMPLIFIED_ONE);
                sas.addAttribute("tip-color", SIMPLIFIED_ONE);
            }
            default -> {
                StyleConstants.setForeground(sas, QRColorsAndFonts.TEXT_COLOR_FORE);
                sas.addAttribute("tip-color", QRColorsAndFonts.TEXT_COLOR_FORE);
            }
        }
        return sas;
    }

    private static void freshToEnglishModelStyle() {
        STYLES.clear();
        final int maxSize = 16;
        for (int i = 0; i < maxSize; i++) {
            final boolean bold = i > 6;
            STYLES.add(getStyle(bold ? i - 7 : i, bold, PREFERRED_ENGLISH_FONT_NAME));
        }
        correctStyle = null;
        WrongStyle = null;
        defaultStyle = null;
        tipStyle = null;
        getCorrectStyle(PREFERRED_ENGLISH_FONT_NAME, true);
        getWrongStyle(PREFERRED_ENGLISH_FONT_NAME);
        getDefaultStyle(PREFERRED_ENGLISH_FONT_NAME);
        getTipStyleFont();
    }

    private static void freshToChineseModelStyle() {
        STYLES.clear();
        final int maxSize = 16;
        for (int i = 0; i < maxSize; i++) {
            final boolean bold = i > 8;
            STYLES.add(getStyle(bold ? i - 8 : i, bold, PREFERRED_CHINESE_FONT_NAME));
        }
        correctStyle = null;
        WrongStyle = null;
        defaultStyle = null;
        tipStyle = null;
        getCorrectStyle(PREFERRED_CHINESE_FONT_NAME, false);
        getWrongStyle(PREFERRED_CHINESE_FONT_NAME);
        getDefaultStyle(PREFERRED_CHINESE_FONT_NAME);
        getTipStyleFont();
    }

    public static SimpleAttributeSet getDefinedStyle(int type, boolean bold) {
        return STYLES.get(type + (bold ? 7 : 0));
    }


    public static SimpleAttributeSet getDefinedStyle(int type, boolean bold, String str, boolean shortPhrase) {
        final SimpleAttributeSet a = STYLES.get((shortPhrase ? type : 0) + (bold ? 7 : 0));
        String fontFamily = StyleConstants.getFontFamily(a);
        Font font = QRFontUtils.getFont(fontFamily, 10);
        if (!QRFontUtils.fontCanAllDisplay(str, font)) {
//            System.out.println("Can't display....");
            //有拓展字
            final String[] chineseExtraPhrase = QRStringUtils.getChineseExtraPhrase(str);
            for (String aChineseWord : chineseExtraPhrase) {
                if (!QRFontUtils.fontCanAllDisplay(aChineseWord, font)) {
                    final Font canDisplayFont = QRFontUtils.getCanDisplayFont(aChineseWord);
                    if (canDisplayFont != null) {
                        final SimpleAttributeSet simpleAttributeSet = EXTRA_STYLE.get(canDisplayFont.getFamily());
                        if (simpleAttributeSet != null) {
                            //如果这个字体样式已经建立了
                            return simpleAttributeSet;
                        } else {
                            final SimpleAttributeSet styleFromFontName = TextStyleManager.createFontNameStyleFromCurrentStyle(canDisplayFont.getFamily(), a);
                            //放入这个字体样式
                            EXTRA_STYLE.put(canDisplayFont.getFamily(), styleFromFontName);
                            return styleFromFontName;
                        }
                    }
                }
            }
        }
        return a;
    }

    public static void updateAll() {
        final boolean isEnglishTyping = TextLoad.TEXT_LOAD != null && TextLoad.TEXT_LOAD.isEnglish();
        if (isEnglishTyping) {
            freshToEnglishModelStyle();
            TextPane.TEXT_PANE.caret.setFontName(PREFERRED_ENGLISH_FONT_NAME).update();
            Font font = QRFontUtils.getFont(Keys.strValue(Keys.TEXT_FONT_NAME_LOOK), Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
            TyperTextPane.TYPER_TEXT_PANE.setFont(font);
        } else {
            freshToChineseModelStyle();
            TextPane.TEXT_PANE.caret.setFontName(PREFERRED_CHINESE_FONT_NAME).update();
//			Font font = QRFontUtils.getFont(Keys.strValue(Keys.TEXT_FONT_NAME_LOOK, PREFFERED_CHINESE_FONT_NAME), Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK, 28));
//			TyperTextPane.TYPER_TEXT_PANE.setFont(font);
        }
    }
}