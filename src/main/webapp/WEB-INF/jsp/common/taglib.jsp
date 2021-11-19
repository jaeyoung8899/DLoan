<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="kaitUi"   uri="/tlds/kait-taglib" %>

<c:set var="ctx"     value="${pageContext.request.contextPath}" />
<c:set var="version" value="1.2.999994" />
<c:set var="cssver"  value="1.2.999994" />
<c:choose>
	<c:when test="${fn:startsWith(requestScope['javax.servlet.forward.request_uri'], '/lib')}">
		<c:set var="path" value="lib" />
	</c:when>
	<c:when test="${fn:startsWith(requestScope['javax.servlet.forward.request_uri'], '/store')}">
		<c:set var="path" value="store" />
	</c:when>
	<c:otherwise>
		<c:set var="path" value="user" />
	</c:otherwise>
</c:choose>