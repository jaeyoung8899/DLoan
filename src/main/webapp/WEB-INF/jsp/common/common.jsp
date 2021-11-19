<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<c:if test="${path eq 'lib'}">
<link rel="shortcut icon" href="${ctx}/resources/library/images/icon_library_32.ico" type="image/x-icon">
</c:if>
<c:if test="${path eq 'store'}">
<link rel="shortcut icon" href="${ctx}/resources/store/images/icon_bookstore_32.ico" type="image/x-icon">
</c:if>

<link rel="stylesheet" href="${ctx}/libs/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" href="${ctx}/libs/jquery-ui/jquery-ui.min.css" >
<link rel="stylesheet" href="${ctx}/libs/HoldOn/HoldOn.min.css" >

<style type="text/css">
<%-- 로딩바 디자인 --%>
#holdon-overlay { z-index: 19999; filter:alpha(opacity=30); opacity:.3 }
<%-- 달력 디자인 --%>
.ui-datepicker select.ui-datepicker-year  { font-size: 13px; height: 24px; }
.ui-datepicker select.ui-datepicker-month { font-size: 13px; height: 24px; }
.ui-datepicker .ui-datepicker-buttonpane button { font-size: 13px; opacity: 1; font-weight: normal; }
.ui-datepicker-trigger { margin-left:5px; vertical-align:middle; }
.ui-datepicker-calendar > thead th.ui-datepicker-week-end:first-child { color: #c85032; }
.ui-datepicker-calendar > tbody td.ui-datepicker-week-end:first-child a { color: #c85032; }
.ui-datepicker-calendar > thead th.ui-datepicker-week-end:last-child { color: #466482; }
.ui-datepicker-calendar > tbody td.ui-datepicker-week-end:last-child a { color: #466482; }
@media print {
	* {
		-webkit-print-color-adjust: exact;
	}
}
</style>

<script type="text/javascript" src="${ctx}/libs/jquery/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${ctx}/libs/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/libs/jquery.form/jquery.form.min.js"></script>
<script type="text/javascript" src="${ctx}/libs/json2/json2.js"></script>
<script type="text/javascript" src="${ctx}/libs/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="${ctx}/libs/bootstrap/bootstrap.js"></script>

<script type="text/javascript">
var _ctx  = "${ctx}";
var _path = "${path}";
</script>
<script type="text/javascript" src="${ctx}/resources/js/common.js?v=${version }"></script>