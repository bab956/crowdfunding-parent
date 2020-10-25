<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="col-sm-3 col-md-2 sidebar">
    <div class="tree">
        <ul style="padding-left:0px;" class="list-group">
            <%--遍历session域中的菜单数据--%>
            <c:forEach items="${sessionScope.parentMenus}" var="parentMenu">
                <li class="list-group-item tree-closed">
                    <c:choose>
                        <%--有子菜单的父菜单--%>
                        <c:when test="${parentMenu.submenus.size() > 0}">
                            <%--icon是菜单的样式、url是点击菜单后跳转的页面(后续还要根据使用情况修改具体路径)、name是菜单的名称--%>
                            <span><i class="${parentMenu.icon}"></i> ${parentMenu.name} <span class="badge"
                                                                                              style="float:right">${parentMenu.submenus.size()}</span></span>
                            <ul style="margin-top:10px;display:none;">
                                <c:forEach items="${parentMenu.submenus}" var="childMenu">
                                    <li style="height:30px;">
                                        <a href="${appPath}/${childMenu.url}"><i
                                                class="${childMenu.icon}"></i> ${childMenu.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <%--没有子菜单的父菜单--%>
                        <c:otherwise>
                            <a href="${appPath}/${parentMenu.url}"><i class="${parentMenu.icon}"></i> ${parentMenu.name}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>