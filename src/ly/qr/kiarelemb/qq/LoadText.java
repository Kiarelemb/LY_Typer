package ly.qr.kiarelemb.qq;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.contract.state.GroupButton;
import ly.qr.kiarelemb.data.LoadedTextData;
import ly.qr.kiarelemb.text.TextLoad;
import method.qr.kiarelemb.utils.*;
import swing.qr.kiarelemb.window.enhance.QRSmallTipShow;

import java.util.ArrayList;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-25 14:59
 **/
public class LoadText {
    public static String getLoadText() {
        if (!QqOperation.textCanSend()) {
            return null;
        }
        //复制群内消息
        QqOperation.start(QqOperation.GET_ARTICLE_MODEL, GroupButton.groupBtn.groupName());
        QRSleepUtils.sleep(100);
        //取得剪贴板内容
        String text = QRSystemUtils.getSysClipboardText();
        if (text == null || text.isEmpty()) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "复制失败！");
            return null;
        }
        if (GroupButton.groupBtn.isQQNT()) {
            return text;
        } else {
            //将内容分割成段
            String[] para = loadedTextSplitToParagraph(text);
            //从段中分析并得到发文内容
            ArrayList<LoadedTextData> textData = paraTextSplitToData(para, true);
            if (!textData.isEmpty()) {
                return textData.get(0).text();
            }
        }
        return text;
    }

    /**
     * 当要获取群内所有可跟打的段落时
     *
     * @return 所有可跟打的段
     */
    public static ArrayList<LoadedTextData> getLoadTexts() {
        if (!QqOperation.textCanSend() || GroupButton.groupBtn.isQQNT()) {
            return null;
        }
        //复制群内消息
        QqOperation.start(QqOperation.GET_ARTICLE_MODEL, GroupButton.groupBtn.groupName());
        QRSleepUtils.sleep(100);
        //取得剪贴板内容
        String text = QRSystemUtils.getSysClipboardText();
        if (text == null || text.isEmpty()) {
            QRSmallTipShow.display(MainWindow.INSTANCE, "复制失败！");
            return null;
        }
        //将内容分割成段
        String[] para = loadedTextSplitToParagraph(text);
        //从段中分析并得到发文内容
        return paraTextSplitToData(para, true);
    }

    private static String[] loadedTextSplitToParagraph(String loadedText) {
        final String[] split = loadedText.replaceAll("\r", "")
                .replaceAll("\n{3,}", "\n\n")
                .trim()
                .split("\n\n");
        return QRArrayUtils.reverse(split);
    }

    /**
     * 从选取的各段落中，取得所有发文
     *
     * @param onlyRecent 若 onlyRecent 为false，那就会载入所有的文
     */
    private static ArrayList<LoadedTextData> paraTextSplitToData(String[] paras, boolean onlyRecent) {
        ArrayList<LoadedTextData> lineTexts = new ArrayList<>();
        for (String paraText : paras) {
            ArrayList<String> lineTextSplit = lineSplit(paraText);
            //发文内容在两行及两行以上就判定为发文内容的候选，
            //由于每条消息第一行是发消息的人和发送，所以判断依据的行数要再加一行
            final int lineCounts = lineTextSplit.size();
            if (lineCounts < 3) {
                continue;
            }
            //最后一行带有“第”和“段”
            String endText = lineTextSplit.get(lineCounts - 1);
            int diIndex = endText.indexOf(TextLoad.DI);
            int duanIndex = endText.indexOf(TextLoad.DUAN);
            if (diIndex == -1 || duanIndex == -1) {
                //找不到就一定不是发文
                continue;
            }
            //先取得第一行的内容，以获取用户和时间
            String firstLine = lineTextSplit.get(0);
            int spaceIndex = firstLine.lastIndexOf(QRStringUtils.A_WHITE_SPACE);
            String user = firstLine.startsWith("【") ? firstLine.substring(firstLine.indexOf("】") + 1, spaceIndex) : null;
            //时间和发信人中间有空格隔开
            String foreText = lineCounts == 4 ? lineTextSplit.get(1) : (spaceIndex == -1 ? lineTextSplit.get(0) : "");
            String time = "";
            if (spaceIndex != -1) {
                time = firstLine.substring(spaceIndex + 1);
                if (!lineTexts.isEmpty() && onlyRecent) {
                    //将已选取的最后一条与这个进行时间比较
                    long diff = QRTimeUtils.getTimeDifference(time, lineTexts.get(lineTexts.size() - 1).time());
                    if (diff < 0 || diff > 180) {
                        //大于3分钟或出错，直接退出
                        return lineTexts;
                    }
                } else {
                    if (lineTexts.size() > 10) {
                        //太多也不能要，只要10条，
                        return lineTexts;
                    }
                }
            }
            //正常的发文一般是四行
            //如果只有三行，foreText就是空的
            String actualContent = lineTextSplit.get(lineCounts - 2);
            final LoadedTextData textData = new LoadedTextData(time, user, foreText, actualContent, endText);
            lineTexts.add(textData);
//            System.out.println(textData);
        }
        //以此返回的内容都是发文的格式化内容
        return lineTexts;
    }

    /**
     * 将传入的文本按行切割
     */
    private static ArrayList<String> lineSplit(String singlePara) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> lineText = new ArrayList<>();
        singlePara = QRArrayUtils.spaceFormat(singlePara);
        char[] chars = singlePara.toCharArray();
        for (char c : chars) {
            if (c == QRStringUtils.AN_ENTER_CHAR) {
                lineText.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }
            sb.append(c);
        }
        String s = sb.toString();
        if (!s.isEmpty()) {
            lineText.add(s);
        }
        return lineText;
    }
}
