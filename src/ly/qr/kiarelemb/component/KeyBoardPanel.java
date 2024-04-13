package ly.qr.kiarelemb.component;

import method.qr.kiarelemb.utils.QRFontUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRLabel;
import swing.qr.kiarelemb.component.utils.QRKeyBoardPanel;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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
    /**
     * 需要扩大的倍数，以对于高占比的数据能实现爆红
     */
    private final int factor;

    public KeyBoardPanel(Map<Character, Integer> map) {
        int mapSize = map.size();
        dataMap = new HashMap<>(mapSize);
        for (QRLabel label : labelList) {
            label.clear();
        }
        List<Float> list = new ArrayList<>(mapSize);
        float totalKeyCount = map.values().stream().mapToInt(Integer::intValue).sum();
        map.forEach((c, i) -> {
            float count = QRMathUtils.floatFormat(QRMathUtils.floatCount(i, totalKeyCount), 5);
            list.add(count);
            dataMap.put(c, count);
        });
        list.sort(Comparator.reverseOrder());

        int factor = 1;
        int max = 1;
        float maxValue = list.get(0);
        float tmp = maxValue;
        while (tmp < max) {
            tmp = maxValue * ++factor;
        }
        this.factor = --factor;
    }

    /**
     * 在这里自定义设计标签的属性，如字体设置、是否透明等
     *
     * @param label 当前标签
     */
    @Override
    protected void labelComponentFresh(QRLabel label) {
        label.setOpaque(true);
        label.setBackground(Color.red);
    }

    /**
     * 在这里自定义标签其他内容的绘制，如添加其他字符等
     *
     * @param label 当前标签
     * @param g     绘制工具
     */
    @Override
    protected void labelPaint(QRLabel label, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        String title = text[labelIndex(label)];
        Rectangle2D r = QRFontUtils.getStringBounds(title, QRSwing.globalFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                this.getClientProperty(RenderingHints.KEY_TEXT_ANTIALIASING));
        g2.setFont(QRSwing.globalFont);
        g2.setColor(QRColorsAndFonts.MENU_COLOR);
        float x = (float) (label.getWidth() / 2 - r.getWidth() / 2);
        g2.drawString(title, x, 30);
    }
}