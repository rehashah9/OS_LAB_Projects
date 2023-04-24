<?php

$servername = "localhost";
$username = "root";
$password = "reha";
$dbname = "os_lab";
$conn = mysqli_connect($servername, $username, $password, $dbname);
  
mysqli_query($conn, "UPDATE current_user_lru SET id = 0") or die('query failed 3');

session_start();
session_unset();
session_destroy();

header('location:index.html');

?>