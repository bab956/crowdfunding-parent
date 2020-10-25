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

        table tbody tr:nth-child(odd) {
            background: #F4F4F4;
        }

        table tbody td:nth-child(even) {
            color: #C00;
        }
    </style>
    <title>用户维护</title>
</head>

<body>

<%--引用公共顶部导航--%>
<%@ include file="/WEB-INF/pages/common/nav.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%--引用左侧菜单栏--%>
        <%@ include file="/WEB-INF/pages/common/sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form id="queryUserForm" class="form-inline" role="form" style="float:left;" action="${appPath}/admin/user" method="post">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <%--添加name属性否则不会携带页面输入的参数，添加value参数拿到提交前输入的内容(注意：如果请求后是重定向此方式则拿不到跳转前的数据，除非是在请求前传到服务器再set到域中)--%>
                                <input name="keyword" value="${param.keyword}" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <%--点击后直接对form进行提交操作--%>
                        <button type="button" onclick="$('#queryUserForm').submit()" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" class="btn btn-danger" id="batchDeletion" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" class="btn btn-primary" style="float:right;"
                            onclick="window.location.href='${appPath}/admin/addPage'"><i class="glyphicon glyphicon-plus"></i> 新增
                    </button>

                    <br>
                    <hr style="clear:both;">
                    <%--显示查询时出现的异常信息--%>
                    <c:if test="${not empty requestScope.queryUserErrorMsg}">
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                            <span class="sr-only">Error:</span>
                                ${requestScope.serverErrorMsg}
                        </div>
                    </c:if>
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox" id="checkAll"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--遍历所有用户信息，list是PageInfo中存储结果集的list集合--%>
                            <c:forEach items="${sessionScope.pageInfo.list}" var="user" varStatus="status">
                                <tr>
                                        <%--遍历时当前数据的索引值--%>
                                    <td>${status.count}</td>
                                    <td><input type="checkbox" class="userCheckBox" value="${user.id}"></td>
                                    <td>${user.loginacct}</td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>
                                        <button type="button" class="btn btn-success btn-xs"><i
                                                class=" glyphicon glyphicon-check"></i></button>
                                        <%--跳转到编辑页面，跳转时需要将id传给服务端用于查询用户信息回显--%>
                                        <button type="button" onclick="window.location.href='${appPath}/admin/editPage?id=${user.id}'" class="btn btn-primary btn-xs"><i
                                                class=" glyphicon glyphicon-pencil"></i></button>
                                        <%--添加一个id用于绑定事件--%>
                                        <button type="button" onclick="delConfirm(${user.id})" class="btn btn-danger btn-xs"><i
                                                class=" glyphicon glyphicon-remove"></i></button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                        <c:choose>
                                            <%--如果不是第一页可以点击--%>
                                            <c:when test="${not sessionScope.pageInfo.isFirstPage}">
                                                <li>
                                                    <%--添加keyword=${param.keyword}是为了携带查询条件--%>
                                                    <a href="${appPath}/admin/user?keyword=${param.keyword}&currentPageNum=${sessionScope.pageInfo.firstPage}">首页</a>
                                                </li>
                                                <li>
                                                    <a href="${appPath}/admin/user?keyword=${param.keyword}&currentPageNum=${sessionScope.pageInfo.pageNum-1}">上一页</a>
                                                </li>
                                            </c:when>
                                            <%--是第一页禁止点击--%>
                                            <c:otherwise>
                                                <li class="disabled"><a>上一页</a></li>
                                            </c:otherwise>
                                        </c:choose>

                                        <%--遍历所有逻辑页数--%>
                                        <c:forEach items="${sessionScope.pageInfo.navigatepageNums}" var="i">
                                            <c:choose>
                                                <%--如果被遍历到的条目是当前页面则高亮展示，pageInfo中的pageNum代表当前所处的页面--%>
                                                <c:when test="${i == sessionScope.pageInfo.pageNum}">
                                                    <li class="active"><a
                                                            href="${appPath}/admin/user?keyword=${param.keyword}&currentPageNum=${i}">${i} <span
                                                            class="sr-only">(current)</span></a></li>
                                                </c:when>
                                                <%--如果被遍历到的条目不是当前页则展示为一个普通的样式--%>
                                                <c:otherwise>
                                                    <li><a href="${appPath}/admin/user?keyword=${param.keyword}&currentPageNum=${i}">${i}</a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <c:choose>
                                            <%--如果不是最后一页可以点击--%>
                                            <c:when test="${not sessionScope.pageInfo.isLastPage}">
                                                <li>
                                                    <a href="${appPath}/admin/user?keyword=${param.keyword}&currentPageNum=${sessionScope.pageInfo.pageNum+1}">下一页</a>
                                                </li>
                                                <li>
                                                    <a href="${appPath}/admin/user?keyword=${param.keyword}&currentPageNum=${sessionScope.pageInfo.lastPage}">尾页</a>
                                                </li>
                                            </c:when>
                                            <%--是最后一页禁止点击--%>
                                            <c:otherwise>
                                                <li class="disabled"><a>下一页</a></li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
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
                // 点击后隐藏显示菜单的内容
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });

        // 全选/全不选框
        var checkAll = $("#checkAll");

        // 当前页面所有数据对应的选择框
        var userCheckBox = $(".userCheckBox");
        // 遍历所有checkBox(得到的结果全部都是DOM对象，也可以使用js提供的常规遍历方式进行遍历)
        // 通过checkAll[0]或checkAll.get(0)都可以得到checkAll的DOM对象
        checkAll.click(function () {
            $.each(userCheckBox, function (index, checkBox){
                // 对checkBox赋值确定是否被选中，checkAll[0].checked是判断checkAll是否被选中，如果被选中checkBox.checked就会被赋值为true，反之赋值为false
                checkBox.checked = checkAll[0].checked;
            })
        })

        // 批量删除
        $("#batchDeletion").click(function () {
            // 被选中的所有选项
           const checkBoxs = $(".userCheckBox:checked");
           if (checkBoxs.length === 0) {
               layer.msg("没有选择任何需要删除的用户", {icon: 2, shift: 6});
           } else {
               layer.confirm("确认要删除选中的所有用户吗", {icon: 2, title: "提示信息", shift: 6}, function () {
                   const ids = new Array();
                   for (var i = 0; i < checkBoxs.length; i++) {
                       ids.push($(checkBoxs[i]).val());
                   }
                   // 将遍历得到的所有id传给服务端【以1,2,3,4,5的格式传递给服务端】
                   location.href="${appPath}/admin/batchDeletion?ids=" + ids;
               })
           }
        })
    });

    // 单行删除提示
    function delConfirm(delUserId) {
        layer.confirm("确认要删除吗", {icon: 3, title: "确认提示"}, function () {
            window.location.href="${appPath}/admin/delUserItem?id=" + delUserId;
        })
    }
</script>
</body>
</html>
