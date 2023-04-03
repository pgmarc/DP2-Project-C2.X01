<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.enrolment.list.label.code" path="code"/>
	<acme:list-column code="authenticated.enrolment.list.label.motivation" path="motivation"/>
	<acme:list-column code="authenticated.enrolment.list.label.goals" path="goals"/>
	<acme:list-column code="authenticated.enrolment.list.label.workTime" path="workTime"/>

</acme:list>

<acme:button code="authenticated.enrolment.form.button.create" 
action="/authenticated/enrolment/create"/>