package com.example.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.dao.IMemberDao;
import com.example.board.dto.MemberDto;

@Controller
public class MemberController {
	@Autowired
	IMemberDao memberDao;
	
	// 로그인
	@RequestMapping("/login")
	public String login(@ModelAttribute("member") MemberDto member, HttpSession session, Model model) {
	   System.out.println("아이디 들어옴?" + member);
	   MemberDto UserInfo = memberDao.getUserIdAndPw(member.getUser_id(), member.getUser_pw());
	   
	   if(UserInfo != null) {
	      // 로그인에 성공하면 사용자 아이디를 세션 변수에 저장한다.
	      // loggedInUser 객체에서 userId 정보를 추출하고, 세션에 userId라는 이름으로 저장
	      // listForm.jsp에서 getAttribute로 가져옴
	      session.setAttribute("user_id", UserInfo.getUser_id());
	      session.setAttribute("non_user_pw", UserInfo.getUser_pw());
	      System.out.println("세션 user_id : " + UserInfo.getUser_id());
	      
	      // 세션 유지 시간 설정
	      session.setMaxInactiveInterval(60);
	      
	      System.out.println("로그인 성공");
	      return "redirect:listForm";
	      
	   } else {
	      // 아이디가 존재하지 않는 경우를 확인하기 위해 getUserId 메서드 사용
	      MemberDto user = memberDao.getUserId(member.getUser_id());
	         if(user != null) {
	            System.out.println("패스워드가 틀림");
	             model.addAttribute("loginFailed", "wrongPw");
	         } else {
	            System.out.println("아이디가 존재하지 않음");
	            model.addAttribute("loginFailed", "nonExistId");
	            // model.addAttribute("wrongId", true);
	         }
	      System.out.println("로그인 실패");
	      return "login";
	      }
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_id");
		System.out.println("로그아웃");
		return "redirect:listForm";
	}
}

	

