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
    <%@ include file="/WEB-INF/pages/common/base_css.jsp"%>
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        .tree-closed {
            height : 40px;
        }
        .tree-expanded {
            height : auto;
        }
    </style>
    <title>控制面板</title>
</head>

<body>

<%--引用公共顶部导航--%>
<%@ include file="/WEB-INF/pages/common/nav.jsp"%>
<div class="container-fluid">
    <div class="row">

        <%@ include file="/WEB-INF/pages/common/sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 style="color: red; text-align: center">您没有权限访问这个页面，请联系管理员开通</h1>
        </div>
    </div>
</div>

<%--引用公共js--%>
<%@ include file="/WEB-INF/pages/common/base_js.jsp"%>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
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
