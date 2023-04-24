<?php
  $servername = "localhost";
	$username = "root";
	$password = "reha";
	$dbname = "os_lab";
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}
	$Reference_string = $_POST['rs'];
	$Size = $_POST['tsim'];
	$No_of_Hit = $_POST['noh'];
	$No_of_Page_Fault = $_POST['lor'];
	$Hit_Rate = $_POST['hr'];
	$Miss_Rate = $_POST['mr'];
	  try{
	  // Insert data into database
	  $sql = "INSERT INTO lru_table (Reference_string, Size, No_of_Hit, No_of_Page_Fault, Hit_Rate, Miss_Rate) VALUES ('$Reference_string', '$Size', '$No_of_Hit', '$No_of_Page_Fault','$Hit_Rate','$Miss_Rate')";
	  mysqli_query($conn, $sql);
  
	  $id_1 = mysqli_query($conn, "SELECT id FROM `current_user_lru` ") or die('query failed');  
		   while($id = mysqli_fetch_assoc($id_1))
		   {
			  if($id['id'] == 0)
			  {
			  }
			  else
			  { 
				$id_2 = $id['id'];
				mysqli_query($conn, "UPDATE `users` SET No_Sim = No_Sim + 1 WHERE id = '$id_2' ") or die('query failed');
			  }
		  } 
	  }
	catch(Exception $e) {}
	// Close database connection
	  mysqli_close($conn);
	  header("location:javascript://history.go(-1)");
  ?>