package com.bi.dds.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bi.dds.service.SystemItf;
import com.bi.dds.model.BiUser;
import com.bi.dds.util.GetProDatas;

/**
 * @author 
 *
 */
@Service(value = "systemItf")
public class SystemItfImpl implements SystemItf{

	//日志类
	private static Logger logger = Logger.getLogger(SystemItfImpl.class);

	public BiUser getUserByNameAndPass(String userTag, String passWord)
			{
		// TODO Auto-generated method stub
		BiUser user = new BiUser();
		try{
			//查询用户是否存在
			/*String hql = "select t from BiUser t where t.userName = '"+userTag+"' and t.password = '"+passWord+"' ";
			List list = basedao.findByHQL("", hql);
			if(null!=list){
				user = (BiUser) list.get(0);
			}*/
			
			//user = (BiUser) basedao.findObjectById("", BiUser.class, "1");
			
			user.setUserName(userTag);
			user.setId(GetProDatas.getUserId());
			user.setPassword(passWord);
			user.setUserDept(GetProDatas.getDept());
			user.setEmail(GetProDatas.getEmail());
			user.setMobile(GetProDatas.getMobile());
			user.setTel(GetProDatas.getTel());
			user.setSkype(GetProDatas.getSkype());
		}catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return user;
	}
}
