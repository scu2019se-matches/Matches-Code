<%--
  Created by IntelliJ IDEA.
  User: Janspiry
  Date: 2019/11/7
  Time: 13:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row">
    <div class="col-lg-6">
        <div class="card">
            <div class="form-group">
                <p class="text-muted m-b-15 f-s-12">商品名称</p>
                <input type="text" id="context" name="context" class="form-control input-focus" placeholder="输入商品名称">
            </div>
        </div>
    </div>
    <div class="col-lg-6">
        <div class="card">
            <div class="form-group">
                <p class="text-muted m-b-15 f-s-12">分数</p>
                <input type="text" id="grades" name="grades" class="form-control input-focus" placeholder="输入商品分数">
            </div>
        </div>
    </div>

</div>
<button type="button" onclick="searchRecord()" class="btn btn-default btn-flat m-b-10">查询</button>
