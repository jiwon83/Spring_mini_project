package com.javaex.dao;

import com.javaex.vo.UserVo;

public interface UserDao {
	public int insert(UserVo vo);
	public int update(UserVo vo);
	public UserVo getUser(String email, String pw);
	//public List<UserVo> getUser();
	public UserVo getUser(int no);
	public String idCheck(String email);//id 확인
	
}
