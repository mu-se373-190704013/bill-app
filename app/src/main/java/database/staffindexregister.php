<?php
header("Access-Control-Allow-Origin: *"); 
require_once 'connection.php';  
  $response = array();  
     
    if(isTheseParametersAvailable(array('username','tc','password',))){  
    $username = $_GET['username'];   
    $tc = $_GET['tc'];   
    $password = md5($_GET['password']);  
  
   
    $stmt = $conn->prepare("SELECT id FROM staff WHERE username = ? OR tc = ?");  
    $stmt->bind_param("ss", $username, $tc);  
    $stmt->execute();  
    $stmt->store_result();  
   
    if($stmt->num_rows > 0){  
        $response['error'] = true;  
        $response['message'] = 'User already registered';  
        $stmt->close();  
    }  
    else{  
        $stmt = $conn->prepare("INSERT INTO staff (username, tc, password) VALUES (?, ?, ?)");  
        $stmt->bind_param("sss", $username, $tc, $password);  
   
        if($stmt->execute()){  
            $stmt = $conn->prepare("SELECT id, id, username, tc FROM staff WHERE username = ?");   
            $stmt->bind_param("s",$username);  
            $stmt->execute();  
            $stmt->bind_result($userid, $id, $username, $tc);  
            $stmt->fetch();  
   
            $staff = array(  
            'id'=>$id,   
            'username'=>$username,   
            'tc'=>$tc,  
              
            );  
   
            $stmt->close();  
            $response['error'] = false;   
            $response['message'] = 'User registered successfully';   
            $response['user'] = $staff;   
        }  
    }   
}  
else{  
    $response['error'] = true;   
    $response['message'] = 'required parameters are not available';   
}  

echo json_encode($response);  
function isTheseParametersAvailable($params){  
foreach($params as $param){  
 if(!isset($_GET[$param])){  
     return false;   
  }  
}  
return true;   
}  
?>  
