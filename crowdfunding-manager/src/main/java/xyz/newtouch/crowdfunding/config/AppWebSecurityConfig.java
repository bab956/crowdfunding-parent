package xyz.newtouch.crowdfunding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author weibing
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 用于用户详情查询服务组件的接口
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 授权
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置资源放行,此部分资源可以被任何人查看
        http.authorizeRequests().antMatchers("/login.jsp", "/static/**").permitAll()

                // 剩余的其他资源必须要经过身份认证才能访问
                .anyRequest().authenticated();
        // 指定webapp路径下的login.jsp为登录页面src/main/webapp/index.jsp
        http.formLogin().loginPage("/login.jsp");
        // 使用springsecurity提供的默认login控制器，如果客户端传递的参数名称非username和password则需要通过该下面的方式进行指定
        http.formLogin().loginProcessingUrl("/login")
                // 指定提交表单时username和password对应的参数key
                .usernameParameter("loginacct").passwordParameter("userpswd")
                // 指定验证成功后需要跳转的控制器，如果验证成功则跳转到AdminController中的main接口
                .defaultSuccessUrl("/admin/main");

        // 登出后徐亚跳转到的目标页面
        http.logout().logoutSuccessUrl("/login.jsp");

        // 没有权限时需要展示的页面
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            // 如果请求头中包含XMLHttpRequest则是ajax请求，对ajax请求做特殊处理，方便web端给出用户提示
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.getWriter().write("403");
            } else {
                // 直接将系统抛出的异常返回给客户端，也可以自己指定一个其他异常信息，这样就可以在前端获取到
                request.setAttribute("serverErrorMsg", accessDeniedException.getMessage());
                // 指定跳转的页面和跳转方式
                request.getRequestDispatcher("/WEB-INF/pages/error/unauthorized.jsp").forward(request, response);
            }
        });

        // 记住我
        http.rememberMe();
    }

    /**
     * 认证信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 根据用户名查询出用户的详细信息,在后面调用passwordEncoder指定加密规则
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}