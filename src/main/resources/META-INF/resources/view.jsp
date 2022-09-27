<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />
<portlet:resourceURL var="testAjaxResourceUrl" />
<%
    String backgroundTaskId = (String)request.getAttribute("backgroundTaskId");
%>

<h1>Background Task Id : ${backgroundTaskId}</h1>

<button id="adbc" onclick="ajaxCall()">Get Background Task Status</button>

<script type="text/javascript">
    function ajaxCall(){

        Liferay.Service(
            '/backgroundtask.backgroundtask/get-background-task-status-json',
            {
                backgroundTaskId: ${backgroundTaskId}
            },
            function(obj) {
                console.log(obj);
            }
        );

        AUI().use('aui-io-request', function(A){
            A.io.request('${testAjaxResourceUrl}', {
                method: 'post',
                data: {
                    <portlet:namespace />backgroundTaskId: '${backgroundTaskId}',
                },
                on: {
                    success: function() {
                        alert(this.get('responseData'));
                    }
                }
            });
        });
    }
</script>