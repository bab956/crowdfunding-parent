package xyz.newtouch.crowdfunding;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author weibing
 */
public class AppStartUpListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 获取上下文信息
        ServletContext servletContext = servletContextEvent.getServletContext();
        // 当前项目的路径，注意不是到/，是启动时的那个工程路径
        String appPath = servletContext.getContextPath();
        servletContext.setAttribute("appPath", appPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
