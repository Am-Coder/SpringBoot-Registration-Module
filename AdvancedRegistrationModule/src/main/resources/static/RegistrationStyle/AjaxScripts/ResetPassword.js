/**
 * 
 */

/**
 * 
 */
$(document).ready(
		function(){
            $("#resetPasswordForm").submit(function myfun(event){
            	event.preventDefault();//prevents default form submission  
                $.ajax({
                    type : "POST",
                    cache: false,
                    contentType : "application/json",//type of data being send to server
                    url : "/reset",
                    data : JSON.stringify({token: $("#token").val(),
                    	password: $("#password").val()
                    }),
                    
                    dataType : 'text',//result expected from server
                					//with json return type we can return java objects
                    				//With text we can return String from java conroller
                    timeout : 100000,
                    success : function(data) {
                    	$("#resetStatus").html(data);

                        console.log("SUCCESS: ", data);
                    },
                    error : function(e) {
                        console.log("ERROR: ", e);
                    },
                    done : function(e) {
                        console.log("DONE");
                    }
                });
            }
        )
		}

)