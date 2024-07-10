package ly.qr.kiarelemb.text.tip;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.TextPanelEditorKit;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import ly.qr.kiarelemb.text.tip.data.TipStyleData;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.basic.QRLabel;
import swing.qr.kiarelemb.basic.QRPanel;
import swing.qr.kiarelemb.basic.QRTextPane;
import swing.qr.kiarelemb.window.basic.QRDialog;

import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class StandardTipWindow extends QRDialog {
    private final QRTextPane centerTextPane;
    private final TextPanelEditorKit textPanelEditorKit;

    public StandardTipWindow() {
        super(MainWindow.INSTANCE);
        setTitle("标顶打法");
        setTitlePlace(QRDialog.CENTER);
        setSize(700, 500);
        setResizable(true);
        setParentWindowNotFollowMove();
        this.mainPanel.setLayout(new BorderLayout(5, 0));
        this.mainPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        addWindowListener();
        //顶部面板

        QRPanel topPanel = new QRPanel();
        topPanel.setLayout(new BorderLayout());

        String methodName = Keys.strValue(Keys.TYPE_METHOD_INPUT);
        QRLabel method = new QRLabel("方案：" + (methodName == null || methodName.isBlank() ? "未定义" : methodName));
        topPanel.add(method, BorderLayout.WEST);

        this.mainPanel.add(topPanel, BorderLayout.NORTH);
        //中间面板

        centerTextPane = new QRTextPane() {

            @Override
            public void setParagraphAttributes(AttributeSet attr, boolean replace) {
                StyledDocument doc = getStyledDocument();
                doc.setParagraphAttributes(0, getText().length(), attr, replace);
            }

            @Override
            public void componentFresh() {
                this.textFont = QRFontUtils.getFontInSize(Keys.intValue(Keys.TEXT_FONT_SIZE_LOOK));
                super.componentFresh();
                setLineSpacing(Keys.floatValue(Keys.TEXT_LINE_SPACE));

            }
        };
        textPanelEditorKit = new TextPanelEditorKit(centerTextPane);
        centerTextPane.setEditorKit(textPanelEditorKit);
        centerTextPane.setEditableFalseButCursorEdit();
        this.mainPanel.add(centerTextPane.addScrollPane(1), BorderLayout.CENTER);

        //底部面板

    }

    @Override
    public void windowOpened(WindowEvent e) {
        // 备份数据
        boolean paintCode = TypingData.paintCode;
        boolean charEnable = TypingData.charEnable;
        centerTextPane.print(TextLoad.TEXT_LOAD.formattedActualText(), TextStyleManager.getDefaultStyle(), 0);
        TypingData.paintCode = true;
        TypingData.charEnable = true;
        textPanelEditorKit.changeFontColor();
        centerTextPane.setLineSpacing(Keys.floatValue(Keys.TEXT_LINE_SPACE));
        TypingData.paintCode = paintCode;
        TypingData.charEnable = charEnable;
    }

    @Override
    public void dispose() {
        super.dispose();
        // 清除样式
        TextLoad.TEXT_LOAD.tipData.tpsd.stream().filter(Objects::nonNull).forEach(TipStyleData::clearStyle);
        TextLoad.TEXT_LOAD.tipData.tcsd.stream().filter(Objects::nonNull).forEach(TipStyleData::clearStyle);
    }
}