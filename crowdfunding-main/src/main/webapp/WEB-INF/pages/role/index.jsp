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

        /* 【12】鼠标悬停时显示一个小手(使用onclick时默认只是鼠标的箭头)*/
        a:hover {
            cursor: pointer
        }
    </style>
    <title>角色维护</title>
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
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <%-- 【13】为标签添加一个name属性--%>
                                <input name="keyword" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <%-- 【14】为标签添加一个id属性--%>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <%--【31】为删除按钮添加id属性--%>
                    <button type="button" class="btn btn-danger" id="batchDeletion"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <%--【18】删除默认的单击事件(onclick="window.location.href='form.html'")，添加一个id属性方便后续操作--%>
                    <button type="button" class="btn btn-primary" style="float:right;"
                            id="addBtn"><i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <%--【28】为全选选项添加一个id属性方便后续操作--%>
                                <th width="30"><input type="checkbox" id="checkAll"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <%--【5】为tbody添加一个id方便为其添加内部数据--%>
                            <tbody id="tbody">
                            <%--异步请求获取页面数据--%>
                            </tbody>
                            <tfoot>

                            <tr>
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                        <%--异步请求获取分页导航栏--%>
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

<!-- 【16】添加模态框 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">新增角色</h4>
            </div>
            <div class="modal-body">
                <%--【17】添加角色新增/修改所需的输入框--%>
                <form role="form">
                    <div class="form-group">
                        <label for="exampleInputRoleName">角色名称</label>
                        <input name="name" type="text" class="form-control" id="exampleInputRoleName"
                               placeholder="请输入角色名称">
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <%--【19】为此按钮添加一个id属性用于绑定提交事件--%>
                <button type="button" id="modalSave" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>

