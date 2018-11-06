package com.bi.dds.service;

import java.util.List;

import com.bi.dds.model.BiUser;



/**
 * 
 * 
 * @author 
 * @version 1.0 , 2011-09-05  创建
 */
public interface SystemItf {

	public BiUser getUserByNameAndPass(String userTag,String passWord);
}
