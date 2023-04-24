<?php
  $servername = "localhost";
	$username = "root";
	$password = "reha";
  $dbname = "os_lab";
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	if (!$conn) {
		die("Connection failed: " . mysqli_connect_error());
	}
     $correct = (int)substr($_POST['correct'],9);
    $incorrect = (int)substr($_POST['incorrect'],11);
	try{
	$id_1 = mysqli_query($conn, "SELECT id FROM `current_user_lru` ") or die('query failed 1');  
         while($id = mysqli_fetch_assoc($id_1))
         {
            if($id['id'] == 0)
            {
            }
            else
            { 
              $id_2 = $id['id'];
              mysqli_query($conn, "UPDATE `users` SET Correct_ans = Correct_ans + '$correct' WHERE id = '$id_2' ") or die('query failed 2');
              mysqli_query($conn, "UPDATE `users` SET Wrong_ans = Wrong_ans + '$incorrect' WHERE id = '$id_2' ") or die('query failed 3');
			}
		} 
    }
  catch(Exception $e) {}
  // Close database connection
	mysqli_close($conn);
	header("location:javascript://history.go(-1)");
?>