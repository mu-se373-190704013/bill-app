<?php
header("Access-Control-Allow-Origin: *"); 
require_once 'connection.php';  
  $response = array();  
     
    if(isTheseParametersAvailable(array('Tc','amount','unit','debt','month'))){  
    $Tc = $_GET['Tc'];   
    $amount= $_GET['amount'];   
    $unit = $_GET['unit'];
    $debt = $_GET['debt'];
    $month = $_GET['month'];
    

    $stmt = $conn->prepare("SELECT id FROM bills WHERE Tc = ? OR amount = ? OR unit = ? OR debt = ? OR month = ?");  
    $stmt->bind_param("sssss", $Tc, $amount,$unit, $debt,$month);  
    $stmt->execute();  
    $stmt->store_result();  
   
   /* if($stmt->num_rows > 0){  
        $response['error'] = true;  
        $response['message'] = 'bill already send';  
        $stmt->close();  
    }  */
    //else{  
        $stmt = $conn->prepare("INSERT INTO bills (Tc, amount, unit, debt,month) VALUES (?, ?, ?, ?, ?)");  
        $stmt->bind_param("sssss", $Tc, $amount,$unit,$debt,$month);  
   
        if($stmt->execute()){  
            $stmt = $conn->prepare("SELECT id, id, Tc, amount, unit, debt,month FROM bills WHERE Tc = ?");   
            $stmt->bind_param("s",$Tc);  
            $stmt->execute();  
            $stmt->bind_result($userid, $id, $Tc, $amount,$unit,$debt,$month);  
            $stmt->fetch();  
   
            $bill = array(  
            'id'=>$id,   
            'Tc'=>$Tc,   
            'amount'=>$amount,  
            'unit'=>$unit,
            'debt'=>$debt,
            'month'=>$month,
              
            );  
   
            $stmt->close();  
            $response['error'] = false;   
            $response['message'] = 'User bill send successfully';   
            $response['bill'] = $bill;   
        }  
   // }   
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
