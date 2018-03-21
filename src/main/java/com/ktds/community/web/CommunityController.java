package com.ktds.community.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.community.service.CommunityService;
import com.ktds.community.vo.CommunityVO;
import com.ktds.member.constants.Member;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.DownloadUtil;

@Controller
public class CommunityController {
	// service와 의존 관계
	private CommunityService communityService;

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}

	@RequestMapping("/") // 제일 첫 화면이 list page
	// String은 jsp(페이지 화면)만, ModelAndView는 jsp(페이지 화면) + data
	public ModelAndView viewListPage() {
		
		ModelAndView view = new ModelAndView();
		// /WEB-INF/view/community/list.jsp
		view.setViewName("community/list");

		List<CommunityVO> communityList = communityService.getAll();
		view.addObject("communityList", communityList);
		return view;
	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	// @GetMapping("/write") // spring 4.3v 이상일 때만 사용 가능
	public String viewWritePage() {
		
		// 전달할 data x, page만
		return "community/write"; // GET
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	// @PostMapping("/write")
	public ModelAndView doWrite(@ModelAttribute("writeForm") @Valid CommunityVO communityVO, Errors errors, 
			HttpSession session, HttpServletRequest request) { // POST // ip 정보는 request에 있음
		// 커맨드 객체 - VO를 통해서 한 번에 받아오는 방법
		// VO의 멤버변수와 받아올 값의 이름이 같아야 함
		if (session.getAttribute(Member.USER) == null) {
			return new ModelAndView("redirect:/login");
		}
		
		if (errors.hasErrors()) {
			ModelAndView view = new ModelAndView();
			view.setViewName("community/write");
			view.addObject("communityVO", communityVO);
			return view;
		}
		// 등록 직전 ip를 얻어와 communityVO 객체에 값을 넣어주기 - 작성자의 IP를 얻어오는 코드
		String requestorIp = request.getRemoteAddr();
		communityVO.setRequestIp(requestorIp);
		communityVO.save(); // file이 있으면 저장, 없으면 x
		boolean isSuccess = communityService.createCommunity(communityVO);

		if (isSuccess) {
			return new ModelAndView("redirect:/"); // 이 url(/ : list page)로 이동해라
		}
		return new ModelAndView("redirect:/write");
	}

	@RequestMapping("/read/{id}")
	public String viewReadPage(@PathVariable int id) {
		
		// TODO 조회수 증가
		if (communityService.incrementViewCount(id)) {
			return "redirect:/view/" + id;
		}
		return "redirect:/";
	}

	@RequestMapping("/view/{id}")
	public ModelAndView viewViewPage(@PathVariable int id) {
		
		ModelAndView view = new ModelAndView();
		view.setViewName("community/view");
		// id로 게시글 얻어오기
		CommunityVO community = communityService.getOne(id);
		view.addObject("community", community);

		return view;
	}

	@RequestMapping("/recommend/{id}")
	public String viewRecommendCount(@PathVariable int id) {
		
		// TODO 추천수 증가
		if (communityService.incrementRecommendCount(id)) {
			return "redirect:/view/" + id;
		}
		return "redirect:/";
	}
	
	@RequestMapping("/get/{id}")
	public void download(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {
		// return type이 void라도 response 보낼 수 있음
		// 게시글의 id를 가져와서 그 게시글에 있는 파일 이름을 가져올 것
		CommunityVO community = communityService.getOne(id);
		String filename = community.getDisplayFilename();
		
		DownloadUtil download = new DownloadUtil("d:/uploadFiles/" + filename );
		try {
			download.download(request, response, filename);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@RequestMapping("/remove/{id}")
	public String removeCommunity(@PathVariable int id, HttpSession session) {
		
		MemberVO member = (MemberVO) session.getAttribute(Member.USER); // 명시적 형변환 필요
		CommunityVO community = communityService.getOne(id); // 삭제하고자 하는 게시글의 정보를 가져오기
		boolean isMyCommunity = member.getId() == community.getUserId(); // 내가 쓴 게시글인지 확인

		//boolean isdelete = communityService.removeCommunity(id);
		
		if (isMyCommunity && communityService.removeCommunity(id)) {
			return "redirect:/";
		}
		return "/WEB-INF/view/error/404";
	}
	
	@RequestMapping(value="/modify/{id}", method=RequestMethod.GET)
	public ModelAndView viewModifyPage(@PathVariable int id, HttpSession session) {
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		CommunityVO community = communityService.getOne(id);
		
		int userId = member.getId();
		if (userId != community.getUserId()) {
			return new ModelAndView("error/404");
		}
		
		ModelAndView view = new ModelAndView();
		view.setViewName("community/write");
		view.addObject("communityVO", community); // 원본 글 보여주기
		view.addObject("mode", "modify"); // key : mode, value : modify
		return view;
	}
	
	@RequestMapping(value="/modify/{id}", method=RequestMethod.POST)
	public String doModifyAction(@PathVariable int id, HttpSession session, @ModelAttribute("writeForm") 
										@Valid CommunityVO communityVO, Errors errors, HttpServletRequest request) {
		// 원본 글이 자신이 쓴 게 맞는지 체크
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		CommunityVO originalVO = communityService.getOne(id);
		
		if (member.getId() != originalVO.getUserId() ) {
			return "error/404";
		}
		if (errors.hasErrors()) {
			return "redirect:/modify/" + id;
		}
		
		/* 
		 * 여러 경우의 수(본인 글 확인, error 확인) 먼저 check 후 시작
		*	수정 시 유의할 점
		*	0. IP 변경 확인
		*	1. 제목 변경 확인
		*	2. 내용 변경 확인
		*	3. 파일 변경 확인
		*	4. 변경 X 확인
		*/
		
		CommunityVO newCommunity = new CommunityVO(); // 수정된 정보만 newCommunity에 넣기 위해
		newCommunity.setId(originalVO.getId());
		newCommunity.setUserId(originalVO.getUserId()); // 게시글 번호, 이를 작성한 user의 번호 - 필수
		
		boolean isModify = false;
		
		// 1. IP 변경 확인
		String ip = request.getRemoteAddr();
		if ( !ip.equals(originalVO.getRequestIp()) ) {
			newCommunity.setRequestIp(ip);
			isModify = true;
		}
		// 2. 제목 변경 확인
		if ( !originalVO.getTitle().equals(communityVO.getTitle()) ) {
			newCommunity.setTitle(communityVO.getTitle());
			isModify = true;
		}
		// 3. 내용 변경 확인
		if ( !originalVO.getBody().equals(communityVO.getBody()) ) {
			newCommunity.setBody(communityVO.getBody());
			isModify = true;
		}
		// 4. 파일 변경 확인
		if (communityVO.getDisplayFilename().length() > 0) { // check가 되어 있을 경우
			File file = new File("d:/uploadFiles/" + communityVO.getDisplayFilename());
			file.delete();
			communityVO.setDisplayFilename("");
		}
		else { // check가 되어 있지 않을 경우
			communityVO.setDisplayFilename(originalVO.getDisplayFilename()); // file을 그대로 유지
		}
		communityVO.save();
		if ( !originalVO.getDisplayFilename().equals(communityVO.getDisplayFilename()) ) {
			newCommunity.setDisplayFilename(communityVO.getDisplayFilename());
			isModify = true;
		}
		// 5. 변경 X 확인
		if (isModify) {
			// 6. UPDATE 하는 Service code 호출
			communityService.updateCommunity(newCommunity); // 수정한 정보를 저장한 객체 받아오기
		}
		return "redirect:/view/" + id;
	}
	
}
