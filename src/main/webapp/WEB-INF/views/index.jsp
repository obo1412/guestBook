<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!doctype html>
<head>
	<meta charset="utf-8" />
	<title>Home</title>
</head>
<body>

	<P>  이번 행사의 이름을 적어주세요 </P>
	<form method='get'
		action='${pageContext.request.contextPath}/event_make.do'
		style="width: 300px;">
		<div class="input-group input-group-sm">
			<input type="text" name='event-name' id='event-name'
				class="form-control form-control-sm" placeholder="검색" value="${keyword}" />
			<span class="input-group-append">
				<button id="search-submit" class="btn btn-sm btn-success" type="submit">
					결정
				</button>
			</span>
			
			<div>
				<c:forEach var="event" items="${eventList}" varStatus="status" >
					<button>${event.nameEvent}</button>
				</c:forEach>
			</div>
		</div>
	</form>

</body>
</html>
