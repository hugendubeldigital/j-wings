<template name="PasswordField" for="org.wings.SPasswordField">
<%@ include file="common.plaf">
<input type="password" size="<%?columns%>" maxlength="<%?maxColumns%>" \
<% if(!component.isEditable()) { %>readonly="1" <%}%>\
<% if(component.isEnabled())   { %>name="<%?namePrefix%>" <%}%>\
<% printStyle(<%?style%>) %>\
value="<%?text%>"/>
</template>