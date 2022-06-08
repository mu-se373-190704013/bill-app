<?php 
 
 define('DB_HOST', 'localhost');
 define('DB_USER', 'root');
 define('DB_PASS', '');
 define('DB_NAME', 'test');
 
 $connection = mysqli_connect(DB_HOST, DB_USER, DB_PASS, DB_NAME);
  
      $id = $_POST['id'];
      $query = "DELETE FROM bills WHERE id = $id";
      echo $id;
      $result = mysqli_query($connection,$query);
    
      if($result){
          echo "Data Deleted";
      }
      else{
          echo"Error Occured";
      }

  ?>