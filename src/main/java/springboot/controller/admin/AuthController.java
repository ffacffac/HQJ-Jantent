package springboot.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springboot.constant.WebConst;
import springboot.controller.AbstractController;
import springboot.controller.helper.ExceptionHelper;
import springboot.dto.LogActions;
import springboot.exception.TipException;
import springboot.modal.bo.RestResponseBo;
import springboot.modal.vo.UserVo;
import springboot.service.ILogService;
import springboot.service.IUserService;
import springboot.util.Commons;
import springboot.util.MyUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录控制
 *
 * @author tangj
 * @date 2018/1/21 14:07
 * <p>
 * **：@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
 * 1) 如果只是使用@RestController注解Controller，则Controller中的方法无法返回jsp页面或者html，
 * 配置的视图解析器 InternalResourceViewResolver不起作用，返回的内容就是Return 里的内容。
 * 2) 如果需要返回到指定页面，则需要用 @Controller配合视图解析器InternalResourceViewResolver才行。
 * 3) 如果需要返回JSON，XML或自定义mediaType内容到页面，则需要在对应的方法上加上@ResponseBody注解。
 * 1.使用@Controller 注解，在对应的方法上，视图解析器可以解析return 的jsp,html页面，并且跳转到相应页面
 * 若返回json等内容到页面，则需要加@ResponseBody注解
 */
@Controller
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class AuthController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Resource
    private IUserService userService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "/login")
    public String login() {
        return "admin/login";
    }

    @PostMapping(value = "login")
    @ResponseBody
    public RestResponseBo doLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        Integer error_count = cache.get("login_error_count");
        try {
//            UserVo userVo = userService.login(username, password);//原来的
            UserVo userVo = userService.userLogin(username, password);//自己的
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, userVo);
            // 设置12小时的cookie
            MyUtils.setCookie(response, userVo.getUid());
            logService.insertLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), userVo.getUid());
        } catch (Exception e) {
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return RestResponseBo.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count", error_count, 10 * 60);
            String msg = "登录失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        try {
            response.sendRedirect(Commons.site_login());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("注销失败", e);
        }
    }
}
