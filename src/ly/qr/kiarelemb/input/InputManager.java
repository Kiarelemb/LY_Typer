package ly.qr.kiarelemb.input;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.TyperTextPane;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRArrayUtils;
import method.qr.kiarelemb.utils.QRBloomFilter;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.QRSwing;
import swing.qr.kiarelemb.component.basic.QRTextField;
import swing.qr.kiarelemb.window.basic.QREmptyDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className InputManager
 * @description TODO
 * @create 2024/4/5 19:06
 */
public class InputManager {
    public static final InputManager INPUT_MANAGER = new InputManager();

    TreeMap<String, ArrayList<String>> dictMap = new TreeMap<>();

    private InputManager() {
        QRSwing.setGlobalSetting(Keys.INPUT_CODE_DICT_PATH, "C:\\Users\\Kiarelemb QR\\Desktop\\B易.txt");
        InputWindow inputWindow = new InputWindow();
        inputWindow.setVisible(true);
        TyperTextPane.TYPER_TEXT_PANE.addTypeActions(e -> {
            TyperTextPane.TYPER_TEXT_PANE.addTypeActions(event -> inputWindow.updateLocation());
        });
    }

    public void init() {
        String filePath = Keys.strValue(Keys.INPUT_CODE_DICT_PATH);
        if (!QRFileUtils.fileExists(filePath)) {
            return;
        }
        String fileCRC;
        try {
            fileCRC = QRFileUtils.getCrc32(filePath);
        } catch (IOException e) {
            fileCRC = QRFileUtils.getFileName(filePath);
        }
        String tmp = Info.DICT_DIRECTORY + fileCRC + ".dct";
        QRFileUtils.dirCreate(Info.DICT_DIRECTORY);
        if (!QRFileUtils.fileExists(tmp)) {
            QRBloomFilter<String> bloomFilter = new QRBloomFilter<>(12, 1000000, 8);
            final char splitChar = '\t';
            final Charset code = StandardCharsets.UTF_8;
            ArrayList<TipFileLineData> lineData = new ArrayList<>();
            try {
                final FileReader reader = new FileReader(filePath, code);
                final FileWriter writer = new FileWriter(tmp, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(reader);
                BufferedWriter out = new BufferedWriter(writer);
                String lineText;
                boolean fileInfo = false;
                while ((lineText = in.readLine()) != null) {
                    //region 前期格式处理
                    lineText = lineText.trim();
                    if ("...".equals(lineText)) {
                        fileInfo = false;
                        continue;
                    }
                    if (fileInfo || lineText.startsWith("#") || lineText.startsWith("$") || lineText.startsWith("--config") || lineText.endsWith("#删")) {
                        continue;
                    }
                    if ("---".equals(lineText)) {
                        fileInfo = true;
                        continue;
                    }
                    //endregion
//                final String[] split = lineText.split(splitStr);
                    final String[] split = QRStringUtils.stringSplit(lineText, splitChar);
                    if (split.length < 2) {
                        continue;
                    }
                    boolean startWithChinese = (QRStringUtils.isEnglish(split[1]) && split[1].length() < 6) || QRStringUtils.isWholeSingleChinese(split[0]);
                    TipFileLineData data;
                    //编码开头
                    if (!startWithChinese) {
                        data = new TipFileLineData(split[1], split[0]);
                    } else {
                        data = new TipFileLineData(split[0], split[1]);
                    }
                    final String s = data.toString();
                    bloomFilter.add(s);
                    lineData.add(data);
                }
                int times = 0;
                for (TipFileLineData child : lineData) {
                    out.write(child.toString() + QRStringUtils.AN_ENTER);
                    if (++times % 100 == 0) {
                        out.flush();
                        times = 0;
                    }
                }
                if (Keys.strValue(Keys.TEXT_TIP_SELECTION).contains("ex")) {
                    if (!bloomFilter.contains("，\ti")) {
                        out.write("，\ti\n");
                        out.write("。\t'\n");
                        out.flush();
                    }
                    if (!bloomFilter.contains("0\t0")) {
                        for (int i = 0; i < 10; i++) {
                            out.write(i + "\t" + i + "\n");
                        }
                        out.flush();
                    }
                }
                out.flush();
                out.close();
                in.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            System.out.println("已完成码表文件识别——");
        }
        try {
            String crc32 = QRFileUtils.getCrc32(tmp);
            String binPath = Info.DICT_DIRECTORY + crc32 + ".bin";
            if (!QRFileUtils.fileExists(binPath)) {
                QRFileUtils.fileReaderWithUtf8(tmp, "\t", (line, split) -> {
                    if (split.length != 2) {
                        return;
                    }
                    String code = split[1];
                    String chinese = split[0];
                    ArrayList<String> list = dictMap.get(code);
                    if (list == null) {
                        list = new ArrayList<>();
                        list.add(chinese);
                        dictMap.put(code, list);
                    } else {
                        list.add(chinese);
                    }
                });
                ObjectOutputStream ops = new ObjectOutputStream(new FileOutputStream(binPath));
                ops.writeObject(dictMap);
                ops.flush();
                ops.close();
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(binPath));
                //noinspection unchecked
                dictMap = (TreeMap<String, ArrayList<String>>) in.readObject();
                in.close();
            }
            System.out.println("读取完成——");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private record TipFileLineData(String chinese, String code) {

        @Override
        public String toString() {
            return chinese + QRStringUtils.A_TAB_CHAR + code;
        }
    }

    private class InputWindow extends QREmptyDialog {

        public InputWindow() {
            super(MainWindow.INSTANCE, false);
            setLayout(new BorderLayout());
            addWindowListener();
            QRTextField inputField = new InputField();
            add(inputField, BorderLayout.CENTER);
            inputField.setPreferredSize(new Dimension(50, 35));
            pack();
            updateLocation();
        }

        @Override
        public void updateLocation() {
            if (TyperTextPane.TYPER_TEXT_PANE.isShowing()) {
                pack();
                //位置跟随光标
                Point location = TyperTextPane.TYPER_TEXT_PANE.getLocationOnScreen();
                Rectangle r = TyperTextPane.TYPER_TEXT_PANE.positionRectangle(TypingData.currentTypedIndex).getBounds();
                if (r != null) {
                    JScrollBar scrollBar = TyperTextPane.TYPER_TEXT_PANE.addScrollPane().getVerticalScrollBar();
                    int x = location.x + r.x;
                    int y = location.y + r.y + r.height + TextPane.INSECT - (scrollBar.isVisible() ? scrollBar.getValue() : 0) - 5;
                    setLocation(x, y);
                } else {
                    //位于跟打面板底部
                    setLocation(location.x + TyperTextPane.TYPER_TEXT_PANE.getWidth() / 2 - 100, location.y + TyperTextPane.TYPER_TEXT_PANE.getHeight() - getHeight());
                }
            }
        }
    }

    private class InputField extends QRTextField {
        private String selections;
        private int codeLength;
        private boolean three42;

        public InputField() {
            addDocumentListener();
            addKeyListener();
            selections = Keys.strValue(Keys.TEXT_TIP_SELECTION);
            selections = selections.startsWith("_") ? selections : "_".concat(selections);
            int cl = Keys.intValue(Keys.TEXT_TIP_CODE_LENGTH);
            this.codeLength = cl > 0 ? 3 : 4;
            this.three42 = cl == 2;
        }

        @Override
        protected void keyType(KeyEvent e) {
            String text = getText();
            int len = text.length();
            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                if (len == 0) {
                    TyperTextPane.TYPER_TEXT_PANE.keyType(e);
                    TyperTextPane.TYPER_TEXT_PANE.removeText(TypingData.currentTypedIndex, 1);
                    e.consume();
                }
                return;
            }
            char keyChar = e.getKeyChar();
            if (keyChar == ' ') {
                keyChar = '_';
            }
            if (keyChar == ',' || keyChar == '.') {
                String mark = keyChar == ',' ? "，" : "。";
                ArrayList<String> list = dictMap.get(text);
                if (list != null) {
                    String input = list.get(0);
                    inputText(input + mark);
                    e.consume();
                    return;
                }
            }
            int index = selections.indexOf(keyChar);
            if (index > -1) {
                System.out.println("输字");
                if (len == 4) {
                    LinkedList<String> parts = QRArrayUtils.splitWithLength(text, 2);
                    ArrayList<String> list = dictMap.get(parts.getFirst());
                    if (list != null) {
                        String input = list.get(0);
                        TyperTextPane.TYPER_TEXT_PANE.print(input, TypingData.currentTypedIndex);
                        TextPane.TEXT_PANE.insertUpdateExecute(input);
                        if (find(parts.getLast(), index)) {
                            clear();
                            e.consume();
                            return;
                        }
                        return;
                    }
                } else if (find(text, index)) {
                    e.consume();
                    return;
                }
                inputText(getText());
            } else {
                if (three42 && len == 3) {
                    LinkedList<String> parts = QRArrayUtils.splitWithLength(text, 2);
                    ArrayList<String> list = dictMap.get(parts.getFirst());
                    if (list != null) {
                        String input = list.get(0);
                        TyperTextPane.TYPER_TEXT_PANE.print(input, TypingData.currentTypedIndex);
                        TextPane.TEXT_PANE.insertUpdateExecute(input);
                        setText(parts.getLast());
                        return;
                    }
                    clear();
                } else if (!three42 && len == this.codeLength) {
                    ArrayList<String> list = dictMap.get(text);
                    if (list != null) {
                        String input = list.get(0);
                        inputText(input);
                        return;
                    }
                    clear();
                }
            }
        }

        @Override
        protected void keyPress(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                int len = getText().length();
                if (len == 0) {
                    e.consume();
                }
            }
        }

        @Override
        protected void keyRelease(KeyEvent e) {
            char keyChar = e.getKeyChar();
            if (keyChar == ' ') {
                keyChar = '_';
            }
            int index = selections.indexOf(keyChar);
            if (index > -1) {
                e.consume();
                return;
            }
            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                e.consume();
            }
        }

        private void inputText(String input) {
            TyperTextPane.TYPER_TEXT_PANE.print(input, TypingData.currentTypedIndex);
            TextPane.TEXT_PANE.insertUpdateExecute(input);
            clear();
        }

        private boolean find(String text, int index) {
            ArrayList<String> list = dictMap.get(text);
            if (list != null) {
                if (index < list.size()) {
                    String input = list.get(index);
                    inputText(input);
                    return true;
                }
            }
            return false;
        }


        @Override
        protected void insertUpdate(DocumentEvent e) {

        }

        @Override
        protected void removeUpdate(DocumentEvent e) {
            super.removeUpdate(e);
        }
    }
}