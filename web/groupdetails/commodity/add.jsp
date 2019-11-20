<%--
  Created by IntelliJ IDEA.
  User: Janspiry
  Date: 2019/11/7
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Start Page Content -->
<button type="button" onclick="addRecord()" class="btn btn-primary m-b-10 m-l-5">添加商品</button>
<%--<form  role="form" id="page_form" name="page_form" action="add_record">--%>
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <form id="newCommodity" method="post" action="<%=request.getContextPath()%>/Commodity">
                        <input type="hidden" name="action" value="addCommodity">
                        <input type="hidden" name="groupId" value="<%=request.getParameter("group_id")%>">
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label>商品名称</label>
                                    <input type="text" id="add_context" name="context" class="form-control" required>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group">
                                    <label class="control-label">商品积分</label>
                                    <input type="number" id="add_grades" name="grades"  required class="form-control" oninput="value=value.replace(/[^\d]/g,'')">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%--</form>--%>