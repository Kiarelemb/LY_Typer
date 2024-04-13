package ly.qr.kiarelemb.component;

import method.qr.kiarelemb.utils.QRMathUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.QRComponentUtils;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.utils.QRKeyBoardPanel;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className KeyBoardPanel
 * @description TODO
 * @create 2024/4/13 17:18
 */
public class KeyBoardPanel extends QRKeyBoardPanel {
    private final Map<Character, Float> dataMap;
    private final Map<Character, Integer> map;
    private static final Font FONT = QRSwing.globalFont.deriveFont(15f);
    /**
     * 需要扩大的倍数，以对于高占比的数据能实现爆红
     */
    private final int factor;
    public static final char[] KEY_CHARS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '←', 'Q', 'W'
            , 'E', 'R'
            , 'Y', 'T', 'U', 'I', 'O', 'P', '[', ']', '\\', 'A', 'S', 'D', 'F', 'H', 'G', 'J', 'K', 'L', ';', '\'',
            '↵', '↑', 'Z', 'X', 'C', 'B', 'V', 'N', 'M', ',', '.', '/', '↑', 'ᐃ', '_'};

    public KeyBoardPanel(Map<Character, Integer> map) {
        int mapSize = map.size();
        dataMap = new HashMap<>(mapSize);
        this.map = map;
        if (mapSize == 0) {
            this.factor = 1;
            return;
        }
        for (QRLabel label : labelList) {
            label.clear();
        }
        List<Float> list = new ArrayList<>(mapSize);
        float totalKeyCount = map.values().stream().mapToInt(Integer::intValue).sum();
        map.forEach((c, i) -> {
            float count = QRMathUtils.floatFormat(i / totalKeyCount, 5);
            list.add(count);
            dataMap.put(c, count);
        });
        list.sort(Comparator.reverseOrder());


        int factor = 1;
        int max = 1;
        float maxValue = list.get(0);
        if (maxValue == 0) {
            this.factor = 1;
            return;
        }
        float tmp = maxValue;
        while (tmp < max) {
            tmp = maxValue * ++factor;
        }
        this.factor = --factor;
        setSize(0, 0);
        final int factorTmp = this.factor;
        labelList.forEach(label -> {
            Float v = dataMap.get(labelChar(label));
            if (v == null) {
                label.setOpaque(false);
                return;
            }
            float a = v * factorTmp;
            label.setBackground(new Color(1, 0, 0, a));
        });
    }

    private char labelChar(QRLabel label) {
        int index = labelIndex(label);
        return KEY_CHARS[index];
    }

    /**
     * 在这里自定义设计标签的属性，如字体设置、是否透明等
     *
     * @param label 当前标签
     */
    @Override
    protected void labelComponentFresh(QRLabel label) {
        label.setOpaque(true);
    }

    /**
     * 在这里自定义标签其他内容的绘制，如添加其他字符等
     *
     * @param label 当前标签
     * @param g     绘制工具
     */
    @Override
    protected void labelPaint(QRLabel label, Graphics g) {

        QRComponentUtils.componentStringDraw(label, g, text[labelIndex(label)], FONT, QRColorsAndFonts.MENU_COLOR, 30);


        char c = labelChar(label);

        Float v = dataMap.get(c);
        if (v == null) {
            return;
        }
        Integer i = map.get(c);

        String count = QRMathUtils.floatFormat(v * 100, 2) + "%/" + i;
        QRComponentUtils.componentStringDraw(label, g, count, FONT, QRColorsAndFonts.MENU_COLOR, 55);

    }
}