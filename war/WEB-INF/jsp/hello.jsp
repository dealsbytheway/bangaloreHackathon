<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head>
	<title>Hello :: Spring Application</title>
	<link type="text/css" href="<c:url value='/resources/css/bootstrap.css'/>" rel="stylesheet"/>
  </head>
  <body>
    <h1>Hello - Spring Application</h1>
    <p>Greetings, it is now <c:out value="${now}"/></p>
  </body>
</html>