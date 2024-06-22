package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import ly.qr.kiarelemb.text.tip.data.TipCharStyleData;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.component.basic.QRTextPane;

import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Kiarelemb QR
 * @date 2021/11/14 14:23
 * @apiNote
 */
public class TextPanelEditorKit extends StyledEditorKit {

    private final QRTextPane textPane;

    public TextPanelEditorKit(QRTextPane textPane) {
        this.textPane = textPane;
    }

    public static class CustomUI extends BasicTextPaneUI {

        @Override
        public View create(Element elem) {
            View result;
            String kind = elem.getName();
            if (kind != null) {
                result = switch (kind) {
                    case AbstractDocument.ContentElementName -> new MyLabelView(elem);
                    case AbstractDocument.ParagraphElementName -> new ParagraphView(elem);
                    case AbstractDocument.SectionElementName -> new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName -> new ComponentView(elem);
                    case StyleConstants.IconElementName -> new IconView(elem);
                    default -> new LabelView(elem);
                };
            } else {
                result = super.create(elem);
            }
            return result;
        }
    }

    public static class MyLabelView extends LabelView {

        public MyLabelView(Element arg0) {
            super(arg0);
        }

        @Override
        public void paint(Graphics g, Shape a) {
            super.paint(g, a);
            final AttributeSet attributes = getElement().getAttributes();
            if (attributes == null) {
                return;
            }
            Color tipForeground = (Color) attributes.getAttribute("tip-color");
            Boolean underlineOpen = (Boolean) attributes.getAttribute("UnderlineOpen");
            if (tipForeground == null || underlineOpen == null || !underlineOpen) {
                return;
            }
            final int fontSize = TypingData.lookfontSize;
            Color c = (Color) attributes.getAttribute("Underline-Color");
            String s = (String) attributes.getAttribute("Number");
            Color foreground = (Color) attributes.getAttribute("foreground");
            if (TextLoad.TEXT_LOAD != null && (!TextLoad.TEXT_LOAD.singleOnly() || TypingData.charEnable)) {
                if (s != null) {
                    final int spaceAdd = (int) ((TypingData.lineSpacing - 0.4) * 10);
                    Rectangle r = a.getBounds();
                    int y = r.y + (int) getGlyphPainter().getAscent(this) + fontSize / 4 + spaceAdd;
                    int x1 = r.x + 4;
                    int x2 = r.x + r.width - 4;
                    if (c == null) {
                        g.setColor(foreground);
                    } else {
                        g.setColor(c);
                    }
                    g.drawLine(x1, y, x2, y);
                    int y2 = r.y + (int) getGlyphPainter().getAscent(this) + fontSize * 3 / 4 - 2 + spaceAdd * 2;
                    g.setColor(tipForeground);
                    Font font = TextStyleManager.getTipStyleFont();
                    g.setFont(font);
                    int textInWidth = QRFontUtils.getTextInWidth(TextPane.TEXT_PANE, font, s);
                    final int x = r.x + (r.width - textInWidth) / 2;
                    g.drawString(s, x, y2);
                }
            }
        }

        @Override
        public float getMinimumSpan(int axis) {
            return switch (axis) {
                case View.X_AXIS -> 0;
                case View.Y_AXIS -> super.getMinimumSpan(axis);
                default -> throw new IllegalArgumentException("Invalid axis: " + axis);
            };
        }
    }

    public void changeFontColor() {
//		StyledDocument doc = (StyledDocument) textPane.getDocument();
        ArrayList<TipPhraseStyleData> tpsd = TextLoad.TEXT_LOAD.tipData.tpsd;
        // 添加剩下字体
        for (final TipPhraseStyleData data : tpsd) {
            if (data == null) {
                continue;
            }
            final SimpleAttributeSet style = data.getStyle();
            if (TypingData.paintCode) {
                style.addAttribute("UnderlineOpen", true);
                style.addAttribute("Underline-Color", Color.red);
                style.addAttribute("Number", data.code());
            } else if (TypingData.paintSelection) {
                // 首选是奇数，其他是偶数
                int type = data.type();
                boolean condition = (TypingData.charEnable || data.shortPhrase()) && type % 2 == 0;
                if (condition) {
                    style.addAttribute("UnderlineOpen", true);
                    style.addAttribute("Underline-Color", Color.red);
                    style.addAttribute("Number", data.lastChar());
                }
            }
            textPane.changeTextsStyle(data.index(), data.phrase().length(), style, true);
            if (data.shortPhrase()) {
                int length = data.phrase().length();
                for (int i = 1; i < length; i++) {
                    ArrayList<TipCharStyleData> tcsd = TextLoad.TEXT_LOAD.tipData.tcsd;
                    TipCharStyleData thisData = tcsd.get(data.index() + i);
                    SimpleAttributeSet singleStyle = thisData.getStyle();
                    if (TypingData.paintCode) {
                        singleStyle.addAttribute("UnderlineOpen", true);
                        singleStyle.addAttribute("Underline-Color", Color.red);
                        singleStyle.addAttribute("Number", thisData.code());
                    } else if (TypingData.paintSelection) {
                        int type = thisData.type();
                        if (!TypingData.charEnable || type % 2 == 1) {
                            continue;
                        }
                        singleStyle.addAttribute("UnderlineOpen", true);
                        singleStyle.addAttribute("Underline-Color", Color.red);
                        singleStyle.addAttribute("Number", thisData.lastChar());
                    }
                }
            }
        }
    }

    @Override
    public ViewFactory getViewFactory() {
        return new CustomUI();
    }
}