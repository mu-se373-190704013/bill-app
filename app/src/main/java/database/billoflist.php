<?php 
 
 //database constants
 define('DB_HOST', 'localhost');
 define('DB_USER', 'root');
 define('DB_PASS', '');
 define('DB_NAME', 'test');
 
 //connecting to database and getting the connection object
 $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
 
 //Checking if any error occured while connecting
 if (mysqli_connect_errno()) {
 echo "Failed to connect to MySQL: " . mysqli_connect_error();
 die();
 }   
    
 //creating a query
    $Tc = $_POST['Tc'];
    $stmt = $conn->prepare("SELECT bills.id,bills.Tc,bills.amount,bills.unit,bills.debt,bills.month FROM bills, users WHERE bills.Tc = users.tc and bills.tc = $Tc");
    //executing the query
    $stmt->execute();

    //binding results to the query
    $stmt->bind_result($id, $Tc, $amount, $unit, $debt, $month);

    $bills = array();

    //traversing through all the result
    while($stmt->fetch()){
    $temp = array();
    $temp['id'] = $id;
    $temp['Tc'] = $Tc;
    $temp['amount'] = $amount;
    $temp['unit'] = $unit;
    $temp['debt'] = $debt;
    $temp['month'] = $month;
    array_push($bills, $temp);
    }

    //displaying the result in json format
    echo json_encode($bills);


?>