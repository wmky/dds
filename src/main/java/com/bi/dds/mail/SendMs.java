package com.bi.dds.mail;

import java.util.ArrayList;
import java.util.List;


public class SendMs {
 
    public int sendMs(String numbers,String msContent){
        
        int r = 0;
//        try {
//            String url = ConstantSMS.url;
//         //   System.out.println(url);
//            HessianProxyFactory factory = new HessianProxyFactory(); 
//            factory.setChunkedPost(false);
//            CommonSms commonSms = (CommonSms) factory.create( CommonSms.class, url);
//            CommonSend commonSend = new CommonSend();
//            //写入短信内容
//            commonSend.setContent(msContent);
//            //写入优先级
//            commonSend.setPriority(Integer.valueOf(GetProDatas.getSmsLevel()));
//            //写入系统来源
//            commonSend.setSource(Integer.valueOf(GetProDatas.getSmsSource()));
//            List<String> mobileList=new ArrayList<String>(); 
//            String [] theNums = numbers.split(ScheduleConstants.mobileNumSplit);
//            for(int i=0;i<theNums.length;i++){
//                mobileList.add(theNums[i]);
//            }
//            //写入短信序列
//            commonSend.setMobileList(mobileList);
//            //调用多短信同内容插入接口
//            commonSms.insertCommonSendBatchNew(commonSend);
//            r=1;
//            } catch (Exception e) {     
//                e.printStackTrace();   
//                r=0;
//            }
            return r;
    }
    
    /**
     * 测试main方法
     */
    public static void main(String[] args) {
        String s1 = System.getProperty("java.version");
      //  System.out.println(s1);
        SendMs s = new SendMs();
        int r = s.sendMs("15927203196;15927203196", "bi预警短信测试");
      //  System.out.println(r);
    }
}
