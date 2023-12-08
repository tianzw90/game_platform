package com.platform.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: wanghaotian
 * Date: 2019/11/14
 * Time: 7:56 PM
 * 常用的一些工具方法
 */
public class CommonUtil {
    /**
     * 随机数
     * //TODO 需要完善，可能会被代码扫描视为非真正的随机
     * @param place 定义随机数的位数
     */
    public static String randomGen(int place) {
        String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();
        for(int i=0;i<place;i++) {
            sb.append(base.charAt(rd.nextInt(base.length())));
        }
        return sb.toString();
    }
    /**
     * 随机纯数字
     * //TODO 需要完善，可能会被代码扫描视为非真正的随机
     * @param place 定义随机数验证码位数
     */
    public static String randomNumGen(int place) {
        String base = "0123456789";
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();
        for(int i=0;i<place;i++) {
            sb.append(base.charAt(rd.nextInt(base.length())));
        }
        return sb.toString();
    }

    /**
     * 查询条件
     * @param arr
     * @param value
     * @return
     */
    public static int getStrArrIndex(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (humpToLine(arr[i]).contains(humpToLine(value))) {
                return i;
            }
        }
        return -1;//如果未找到返回-1
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /** 驼峰转下划线,效率比上面高 */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

//    public static Map<String, Object> beanToMap(Object obj) {
//        Map<String, Object> params = new HashMap<String, Object>(0);
//        try {
//            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
//            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
//            for (int i = 0; i < descriptors.length; i++) {
//                String name = descriptors[i].getName();
//                String type=descriptors[i].getPropertyType().toString();
//                if (!"class".equals(name)) {
//                    Object ob=propertyUtilsBean.getNestedProperty(obj, name);
//
//                    if(ob==null){//必须加非空判断 不然 poi导出逻辑 不认识null
//                        ob="_";
//                    }else{
//                        if(type.equals("class java.lang.Integer")){  //整数不用考虑
//                            //System.out.println("type="+type+",name="+name+",ob="+ob);
//                        }else if(type.equals("class java.util.Date")){//日期类型转为 YYYY-MM-DD格式
//                            ob=ob.toString().substring(0,10);
//                            //System.out.println("type="+type+",name="+name+",ob="+ob);
//                        }
//                    }
//                    params.put(name, ob);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return params;
//    }
//    /**
//     *
//     * 导出 工具  字段为null时显示空白，不显示下划线
//     * update by wht 2018-05-06 16:37:52
//     * @param obj
//     * @return
//     */
//    public static Map<String, Object> beanToMapNoUnderLine(Object obj) {
//        Map<String, Object> params = new HashMap<String, Object>(0);
//        try {
//            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
//            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
//            for (int i = 0; i < descriptors.length; i++) {
//                String name = descriptors[i].getName();
//                String type=descriptors[i].getPropertyType().toString();
//                if (!"class".equals(name)) {
//                    Object ob=propertyUtilsBean.getNestedProperty(obj, name);
//
//                    if(ob==null){//必须加非空判断 不然 poi导出逻辑 不认识null
//                        ob="";
//                    }else{
//                        if(type.equals("class java.lang.Integer")){  //整数不用考虑
//                            //System.out.println("type="+type+",name="+name+",ob="+ob);
//                        }else if(type.equals("class java.util.Date")){//日期类型转为 YYYY-MM-DD格式
//                            ob=ob.toString().substring(0,10);
//                            //System.out.println("type="+type+",name="+name+",ob="+ob);
//                        }
//                    }
//                    params.put(name, ob);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return params;
//    }

    /**
     * 大陆号码或香港号码均可 校验手机号
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])|(18[0-9])|(19[1,3,5,8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /**
     * 校验邮箱
     */
    public static boolean validateEmail(String str) throws PatternSyntaxException {
        String regExp = "^\\+?[a-z0-9](([-+.]|[_]+)?[a-z0-9]+)*@([a-z0-9]+(\\.|\\-))+[a-z]{2,6}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }



    private final static String BIRTH_DATE_FORMAT = "yyyyMMdd"; // 身份证号码中的出生日期的格式

    private final static Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L); // 身份证的最小出生日期,1900年1月1日

    private final static int NEW_CARD_NUMBER_LENGTH = 18;

    private final static int OLD_CARD_NUMBER_LENGTH = 15;

