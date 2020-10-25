<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <%--引用公共css--%>
    <%@ include file="/WEB-INF/pages/common/base_css.jsp" %>
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }
    </style>
    <title>修改用户</title>
</head>

<body>

<%--引用公共顶部导航--%>
<%@ include file="/WEB-INF/pages/common/nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%--引用左侧菜单栏--%>
        <%@ include file="/WEB-INF/pages/common/sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">修改</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <%--显示修改时出现的异常信息--%>
                <c:if test="${not empty requestScope.updateUserErrorMsg}">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        <span class="sr-only">Error:</span>
                            ${requestScope.updateUserErrorMsg}
                    </div>
                </c:if>
                <div class="panel-body">
                    <%--添加请求地址、请求方式、form的id--%>
                    <form id="updateUserForm" role="form" action="${appPath}/admin/updateUser" method="post">
                        <%--隐藏域，提交时明确需要修改的用户id，直接使用param获取因为从user.jsp跳转过来时携带了此参数--%>
                        <input type="hidden" name="id" value="${requestScope.updateUserInfo.id}">
                        <div class="form-group">
                            <label for="exampleInputPassword1">登陆账号</label>
                            <%--添加name属性，添加value属性用于回显需要修改的用户信息--%>
                            <input name="loginacct" value="${requestScope.updateUserInfo.loginacct}" type="text"
                                   class="form-control" id="exampleInputPassword1" placeholder="修改后的登陆账号">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword2">用户名称</label>
                            <%--添加name属性，添加value属性用于回显需要修改的用户信息--%>
                            <input name="username" value="${requestScope.updateUserInfo.username}" type="text"
                                   class="form-control" id="exampleInputPassword2" placeholder="修改后的用户名称">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">邮箱地址</label>
                            <%--添加name属性，添加value属性用于回显需要修改的用户信息--%>
                            <input name="email" value="${requestScope.updateUserInfo.email}" type="email"
                                   class="form-control" id="exampleInputEmail1" placeholder="修改后的邮箱地址">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <%--添加单击事件,提交表单--%>
                        <button type="button" class="btn btn-success" onclick="$('#updateUserForm').submit()"><i
                                class="glyphicon glyphicon-edit"></i> 修改
                        </button>
                        <%--添加单击事件,重置表单--%>
                        <button type="button" class="btn btn-danger"
                                onclick="document.getElementById('updateUserForm').reset()"><i
                                class="glyphicon glyphicon-refresh"></i> 重置
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">帮助</h4>
            </div>
            <div class="modal-body">
                <div class="bs-callout bs-callout-info">
                    <h4>填写说明</h4>
                    <p>各字段均为必填项</p>
                </div>
            </div>
        </div>
    </div>
</div>
<%--引用公共js--%>
<%@ include file="/WEB-INF/pages/common/base_js.jsp" %>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function () {
            if ($(this).find("ul")) {
                $(this).toggleClass("tree-closed");
                // 菜单伸缩
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
    });
</script>
</body>
</html>
