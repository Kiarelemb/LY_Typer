package ly.qr.kiarelemb.text.send.data;

import ly.qr.kiarelemb.data.TypingData;
import ly.qr.kiarelemb.qq.SendText;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.TextSendManager;
import method.qr.kiarelemb.utils.QRFileUtils;
import method.qr.kiarelemb.utils.QRMathUtils;
import method.qr.kiarelemb.utils.QRRandomUtils;
import method.qr.kiarelemb.utils.QRStringUtils;
import method.qr.kiarelemb.utils.data.QRTextSendData;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 当前跟打信息记录类
 *
 * @author Kiarelemb QR
 */
public final class TypedData implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = -5849714470754967711L;
    private final String fileName;
    private final String fileCrc;
    private final int totalWordsNum;
    private final String filePath;
    private final boolean paraNumRandom;
    private boolean randomPick = false;
    private long startIndex;
    private int typedTimes;
    private int remainingWordsCount;
    private int perLength;

    /**
     * @param fileName            跟打文件名，不带拓展名
     * @param fileCrc             文件校验CRC码
     * @param startIndex          本次跟打开始索引
     * @param typedTimes          已跟打次数
     * @param remainingWordsCount 剩余字数
     * @param perLength           每段字数
     * @param paraNumRandom       段号随机
     */
    public TypedData(String fileName, String fileCrc, long startIndex, int typedTimes, int totalWordsNum,
                     int remainingWordsCount, int perLength, boolean paraNumRandom) {
        this.fileName = fileName;
        filePath = Info.TYPE_DIRECTORY + fileName + ".txt";
        this.fileCrc = fileCrc;
        this.startIndex = startIndex;
        this.typedTimes = typedTimes;
        this.totalWordsNum = totalWordsNum;
        this.remainingWordsCount = remainingWordsCount;
        this.perLength = perLength;
        this.paraNumRandom = paraNumRandom;
        if (!this.paraNumRandom) {
            paraNum = 1;
        }
    }

    /**
     * 当前段号
     */
    private int paraNum;
    /**
     * 当前段文本内容
     */
    private String currentText;

    private QRTextSendData sendData = null;

    public TypedData gotoForePara() {
        if (sendData == null || startIndex <= 0) {
            return this;
        }
        // 段号
        if (paraNumRandom) {
            paraNum = QRRandomUtils.getRandomInt(100000, 999999);
        } else {
            paraNum--;
        }
        currentText = sendData.text();
        remainingWordsCount += currentText.length();
        return this;
    }

    public String foreParaText() {
        sendData = QRFileUtils.fileForeReaderByRandomAccessMarkPositionFind(filePath, startIndex, perLength,
                TypingData.textLoadIntelli);
        // 上一段的结束位置
        startIndex -= sendData.typeTextByteLen();
        return getSendParaText();
    }

    /**
     * 为进入下一段前做数据计算准备。在最开始发文时，该方法不调用
     *
     * @return 当前跟打记录文件
     */
    public TypedData gotoNextPara() {
        // 段号
        if (paraNumRandom) {
            paraNum = QRRandomUtils.getRandomInt(100000, 999999);
        } else {
            paraNum++;
        }
        if (sendData == null) {
            return this;
        }
        // 下一段的开始位置
        startIndex += sendData.typeTextByteLen();
        return this;
    }

    public String nextParaText() {
        TypedData data = TextSendManager.data();
        if (data.randomPick) {
            String fileText = QRFileUtils.fileReaderWithUtf8All(filePath);
            String[] singleParts = QRStringUtils.getChineseExtraPhrase(fileText);
            StringBuilder text = new StringBuilder();
            int word = data.perLength;
            Set<Integer> parts = new HashSet<>(word);
            while (parts.size() < word) {
                int i = QRRandomUtils.getRandomInt(0, data.totalWordsNum);
                if (parts.add(i)) {
                    text.append(singleParts[i]);
                }
            }
            currentText = text.toString();
            return getSendParaText();
        }
        sendData = QRFileUtils.fileReaderByRandomAccessMarkPositionFind(filePath, startIndex, perLength,
                TypingData.textLoadIntelli);
        currentText = sendData.text();
        remainingWordsCount -= currentText.length();
        return getSendParaText();
    }

    private String getSendParaText() {
        String fore = fileName + "\n";
        String ends;
        if (TextSendManager.data().randomPick) {
            ends = String.format("\n-----第%d段 %d字 单字随机抽取", paraNum, TextSendManager.data().perLength);
        } else {
            ends = String.format("\n-----第%d段 %d字 进度%s%%", paraNum, currentText.length(),
                    QRMathUtils.doubleFormat((totalWordsNum - remainingWordsCount) * 100d / totalWordsNum, 2));
        }
        TextSendManager.save();
        String text = fore + currentText + ends;
        SendText.sendText(text);
        return text;
    }

    public void addTypedTimes() {
        this.typedTimes++;
    }

    public void setPerLength(int perLength) {
        this.perLength = perLength;
    }

    public void setRandomPick(boolean randomPick) {
        this.randomPick = randomPick;
    }

    public String fileName() {
        return fileName;
    }

    public String fileCrc() {
        return fileCrc;
    }

    public long startIndex() {
        return startIndex;
    }

    public int typedTimes() {
        return typedTimes;
    }

    public int remainingWordsCount() {
        return remainingWordsCount;
    }

    public int totalWordsNum() {
        return totalWordsNum;
    }

    public String filePath() {
        return filePath;
    }

    public boolean paraNumRandom() {
        return paraNumRandom;
    }

    public int perLength() {
        return perLength;
    }

    public int paraNum() {
        return paraNum;
    }

    public String currentText() {
        return currentText;
    }

    public boolean randomPick() {
        return randomPick;
    }

    public QRTextSendData sendData() {
        return sendData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypedData typedData)) return false;
        return totalWordsNum == typedData.totalWordsNum && paraNumRandom == typedData.paraNumRandom && randomPick == typedData.randomPick && startIndex == typedData.startIndex && typedTimes == typedData.typedTimes && remainingWordsCount == typedData.remainingWordsCount && perLength == typedData.perLength && paraNum == typedData.paraNum && Objects.equals(fileName, typedData.fileName) && Objects.equals(fileCrc, typedData.fileCrc) && Objects.equals(filePath, typedData.filePath) && Objects.equals(currentText, typedData.currentText) && Objects.equals(sendData, typedData.sendData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileCrc, totalWordsNum, filePath, paraNumRandom, randomPick, startIndex, typedTimes, remainingWordsCount, perLength, paraNum, currentText, sendData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "发文数据：[", "]")
                .add("fileName='" + fileName + "'")
                .add("fileCrc='" + fileCrc + "'")
                .add("totalWordsNum=" + totalWordsNum)
                .add("filePath='" + filePath + "'")
                .add("paraNumRandom=" + paraNumRandom)
                .add("randomPick=" + randomPick)
                .add("startIndex=" + startIndex)
                .add("typedTimes=" + typedTimes)
                .add("remainingWordsCount=" + remainingWordsCount)
                .add("perLength=" + perLength)
                .add("paraNum=" + paraNum)
                .add("sendData=" + sendData)
                .toString();
    }
}