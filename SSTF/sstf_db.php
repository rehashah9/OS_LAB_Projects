<?php
  $servername = "localhost";
	$username = "root";
	$password = "reha";
	$dbname = "os_lab";
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}
		  $Head_Pos = $_POST['ip'];
		  $Req_List = $_POST['lor'];
		  $Req_Order = $_POST['oc'];
		  $Total_Mov = $_POST['mior'];
	try{
		// Insert data into database
		$sql = "INSERT INTO sstf_data (Head_Pos, Req_List, Req_Order, Total_Mov) VALUES ('$Head_Pos', '$Req_List', '$Req_Order', '$Total_Mov')";
		mysqli_query($conn, $sql);
  }
  catch(Exception $e) {
  }
  // Close database connection
	mysqli_close($conn);
	header("location:javascript://history.go(-1)");
?>