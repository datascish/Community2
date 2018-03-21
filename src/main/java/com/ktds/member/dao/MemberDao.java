package com.ktds.member.dao;

import com.ktds.member.vo.MemberVO;

public interface MemberDao {
	// 회원가입 interface
	
	public int selectCountMemberEmail(String email);
	
	public int selectCountMemberNickname(String nickname);
	
	public int insertMember(MemberVO memberVO);
	
	public MemberVO selectMember(MemberVO memberVO);
	
	public int deleteMember(int id);
	
	public String selectSalt(String email); 
	// 로그인을 해야만 사용자의 ID를 알 수 있기 때문에 email을 파라미터로

}
