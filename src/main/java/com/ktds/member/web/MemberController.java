package com.ktds.member.web;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.community.service.CommunityService;
import com.ktds.member.constants.Member;
import com.ktds.member.service.MemberService;
import com.ktds.member.vo.MemberVO;

@Controller
public class MemberController {
	private CommunityService communityService;
	private MemberService memberService;
	
	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String viewLoginPage() {
		// jsp page return
		return "member/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLoginAction (MemberVO memberVO, Errors errors, HttpServletRequest request) {
		
		HttpSession session = request.getSession(); // request 객체에서 session을 꺼내오는 방법
		// id를 누락했거나(id x) id를 안 적었을 경우(id o)를 check
//		if (errors.hasErrors()) {
//			ModelAndView view = new ModelAndView();
//			view.setViewName("member/login");
//			view.addObject("loginForm", loginForm);
//			return view;
//			return "member/login";
//		}
		// FIXME DB에 계정이 존재하지 않을 경우로 변경
		MemberVO loginMember = memberService.readMember(memberVO);
		if ( loginMember != null) {
			session.setAttribute(Member.USER, loginMember);
			return "redirect:/";
		}
		
//		if( loginForm.getEmail().equals("admin") && 
//			loginForm.getPassword().equals("1234")) {
//			// 로그인 성공 시 그 데이터를 session에 넣어라
//			loginForm.setNickname("관리자");
//			session.setAttribute(Member.USER, loginForm);
//			session.removeAttribute("status"); // status(key)를 가진 session을 지워라 - 없으면 그대로, 있으면 삭제
//			
//			return "redirect:/";
//		}
//		session.setAttribute("status", "fail"); // login을 실패했을 때만 만들어짐
		return "redirect:/login";
	}
	
	@RequestMapping(value="/regist", method=RequestMethod.GET)
	// parameter가 없어도 페이지 들어올 수 있도록 return type을 String으로
	public String viewRegistPage() {
		return "member/regist";
	}
	@RequestMapping(value="/regist", method=RequestMethod.POST)
	public ModelAndView doRegistAction(@ModelAttribute("registForm") @Valid MemberVO memberVO, Errors errors) {
		if ( errors.hasErrors()) {
			return new ModelAndView("member/regist");
		}
		if (memberService.createMember(memberVO)) {
			return new ModelAndView("redirect:/login");
		}
		return new ModelAndView("redirect:/login");
	}
	@RequestMapping("/logout")
	public String doLogoutAction(HttpSession session) {
		
		// 세션 소멸 - 세션 전체를 다 제거
		session.invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping("/account/delete/{deleteFlag}")
	public String doRemoveMember(HttpSession session, @PathVariable String deleteFlag,
			@RequestParam(required=false, defaultValue="") String token) {
		String sessionToken = (String) session.getAttribute("__TOKEN__");
		if (sessionToken == null || !sessionToken.equals(token)) {
			// sessionToekn이 null이거나 sessionToken을 임의로 변경한 경우
			return "error/404";
		}
		
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		if ( member == null) {
			return "redirect:/login";
		}
		int id = member.getId();
		
		//boolean isRemove = memberService.removeMember(id);
		if (memberService.removeMember(id, deleteFlag)) {
			session.invalidate();
		}
		return "member/delete/delete";
	}
	
	@RequestMapping("/delete/process1")
	public String viewVerifyPage() {
		return "member/delete/process1";
	}
	
	@RequestMapping("/delete/process2") // process1이 보내준 password 받아와야 함
	public ModelAndView viewDeleteMyCommunitiesPage
		(@RequestParam(required=false, defaultValue="") String password, HttpSession session) {
		if (password.length() == 0) {
			// password가 없다면
			return new ModelAndView("error/404");
		}
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		member.setPassword(password);
		
		MemberVO verifyMember = memberService.readMember(member);
		if (verifyMember == null) {
			return new ModelAndView("redirect:/delete/process1");
		}
		// 내가 작성한 게시글의 개수 가져오기
		int myCommunitiesCount = communityService.readMyCommunitiesCount(verifyMember.getId());
		
		ModelAndView view = new ModelAndView();
		view.setViewName("member/delete/process2");
		view.addObject("myCommunitiesCount", myCommunitiesCount);
		
		String uuid =  UUID.randomUUID().toString();
		session.setAttribute("__TOKEN__", uuid); 
		view.addObject("token",uuid); // 난수의 범위 지정
		return view;
	}
}
