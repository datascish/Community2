package com.ktds.member.service;

import com.ktds.community.dao.CommunityDao;
import com.ktds.member.dao.MemberDao;
import com.ktds.member.vo.MemberVO;

public class MemberServiceImpl implements MemberService {
	private MemberDao memberDao;
	private CommunityDao communityDao;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	@Override
	public boolean createMember(MemberVO memberVO) {
		return memberDao.insertMember(memberVO) > 0 ;
	}

	@Override
	public MemberVO readMember(MemberVO memberVO) {
		return memberDao.selectMember(memberVO);
	}

	@Override
	public boolean removeMember(int id, String deleteFlag) {
		if (deleteFlag.equals("y")) {
				communityDao.deleteMyCommunities(id); // community와 member의 관계는 1:多이기 때문에 조건문 사용 x - null 주의
		}
		return memberDao.deleteMember(id) > 0;
	}
}
