<?php
header("Access-Control-Allow-Origin: *"); 
require_once 'connection.php';  
  $response = array();  
  if(isTheseParametersAvailable(array('tc', 'password'))){  
    $tc = $_GET['tc'];   
    $password = md5($_GET['password']);  
    $response['message'] = 'apicall'; 
    $stmt = $conn->prepare("SELECT id, username, tc FROM staff WHERE tc = ? AND password = ?");  
    $stmt->bind_param("ss",$tc, $password);  
    $stmt->execute();  
    $stmt->store_result();  
    if($stmt->num_rows > 0){  
    $stmt->bind_result($id, $username, $tc);  
    $stmt->fetch();  
    $staff = array(  
    'id'=>$id,   
    'username'=>$username,   
    'tc'=>$tc,  
     
    );  
   
    $response['error'] = false;   
    $response['message'] = 'Login successfull';   
    $response['user'] = $staff;   
 }  
 else{  
    $response['error'] = false;   
    $response['message'] = 'Invalid username or password';  
 }  
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
