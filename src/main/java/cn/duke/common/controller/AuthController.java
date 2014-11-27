package cn.duke.common.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.duke.common.Constant;
import cn.duke.common.UserInfo;
import cn.duke.common.dao.IUserInfoDao;

@Controller
public class AuthController {

	@Resource
	private IUserInfoDao userInfoDao;

	@RequestMapping(value = "/login.do")
	public ModelAndView login(String userName, String password, HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		UserInfo user = userInfoDao.getUserInfo(userName);
		if (user == null || !user.getPassword().equals(password)) {
			ModelAndView mv = new ModelAndView("loginView");
			mv.addObject("userName", userName);
			mv.addObject("loginError", true);
			return mv;
		} else {
			session.setAttribute(Constant.USER_ENTITY, user);
			try {
				httpServletResponse.sendRedirect(user.getMainPage());
			} catch (IOException e) {
			}
			return null;
		}
	}

	@RequestMapping(value = "/logout.do")
	public String logout(HttpSession session) {
		session.removeAttribute(Constant.USER_ENTITY);
		return "redirect:toLoginView.do";
	}

	@RequestMapping(value = "/toLoginView.do")
	public ModelAndView toLoginView() {
		ModelAndView mv = new ModelAndView("loginView");
		return mv;
	}

	@RequestMapping(value = "/toMainView.do")
	public String toMainView(HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(Constant.USER_ENTITY);
		String mainPage = userInfo.getMainPage();
		return "redirect:" + mainPage;
	}
}