    private final static char[] VERIFY_CODE = { '1', '0', 'X', '9', '8', '7',
            '6', '5', '4', '3', '2' }; // 18位身份证中最后一位校验码

    private final static int[] VERIFY_CODE_WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1,
            6, 3, 7, 9, 10, 5, 8, 4, 2 };// 18位身份证中，各个数字的生成校验码时的权值


    /**
     * 如果是15位身份证号码，则自动转换为18位
     *
     * @param cardNumber
     * @return
     */
    public static boolean checkIdCardNumber(String cardNumber){
        if (null != cardNumber){
            cardNumber = cardNumber.trim();
            if (OLD_CARD_NUMBER_LENGTH == cardNumber.length()){
                cardNumber = contertToNewCardNumber(cardNumber);
            }
            return validate(cardNumber);
        }
        return false;
    }

    public static boolean validate(String cardNumber){
        boolean result = true;
        result = result && (null != cardNumber); // 身份证号不能为空
        result = result && NEW_CARD_NUMBER_LENGTH == cardNumber.length(); // 身份证号长度是18(新证)
        // 身份证号的前17位必须是阿拉伯数字
        for (int i = 0; result && i < NEW_CARD_NUMBER_LENGTH - 1; i++){
            char ch = cardNumber.charAt(i);
            result = result && ch >= '0' && ch <= '9';
        }
        // 身份证号的第18位校验正确
        result = result
                && (calculateVerifyCode(cardNumber) == cardNumber
                .charAt(NEW_CARD_NUMBER_LENGTH - 1));
        // 出生日期不能晚于当前时间，并且不能早于1900年
        try{
            Date birthDate = new SimpleDateFormat(BIRTH_DATE_FORMAT)
                    .parse(getBirthDayPart(cardNumber));
            result = result && null != birthDate;
            result = result && birthDate.before(new Date());
            result = result && birthDate.after(MINIMAL_BIRTH_DATE);
            /**
             * 出生日期中的年、月、日必须正确,比如月份范围是[1,12],
             * 日期范围是[1,31]，还需要校验闰年、大月、小月的情况时，
             * 月份和日期相符合
             */
            String birthdayPart = getBirthDayPart(cardNumber);
            String realBirthdayPart = new SimpleDateFormat(BIRTH_DATE_FORMAT)
                    .format(birthDate);
            result = result && (birthdayPart.equals(realBirthdayPart));
        }catch(Exception e){
            result = false;
        }
        return result;
    }

    private static String getBirthDayPart(String cardNumber){
        return cardNumber.substring(6, 14);
    }

    /**
     * 校验码（第十八位数）：
     *
     * 十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
     * 2; 计算模 Y = mod(S, 11)< 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9
     * 8 7 6 5 4 3 2
     *
     * @param cardNumber
     * @return
     */
    private static char calculateVerifyCode(CharSequence cardNumber){
        int sum = 0;
        for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++){
            char ch = cardNumber.charAt(i);
            sum += ((int) (ch - '0')) * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 把15位身份证号码转换到18位身份证号码<br>
     * 15位身份证号码与18位身份证号码的区别为：<br>
     * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
     * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成
     *
     * @param oldCardNumber
     * @return
     */
    private static String contertToNewCardNumber(String oldCardNumber){
        StringBuilder buf = new StringBuilder(NEW_CARD_NUMBER_LENGTH);
        buf.append(oldCardNumber.substring(0, 6));
        buf.append("19");
        buf.append(oldCardNumber.substring(6));
        buf.append(calculateVerifyCode(buf));
        return buf.toString();
    }

    /**
     *  list集合转数组
     * @param list
     * @return
     */
    public static String[] list2Array(List<String> list){
        String ids[]=new String[list.size()];
        for(int i=0;i<list.size();i++){
            ids[i]=list.get(i);
        }
        return ids;
    }
    /**
     * 验证字符串内容是否包含下列非法字符<br>
     * @param content 字符串内容
     * @param illegal  指定要过滤的非法字符  可以传null 此时默认为  `~()!#%^&*=+\\|{};:'\",<>/?○●★☆☉♀♂※¤╬の〆;
     * @return
     */
    public static boolean validateLegalString(String content,String illegal) {
        if(ObjectUtils.isEmpty(illegal)){
            illegal = "`~()!#%^&*=+\\|{};:'\",<>/?○●★☆☉♀♂※¤╬の〆";
        }
        char isLegalChar = 't';
        L1: for (int i = 0; i < content.length(); i++) {
            for (int j = 0; j < illegal.length(); j++) {
                if (content.charAt(i) == illegal.charAt(j)) {
                    isLegalChar = content.charAt(i);
                    break L1;
                }
            }
        }
        if(isLegalChar=='t'){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 批量 对象转换 同名属性转换
     * @param sourceList
     * @param voClass
     * @return
     */
    public static List copyList(List <? extends Object> sourceList , Class voClass){
        List voList=new ArrayList();
        Object voObj =null;
        for(Object poObj:sourceList){
            try {
                voObj = voClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(poObj, voObj);
            voList.add(voObj);
        }
        return voList;
    }
    /**
     * 带分页的  批量 对象转换 同名属性转换
     * @param sourcePackageList
     * @param voClass
     * @return
     */
    public static IPage copyPageList(IPage<? extends Object> sourcePackageList , Class voClass){
        List  sourceList= sourcePackageList.getRecords();
        List targetList=copyList(sourceList,voClass);
        sourcePackageList.setRecords(targetList);
        return sourcePackageList;
    }

    /**
     * 验证时间戳是否为指定日期格式
     *
     * @param rawDateStr 待验证字符串 示例：java 2002-02-02T12:20:20.000Z
     * @return 有效性结果, true 为正确, false 为错误
     */
    public static boolean dateStrIsValid(String rawDateStr) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        try{
            format.parse(rawDateStr);
        }catch (ParseException e){
            return false;
        }
        return true;

    }
    /**
     * 验证字符串是否为指定日期格式
     *
     * @param rawDateStr 待验证字符串
     * @param pattern    日期字符串格式, 例如 "yyyy-MM-dd"
     * @return 有效性结果, true 为正确, false 为错误
     */
    public static boolean dateStrIsValid(String rawDateStr, String pattern) {
      /*  SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            // 转化为 Date类型测试判断
            date = dateFormat.parse(rawDateStr);
            //return rawDateStr.equals(dateFormat.format(date));
            return true;
        } catch (ParseException e) {
            return false;
        }*/
        final DateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        try {
            sdf.parse(rawDateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;

    }

    /**
     * 通过抛出异常判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        // 这个代码意思是如果没有抛出异常 就证明是数字，抛出异常了那么就不是数字
        // 异常不适合做逻辑判断，不适合做业务逻辑，异常使用不合理，不符合代码规范
        try {
            // parseInt 是将字符串转换为整数类型，返回一个int类型，如果字符串中有非数字类型字符，则会抛出一个NumberFormatException的异常
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否是BigDecimal
     * @param str 要判断的数字
     * @param valDecimal 精度
     * @return
     */
    public static boolean isNumeric(String str,Integer valDecimal){
        if(str==null || str.trim().length() == 0){
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        int i = (chars[0] == '-') ? 1 : 0;
        if(i == sz) return false;

        if(chars[i] == '.') return false;//除了负号，第一位不能为'小数点'
        if(str.indexOf(".")<0){
            return isNumber(str);
        }
        if(str.length()-1-str.indexOf(".")>valDecimal){//判断是否是指定精度的小数
            return false;
        }
        boolean radixPoint = false;
        for(; i < sz; i++){
            if(chars[i] == '.'){
                if(radixPoint) return false;
                radixPoint = true;
            }else if(!(chars[i] >= '0' && chars[i] <= '9')){
                return false;
            }
        }
        return true;
    }
    /*public static boolean isNumeric(String str,Integer valDecimal){
        Pattern pattern = Pattern.compile("[0-9]*");
        if(str.indexOf(".")>0){//判断是否有小数点
            if(str.indexOf(".")==str.lastIndexOf(".") && str.split("\\.").length==2){ //判断是否只有一个小数点
                if(!ObjectUtils.isEmpty(valDecimal)&&str.length()-1-str.indexOf(".")>valDecimal){//判断是否是指定精度的小数
                    return false;
                }
                return pattern.matcher(str.replace(".","")).matches();
            }else {
                return false;
            }
        }else {
            return pattern.matcher(str).matches();
        }
    }*/

}
