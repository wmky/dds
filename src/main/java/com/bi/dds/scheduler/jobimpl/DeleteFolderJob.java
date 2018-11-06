package com.bi.dds.scheduler.jobimpl;

import java.io.File;
import java.util.Calendar;

import com.bi.dds.scheduler.BaseJob;
import com.bi.dds.util.GetProDatas;
import com.bi.dds.util.MyFileUtil;


public class DeleteFolderJob extends BaseJob{

    
    @Override
    public void run() throws Exception {
       // System.out.println("delete folder job:"+super.getJobId());
        MyFileUtil fileUtil = new MyFileUtil();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        cal.setTime(cal.getTime());
        String folder = GetProDatas.getCsvFilePath();
        fileUtil.deleteFile(new File(folder+MyFileUtil.getDateFileStrAgo()));
    }

}
