package com.ktds.member.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.ktds.member.vo.MemberVO;

public class MemberDaoImplForOracle extends SqlSessionDaoSupport implements MemberDao{

	@Override
	public int selectCountMemberEmail(String email) {
		return getSqlSession().selectOne("MemberDao.selectCountMemberEmail", email);
	}
	
	@Override
	public int selectCountMemberNickname(String nickname) {
		return getSqlSession().selectOne("MemberDao.selectCountMemberNickname", nickname);
	}
	
	@Override
	public int insertMember(MemberVO memberVO) {
		// insert("interface 이름.현재 method 이름", 파라미터 있을 경우 파라미터 이름)
		return getSqlSession().insert("MemberDao.insertMember", memberVO);
	}

	@Override
	public MemberVO selectMember(MemberVO memberVO) {
		// 하나씩만 가져옴 - selectOne("interface 이름.현재 method 이름", 파라미터 있을 경우 파라미터 이름)
		return getSqlSession().selectOne("MemberDao.selectMember", memberVO);
	}

	@Override
	public int deleteMember(int id) {
		return getSqlSession().delete("MemberDao.deleteMember", id);
	}
	
	@Override
	public String selectSalt(String email) {
		return getSqlSession().selectOne("MemberDao.selectSalt", email);
	}
	
}
