package ly.qr.kiarelemb.data;

import ly.qr.kiarelemb.text.TextLoad;
import ly.qr.kiarelemb.text.tip.data.TextStyleManager;
import ly.qr.kiarelemb.text.tip.data.TipCharStyleData;
import ly.qr.kiarelemb.text.tip.data.TipPhraseStyleData;

import javax.swing.text.SimpleAttributeSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-02-03 12:44
 **/
public class TipData {
    public final ArrayList<TipCharStyleData> tcsd;
    public final ArrayList<TipPhraseStyleData> tpsd;
    public final String codes;
    public final int[] indexes;
    public final int singleCodeNum;
    public final StandardData data;


    public TipData(ArrayList<TipCharStyleData> tcsd, ArrayList<TipPhraseStyleData> tpsd, String codes, int[] indexes, int singleCodeNum, StandardData data) {
        this.tcsd = tcsd;
        this.tpsd = tpsd;
        this.codes = codes;
        this.indexes = indexes;
        this.singleCodeNum = singleCodeNum;
        this.data = data;
    }

    public SimpleAttributeSet getForeAttributeSet(int index) {
        TipPhraseStyleData data = null;
        while (data == null) {
            data = this.tpsd.get(index);
            index--;
        }
        return data.getStyle();
    }

    public SimpleAttributeSet getForeAttributeSetExact(int index) {
        TipPhraseStyleData data = this.tpsd.get(index);
        return data == null ? TextStyleManager.getDefaultStyle() : data.getStyle();
    }

    /**
     * @return {@code TipLenAndIndexData} 中的第一个参数 {@code index} 为 {@code indexes} 中最贴近 {@code currentIndex} 的值，且该值小于等于 {@code currentIndex}；
     * <p>{@code TipLenAndIndexData} 中的另一个参数 {@code maxLen} 为下一个词结束位置与当前位置 {@code currentIndex} 的差
     */
    public int getMaxRange(final int currentIndex) {
        int s = 0;
        final int len = this.indexes.length;
        int e = len;
        int mid = len / 2;

        while (true) {
            int midValue = this.indexes[mid];
            if (midValue > currentIndex) {
                e = mid;
                if (mid - 1 < 0) {
                    return (mid + 2 < len) ? this.indexes[mid + 2] - currentIndex : TextLoad.TEXT_LOAD.wordsLength() - currentIndex;
                }
                int foreValue = this.indexes[mid - 1];
                if (foreValue < currentIndex) {
//                    midValue = indexes[--mid];
                    return (--mid + 2 < len) ? this.indexes[mid + 2] - currentIndex : TextLoad.TEXT_LOAD.wordsLength() - currentIndex;
                } else if (foreValue != currentIndex) {
                    e--;
                } else {
                    break;
                }
            } else if (midValue < currentIndex) {
                s = mid;
                if (mid + 1 == len) {
                    return TextLoad.TEXT_LOAD.wordsLength() - currentIndex;
                }
                final int nextValue = this.indexes[mid + 1];
                if (nextValue > currentIndex) {
                    return (mid + 2 < len) ? this.indexes[mid + 2] - currentIndex : TextLoad.TEXT_LOAD.wordsLength() - currentIndex;
                } else if (nextValue != currentIndex) {
                    s++;
                } else {
                    break;
                }
            } else {
                break;
            }
            mid = s + (e - s) / 2;
        }
        return 0;
    }

    public int getIndex(final int currentIndex) {
        int s = 0;
        final int len = this.indexes.length;
        int e = len;
        int mid = len / 2;
        while (true) {
            int midValue = this.indexes[mid];
            if (midValue > currentIndex) {
                e = mid;
                int foreValue = this.indexes[mid - 1];
                if (foreValue < currentIndex) {
                    return --mid;
                } else if (foreValue != currentIndex) {
                    e--;
                } else {
                    break;
                }
            } else if (midValue < currentIndex) {
                s = mid;
                final int nextValue = this.indexes[mid + 1];
                if (nextValue != currentIndex) {
                    s++;
                } else {
                    return ++mid;
                }
            } else {
                break;
            }
            mid = s + (e - s) / 2;
        }
        return mid;
    }


    @Override
    public String toString() {
        return "TipData{" +
                "tcsd=" + tcsd +
                ", tpsd=" + tpsd +
                ", codes='" + codes + '\'' +
                ", indexes=" + Arrays.toString(indexes) +
                ", singleCodeNum=" + singleCodeNum +
                ", TypedData=" + data +
                '}';
    }

    public static record StandardData(int first, int multi, int singleCounts, double phraseTypeCounts, int oneFirst,
                                      int oneMulti, int twoFirst, int twoMulti, int threeFirst, int threeMulti,
                                      int fourFirst, int fourMulti, int totalCounts, int leftCounts, int rightCounts,
                                      int spaceCounts) {
    }
}