<%--引用公共js--%>
<%@ include file="/WEB-INF/pages/common/base_js.jsp" %>
<script type="text/javascript">
    $(function () {
        // 注：此事件属于历史代码
        $(".list-group-item").click(function () {
            if ($(this).find("ul")) {
                $(this).toggleClass("tree-closed");
                // 菜单折叠
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });

        // 【4】调用loadData完成页面数据的加载
        loadData(1);
    });

    // 每页展示的数据量，默认5条
    var pageSize = 2;
    // 查询的关键字，默认为空
    var keyword = "";

    // 【1】添加ajax请求方法$.ajax(xxx)/$.get()/$.post()/$.getJSON()
    function loadData(pageNum) {
        $.getJSON("${appPath}/role/loadData",
            {"pageNum": pageNum, "pageSize": pageSize, "keyword": keyword},
            function (retMsg) {
                // 只判断值，所以只需要==如果是3个等号将会出现问题
                if (retMsg == "403") {
                    layer.msg("您没有权限访问这个页面，请联系管理员开通", {time: 2000, shift: 6, icon: 3})
                } else {
                    // 【4】将异步请求后服务器异步响应的数据(角色信息,和分页信息)传递给对应的加载函数，分别根据各的加载逻辑进行处理
                    loadRole(retMsg.list);
                    loadPage(retMsg)
                }
            });
    }

    // 【2】添加异步加载角色数据的方法
    function loadRole(roleList) {
        // 【6】遍历list中的所有数据并拼接为一个大字符串
        var content = "";
        for (var i = 0; i < roleList.length; i++) {
            content += '<tr>';
            content += '	<td>' + (i + 1) + '</td>';
            // 【29】为标签添加一个class属性方便后续操作【32】为标签添加一个id属性方便批量选中时进行判断
            content += '	<td><input type="checkbox" class="checkRole" id="' + (roleList[i].id) + '"></td>';
            content += '	<td>' + (roleList[i].name) + '</td>';
            content += '	<td>';
            content += '		<button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>';
            // 【20】为修改按钮添加一个单击事件,调用函数并将id传过去完成修改操作,如果需要传递多个参数onclick="updateRole('+ (roleList[i].id + ',' + 123) +')"
            content += '		<button type="button" onclick="getRole(' + (roleList[i].id) + ')" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>';
            content += '		<button type="button" onclick="deleteItem(' + (roleList[i].id) + ')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
            content += '	</td>';
            content += '</tr>';

        }
        // 【7】将拼接的标签放到tbody标签中
        $("#tbody").html(content);
    }

    // 【3】添加异步加载角色分页的方法
    function loadPage(pageInfo) {
        // 查询到的数据页数大于1页才展示下面的分页导航
        if (pageInfo.navigatepageNums.length > 1) {
            // 拼接页码导航栏
            var content = "";
            // 【8】拼接上一页
            if (pageInfo.isFirstPage) {
                content += '<li class="disabled"><a>上一页</a></li>';
            } else {
                // 去掉href因为不能使用同步的方式进行访问，将其改为一个事件进行调用上面的loadData进行异步加载(因为函数在字符串中给其传递的参数需要拼接，不使用括号括起来会有问题)
                content += '<li><a onclick="loadData(' + (pageInfo.pageNum - 1) + ')">上一页</a></li>';
            }

            // 【9】所有导航页号(所谓的逻辑分页)
            var allPageNum = pageInfo.navigatepageNums;
            for (var i = 0; i < allPageNum.length; i++) {
                if (allPageNum[i] === pageInfo.pageNum) {
                    content += '<li class="active"><a onclick="loadData(' + (allPageNum[i]) + ')">' + allPageNum[i] + ' <span class="sr-only">(current)</span></a></li>';
                } else {
                    content += '<li><a onclick="loadData(' + (allPageNum[i]) + ')">' + allPageNum[i] + '</a></li>';
                }

            }
            // 【10】拼接下一页
            if (pageInfo.isLastPage) {
                content += '<li class="disabled"><a>下一页</a></li>';
            } else {
                // 去掉href因为不能使用同步的方式进行访问，将其改为一个事件进行调用上面的loadData进行异步加载
                content += '<li><a onclick="loadData(' + (pageInfo.pageNum + 1) + ')">下一页</a></li>';
            }
            // 【11】将拼接的标签放到页面中
            $(".pagination").html(content);
        } else {
            $(".pagination").html("");
        }
    }

    // 【15】查询按钮的单击事件
    $("#searchBtn").click(function () {
        // 对全局变量keyword进行赋值
        keyword = $("input[name='keyword']").val();
        // 调用上面的loadData，默认查询第一页(现在他也能拿到keyword的值)
        loadData(1)
    });

    // 【18】添加新增按钮的单击事件
    $("#addBtn").click(function () {
        // 【25】每次点击新增后清除上一次回显的内容，避免新增时时还保留着上一次回显的内容【不能放在修改成功后的回调函数里，因为可能存在点击了修改但是没有修改直接关闭了窗口】
        $("#exampleInputRoleName").val("");
        // 【26】将点击修改时加入的roleId属性置空以免上一次点了修改保留了roleId属性【不能放在修改成功后的回调函数里，因为可能存在点击了修改但是没有修改直接关闭了窗口】
        $("#modalSave").attr("roleId", "");
        // 【27】将模态框标题置为新增角色，以免上一次点了修改保留了此属性【不能放在修改成功后的回调函数里，因为可能存在点击了修改但是没有修改直接关闭了窗口】
        $("#myModalLabel").text("新增角色");
        // 弹出id为myModal的模态框
        $("#myModal").modal({backdrop: 'static'})
    });

    // 【19】添加模态框保存按钮的单击事件
    $("#modalSave").click(function () {
        // 获取输入框中输入的值
        var roleName = $("#exampleInputRoleName").val();
        var roleId = $("#modalSave").attr("roleId");
        // 【23】根据roleId判断是新增还是修改(新增时没有roleId)
        if (roleId === null || roleId === "") {
            $.getJSON("${appPath}/role/saveRole", {"name": roleName}, function (retMsg) {
                // 判断返回的结果中是否包含pageNum属性，如果有
                if (retMsg.status === "success") {
                    // 新增成功后的善后工作
                    layer.msg("新增成功", {icon: 6, time: 1000}, function () {
                        // 每次新增成功后清除上一次输入的内容，避免下一次新增时还保留着上一次输入的内容
                        $("#exampleInputRoleName").val("");
                        // 隐藏模态框
                        $("#myModal").modal("hide");
                        // 让服务端返回一个当前数据的最大页数用于跳转到最后一页
                        loadData(retMsg.pages);
                    })
                } else {
                    // 让其返回一个错误信息用于提示用户
                    layer.msg("新增失败:" + retMsg.errorMsg, {icon: 5, time: 1000});
                }
            })
        } else {
            // 【24】保存修改的角色信息
            $.getJSON("${appPath}/role/updateRole", {"id": roleId, "name": roleName}, function (retMsg) {
                if (retMsg.status === "success") {
                    // 如果修改成功则进行善后工作
                    layer.msg("修改成功", {icon: 6, time: 1000}, function () {
                        // 隐藏模态框
                        $("#myModal").modal("hide");
                        // 让服务端返回一个当前所在的页数用于跳转到当前页
                        loadData(retMsg.pageNum);
                    })
                } else {
                    // 让其返回一个错误信息用于提示用户
                    layer.msg("修改失败:" + retMsg.errorMsg, {icon: 5, time: 1000});
                }
            })
        }
    });

    // 【21】查询角色信息，用于回显
    function getRole(roleId) {
        $.getJSON("${appPath}/role/getRoleInfo", {"id": roleId}, function (roleInfo) {
            // 以id查询到数据进行回显
            $("#exampleInputRoleName").val(roleInfo.name);
            // 弹出模态框
            $("#myModal").modal({backdrop: 'static'})
            // 修改模态框的title
            $("#myModalLabel").text("修改角色");
            // 【22】添加一个roleId用于判断保存时是进行的新增操作还是修改操作，而且可以给修改时使用
            $("#modalSave").attr("roleId", roleId)
        })
    }

    // 【30】为全选/全不选添加单击事件
    $("#checkAll").click(function () {
        var $selectAll = $("#checkAll");
        var $checkRole = $(".checkRole");
        // 遍历当前页面的所有角色数据，进行全选或全不选
        for (var i = 0; i < $checkRole.length; i++) {
            // 如果selectAll[0].checked为true则所有数据也就为true(被选中)，反之没有被选中
            $checkRole[i].checked = $selectAll[0].checked;
        }
    });

    // 【33】完成批量删除
    $("#batchDeletion").click(function () {
        // 获取id为tbody标签下class为checkRole且被选中的元素
        var $checkRole = $("#tbody .checkRole:checked");
        if ($checkRole.length > 0) {
            layer.confirm("确定要删除被选中的所有角色信息吗", {icon: 3, shift: 6, title: '提示信息'}, function () {
                var ids = new Array();
                for (var i = 0; i < $checkRole.length; i++) {
                    // 将得到的元素取出id放到arr中，因为遍历出来的元素属于DOM对象，attr属于jQuery对象的方法所以还需要包一层$()
                    ids.push($($checkRole[i]).attr("id"));
                }
                deleteCoreCode(ids)
            })
        } else {
            layer.msg("没有选中任何角色信息")
        }
    });

    // 【34】将删除代码进行抽取
    function deleteCoreCode(deleteIdList) {
        // 调用接口删除角色，注意传递的是一个数组只能拼接的方式传过去如果是以json传递将会把中括号也传过去
        $.getJSON("${appPath}/role/deleteRole?ids=" + deleteIdList, null, function (retMsg) {
            if (retMsg.status === "success") {
                // 如果修改成功则进行善后工作
                layer.msg("删除成功", {icon: 6, time: 1000}, function () {
                    // 将全新框置为未选中，同步的时候会刷新页面所以无需关注这个问题
                    $("#checkAll").attr("checked", false);
                    // 让服务端返回一个当前所在的页数用于跳转到当前页
                    loadData(retMsg.pageNum);
                })
            } else {
                // 让其返回一个错误信息用于提示用户
                layer.msg("删除失败:" + retMsg.errorMsg, {icon: 5, time: 1000});
            }
        })
    }

    // 【35】单行删除
    function deleteItem(roleId) {
        layer.confirm("确定要删除该角色信息吗", {icon: 3, title: '确认提示'}, function () {
            deleteCoreCode(roleId);
        })
    }
</script>
</body>
</html>
