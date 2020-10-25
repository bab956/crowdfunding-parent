<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">

    <%@ include file="/WEB-INF/pages/common/base_css.jsp"%>
    <style>

    </style>
    <title>用户登录</title>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">众筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">

    <form id="loginForm" class="form-signin" role="form" action="${appPath}/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户登录</h2>
        <p style="font-size: 10px; color: red;">${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
        <c:if test="${not empty requestScope.loginErrorMsg}">
            <div class="alert alert-danger" role="alert">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only">Error:</span>
                    ${loginErrorMsg}
            </div>
        </c:if>

        <div class="form-group has-success has-feedback">
            <input type="text" name="loginacct" class="form-control" id="inputSuccess4" placeholder="请输入登录账号" autofocus>
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="text" name="userpswd" class="form-control" id="inputSuccess5" placeholder="请输入登录密码" style="margin-top:10px;">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <%--<div class="form-group has-success has-feedback">
            <select class="form-control" >
                <option value="member">会员</option>
                <option value="user" selected>管理</option>
            </select>
        </div>--%>
        <div class="checkbox">
            <label>
                <%--添加name属性是为了使用权限框架提供的启用记住我功能(cookie版)--%>
                <input type="checkbox" name="remember-me" value="remember-me"> 记住我
            </label>
            <br>
            <label>
                忘记密码
            </label>
            <label style="float:right">
                <a href="reg.html">我要注册</a>
            </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="dologin()" > 登录</a>
    </form>
</div>

<%--引用公共js--%>
<%@ include file="/WEB-INF/pages/common/base_js.jsp"%>
<script>
    function dologin() {
        // 只要单击登录按钮就提交这个form表单
        $("#loginForm").submit();
    }
</script>
</body>
</html>