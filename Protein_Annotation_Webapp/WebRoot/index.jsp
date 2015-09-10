<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>PAWS home page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    
    All submit and get web services will return the following top level properties:
    <br>
    <br>
    <ul>
	    <li>
	    pawsStatus - the status of running the job on the module.  Returned values:  (See Java class AnnotationDataRunStatusConstants)
	    <ul>
		    <li>	
		    	complete - processing completed successfully and the data is available
		    </li>
		    <li>	  
		    	fail - processing failed
		    </li>
		    <li>	
		    	submitted - the request has been submitted and is waiting to be processed
		    </li>
		    <li>
		    	no_record - indicates that there is no record for the provided parameters
		    </li>
	    			
		    </li>
	    </ul>	    			
	    </li>
	    <li>
	    sequenceId - the id assigned for the sequence passed in in submit.
	    </li>
	    <li>
	    data - only exists if the pawsStatus is complete.
	    </li>
    </ul>
    <br>
    <br>
    The WADL for the web services is available at:
    <br>
    
    <a href="/paws/services/application.wadl" target="_blank">
    /paws/services/application.wadl
    </a>
    
  </body>
</html>
