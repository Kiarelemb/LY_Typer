package ly.qr.kiarelemb.component;

import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRSleepUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.event.QRTabSelectEvent;
import swing.qr.kiarelemb.theme.QRColorsAndFonts;
import swing.qr.kiarelemb.utils.QRComponentUtils;
import swing.qr.kiarelemb.utils.QRKeyBoardPanel;

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
    private static final Font FONT = QRSwing.globalFont.deriveFont(18f);
    private static final Font DATA_Font = QRSwing.globalFont.deriveFont(15f);
    public static final char[] KEY_CHARS = {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '←', '\t',
            'Q', 'W', 'E', 'R', 'Y', 'T', 'U', 'I', 'O', 'P', '[', ']', '\\', '⇧', 'A', 'S', 'D', 'F', 'H', 'G', 'J',
            'K', 'L', ';', '\'', '↵', '↑', 'Z', 'X', 'C', 'B', 'V', 'N', 'M', ',', '.', '/', '↑', 'ᐃ', '_'};
    private final ArrayList<Runnable> runs = new ArrayList<>();

    public KeyBoardPanel(Map<Character, Integer> map) {
        int mapSize = map.size();
        this.dataMap = new HashMap<>(mapSize);
        this.map = map;
        for (QRLabel label : labelList) {
            label.clear();
            label.setOpaque(false);
        }
        if (mapSize == 0) {
            return;
        }
        // 去除特殊字符
        map.remove('⊗');
        List<Float> list = new ArrayList<>(mapSize);
        float totalKeyCount = map.values().stream().mapToInt(Integer::intValue).sum();
        map.forEach((c, i) -> {
            float count = QRMathUtils.floatFormat(i / totalKeyCount, 5);
            list.add(count);
            dataMap.put(c, count);
        });
        // 降序排序
        list.sort(Comparator.reverseOrder());

        // factor: 需要扩大的倍数，以对于高占比的数据能实现爆红
        final int max = 1;
        float maxValue = list.get(0);
        if (maxValue == 0) {
            return;
        }
        float tmp = maxValue;
        int factorTmp = 1;
        while (tmp < max) {
            tmp = maxValue * ++factorTmp;
        }
        factorTmp--;
        int normalFactorTmp = 1;
        int normalFactor;
        float nextValue = list.get(1);
        boolean diffObvious = list.get(0) > 2 * nextValue;
        // 当最高占比的数据和第二高占比的数据相差明显时，设置第二高占比的倍数，以显著提高其他占比较低的红色显示
        if (diffObvious) {
            float normalTmp = nextValue;
            while (normalTmp < max) {
                normalTmp = nextValue * ++normalFactorTmp;
            }
            normalFactor = --normalFactorTmp;
        } else {
            normalFactor = factorTmp;
        }
        final int factor = factorTmp;
        float maxFloat = Math.max(maxValue * factor, nextValue * normalFactor);
        labelList.forEach(label -> {
            Float v = dataMap.get(labelChar(label));
            if (v == null) {
                label.setOpaque(false);
                return;
            }
            float a;

            if (v == maxValue || v == nextValue) {
                //使得第一和第二的一样红
                if (diffObvious) {
                    a = maxFloat;
                } else if (v == maxValue) {
                    a = v * factor;
                } else {
                    a = v * normalFactor;
                }
            } else {
                a = v * normalFactor;
            }
            Runnable run = () -> {
                label.setOpaque(true);
                for (float i = 0.005f; i < a; i += 0.005f) {
                    label.setBackground(new Color(1, 0, 0, i));
                    repaint();
                    QRSleepUtils.sleep(7);
                }
                label.setBackground(new Color(1, 0, 0, a));
            };
            runs.add(run);
        });

        QRLabel totalCountLabel = new QRLabel("总键数：" + (int) totalKeyCount);
        QRComponentUtils.setBoundsAndAddToComponent(this, totalCountLabel, 20, 470, 200, 30);
        // 设置窗体大小
        setSize(0, 0);
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
        char c = labelChar(label);
        Float v = dataMap.get(c);
        String str = text[labelIndex(label)];
        if (v == null) {
            QRComponentUtils.componentStringDraw(label, g, str, FONT, QRColorsAndFonts.MENU_COLOR, 45);
            return;
        }
        QRComponentUtils.componentStringDraw(label, g, str, FONT, QRColorsAndFonts.MENU_COLOR, 25);
        String count = QRMathUtils.floatFormat(v * 100, 2) + "%";
        QRComponentUtils.componentStringDraw(label, g, count, DATA_Font, QRColorsAndFonts.MENU_COLOR, 50);
        String num = String.valueOf(map.get(c));
        QRComponentUtils.componentStringDraw(label, g, num, DATA_Font, QRColorsAndFonts.MENU_COLOR, 70);
    }

    public static class TabContentPanel extends QRTabbedContentPanel {
        private final KeyBoardPanel keyBoardPanel;
        private final KeyBoardPanel otherKeyBoardPanel;

        public TabContentPanel(KeyBoardPanel keyBoardPanel, KeyBoardPanel otherKeyBoardPanel) {
            this.keyBoardPanel = keyBoardPanel;
            this.otherKeyBoardPanel = otherKeyBoardPanel;
            setLayout(new BorderLayout());
            add(keyBoardPanel);
        }

        @Override
        protected void thisTabSelectChangeAction(QRTabSelectEvent event) {
            QRComponentUtils.runLater(100, e -> {
                // 清除另一个键盘的所有键背景色
                otherKeyBoardPanel.labelList.forEach(label -> label.setBackground(new Color(1, 0, 0, 0)));
                ArrayList<Runnable> runs = new ArrayList<>(keyBoardPanel.runs);
                // 为线程乱序
                Object[] objects = QRArrayUtils.getRandomObject(runs);
                for (Object run : objects) {
                    Thread t = new Thread((Runnable) run);
                    t.start();
                    // 为每个键的显示时间添加间隔
                    QRSleepUtils.sleep(15);
                }
            });
        }
    }
}