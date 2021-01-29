// 本试卷唯一 ID: A58B6BDFF0E4B374AFA1C5272BF2322E, 请勿修改本行内容
/* 注意, 这里不要写包名. 留空 */
/* 这里如果用到其他的类, 记得要引用, 否则编译不过 */
import java.util.*;
/**
 * 回答方式: 直接保存或者复制本java文件, 然后在原处作答. 建议重命名成 meta-java-<姓名>.java
 * 选择题改变量赋值的字符串
 * 实现题在原本的函数体里返回正确答案, 注意不要改动函数结构. 用这一个Java文件完成
 * 本卷直接用代码判卷, 没有人工干预. 格式错误, 语法错误, 格式改动会导致试卷无效
 *
 * 注: 一个java文件可以有多个类, 但只能有一个public类. 所以你的实现中可以建内部类, 辅助类. 可以javac编译确认自己语法无误
 * 以下带public static修饰的实现题方法, 不要改变这个签名
 *
 *
 * 这里不要更改默认的类名, 也不要更改修饰符不要加public. 类名保持为 MetaJavaQuestionSheet
 * 可以改文件名, 建议改为 meta-java-<姓名>.java
 */
class MetaJavaQuestionSheet {


    private static String testString = " ADABEFFFDCBGH发货价啦AFG";
    private static String numA = "5135156864146198510515713546981304";
    private static String numB = " 14105283157813257031975091759832782750923";

    public static void main(String[] args) {
        System.out.println(removeDuplicate(testString));
        System.out.println(addStrings(numA, numB));
    }

    /* ============
     *  以下是实现题
     * ============
     */

    /**
     * 对字符串进行去重处理，并且保持原来的输入顺序。举例：
     * 输入的字符串 String  s = "ADABEFFFDCBGH发货价啦AFG"；
     * 经过处理之后结果应该是："ADBEFCGH发货价啦"
     * （考虑时间复杂度, 要求时间复杂度O(n)）
     *
     * @param str: 要去重的字符串
     * @return: 去重后的字符串
     */
    public static String removeDuplicate(String str) {
        if (str == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            String currentChar = Character.toString(str.charAt(i));
            if (!stringBuffer.toString().contains(currentChar)) {
                stringBuffer.append(currentChar);
            }
        }
        return stringBuffer.toString();
    }
    /**
     * 两个大数相加。
     * String numA = "5135156864146198510515713546981304...";
     * String numB = "14105283157813257031975091759832782750923...";
     * 两个字符串长度不一定相等，都可能特别长，甚至超过long的范围。求numA 和 numB的和（两个数字都是正整数）。
     *
     * ** 注意：不能直接使用 Java BigDecimal 系列 API**
     *
     * @param numA: 加数
     * @param numB: 加数
     */
    public static String addStrings(String numA, String numB) {
        int numIntA = 0;
        int numIntB = 0;
        if (numA == null || numA.length() <= 0) {
            numIntA = 0;
        }
        if (numB == null || numB.length() <= 0) {
            numIntB = 0;
        }
        long l = Long.parseLong(numA);
        long l1 = Long.parseLong(numB);
//        numIntA = Integer.parseInt(numA);
//        numIntB = Integer.parseInt(numB);
        System.out.println(l);
        System.out.println(l1);
        return "";
    }
}