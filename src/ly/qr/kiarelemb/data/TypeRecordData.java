package ly.qr.kiarelemb.data;

import ly.qr.kiarelemb.component.TextPane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * @param time   打字上屏的时间
 * @param length 单次上屏的字数
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TypeRecordData
 * @description 记录每次上屏的字与时间
 * @create 2024/6/29 下午10:28
 */
public record TypeRecordData(long time, int length) {
    private static final LinkedList<TypeRecordData> TYPE_LIST_DATA = new LinkedList<>();
    private static TreeSet<Long> typeWordsDiffLists = new TreeSet<>();
    private static ArrayList<Long> typeKeyDiffLists = new ArrayList<>();

    static {
        TextPane.TEXT_PANE.addSetTextFinishedAction(e -> {
            TYPE_LIST_DATA.clear();
            typeWordsDiffLists.clear();
            typeKeyDiffLists.clear();
        });
    }

    public static void putWords(long systemTime, int length) {
        if (!TypingData.typing) {
            return;
        }
        if (!TYPE_LIST_DATA.isEmpty()) {
            typeWordsDiffLists.add(systemTime - TYPE_LIST_DATA.getFirst().time());
        }
        TYPE_LIST_DATA.addFirst(new TypeRecordData(systemTime, length));
    }

    public static void putKeyStroke(long SystemTime) {
        if (!TypingData.typing) {
            return;
        }
        typeKeyDiffLists.add(SystemTime - TypingData.startTime);
    }

    public static TypeRecordData updateData(long endTime) {
        if (!TypingData.typing) {
            return null;
        }
        int size = TYPE_LIST_DATA.size();
        if (size == 0) {
            return null;
        }

        int length = 0;
        int wordLengthToCount;
        long timeDiffToCount;
        Iterator<TypeRecordData> iterator = TYPE_LIST_DATA.iterator();
        // 少于 3 次上屏
        if (size < 3) {
            while (iterator.hasNext()) {
                length += iterator.next().length();
            }
            wordLengthToCount = length;
            timeDiffToCount = endTime - TypingData.startTime;
            return new TypeRecordData(timeDiffToCount, wordLengthToCount);
        }

        // 少于 10 次上屏
        if (size < 10) {
            // 只记录最后上屏三次的打字情况，最后三次上屏的字数总和可能多于 3 字
            for (int i = 0; i < 3; i++) {
                length += iterator.next().length();
            }
            wordLengthToCount = length;
            timeDiffToCount = endTime - TYPE_LIST_DATA.get(2).time();
            return new TypeRecordData(timeDiffToCount, wordLengthToCount);
        }

        // 多于 10 次上屏

        // 对所有上屏时间排序，去极端首末，算平均
        LinkedList<Long> list = listCleanAction();
        long avgTime = list.stream().mapToLong(Long::longValue).sum() / list.size();

        // 边界上屏时间。从最后上屏开始遍历三次，若其中一次上屏时间超过 5 倍平均时间，则打断不再统计
        long bound = avgTime * 5;
        TypeRecordData next = iterator.next();
        wordLengthToCount = next.length();
        timeDiffToCount = endTime - next.time();
        if (timeDiffToCount > bound) {
            return new TypeRecordData(timeDiffToCount, wordLengthToCount);
        }
        length += next.length();
        for (int i = 0; i < 3; i++) {
            long foreTime = next.time();
            next = iterator.next();
            if (foreTime == next.time()) {
                i--;
                length += next.length();
                continue;
            }
            long diff = foreTime - next.time();
            if (diff > bound) {
                break;
            }
            timeDiffToCount += diff;
            length += next.length();
        }
        wordLengthToCount = length;
        return new TypeRecordData(timeDiffToCount, wordLengthToCount);
    }

    /**
     * 对特定列表进行清理操作，以保留满足特定条件的元素。
     * 此方法的目的是为了过滤出一个列表，其中第一个元素是剩余元素中的最小值，最后一个元素是剩余元素中的最大值，
     * 并且最小值至少是剩余元素中第二个值的两倍，最大值最多是剩余元素中倒数第二个值的一半。
     *
     * @return 经过清理操作后的 LinkedList<Long> ，包含满足条件的元素。
     */
    private static LinkedList<Long> listCleanAction() {
        // 初始化一个链表，其初始元素来自typeWordsDiffLists，并移除第一个元素。
        LinkedList<Long> list = new LinkedList<>(typeWordsDiffLists);
        list.removeFirst();

        // 获取链表中的第一个元素。
        Long first = list.getFirst();
        // 当链表的第二个元素小于第一个元素的一半时，移除第一个元素，直到条件不再满足。
        while (list.get(1) < first / 2) {
            list.removeFirst();
            first = list.getFirst();
        }
        // 移除链表的最后一个元素。
        list.removeLast();
        // 获取更新后的链表的最后一个元素。
        Long last = list.getLast();
        // 当链表的倒数第二个元素大于最后一个元素的两倍时，移除最后一个元素，直到条件不再满足。
        while (list.get(list.size() - 2) > last * 2) {
            list.removeLast();
            last = list.getLast();
        }

        // 返回经过清理操作后的链表。
        return list;
    }
}