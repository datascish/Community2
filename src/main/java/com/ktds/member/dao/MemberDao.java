package com.ktds.member.dao;

import com.ktds.member.vo.MemberVO;

public interface MemberDao {
	// 회원가입 interface
	public int insertMember(MemberVO memberVO);
	
	public MemberVO selectMember(MemberVO memberVO);
	
	public int deleteMember(int id);
	
}
