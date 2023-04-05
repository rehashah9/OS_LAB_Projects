<?php
  $servername = "localhost";
	$username = "root";
	$password = "reha";
	$dbname = "os_lab";
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}
		  $Name = $_POST['name'];
		  $Email = $_POST['email'];
		  $Phone = $_POST['phone'];
		  $Problem = $_POST['text'];
	try{
		// Insert data into database
		$sql = "INSERT INTO sstf_contact (Name, Email, Phone, Problem) VALUES ('$Name', '$Email', '$Phone', '$Problem')";
		mysqli_query($conn, $sql);
  }
  catch(Exception $e) {
  }
  // Close database connection
	mysqli_close($conn);
	header("location:javascript://history.go(-1)");
?>