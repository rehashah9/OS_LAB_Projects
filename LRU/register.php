<?php
$servername = "localhost";
$username = "root";
$password = "reha";
$dbname = "os_lab";
$conn = mysqli_connect($servername, $username, $password, $dbname);

if(isset($_POST['submit'])){
   $name = mysqli_real_escape_string($conn, $_POST['name']);
   $email = mysqli_real_escape_string($conn, $_POST['email']);
   $pass = mysqli_real_escape_string($conn, $_POST['password']);
   $cpass = mysqli_real_escape_string($conn, $_POST['cpassword']);
   $user_type = 'user';

   $select_users = mysqli_query($conn, "SELECT * FROM `users` WHERE email = '$email'") or die('query failed');

   if(mysqli_num_rows($select_users) > 0){
      $message[] = 'user already exist!';
   }else{
      if($pass != $cpass){
         $message[] = 'confirm password not matched!';
      }else{
         mysqli_query($conn, "INSERT INTO `users`(name, email, password, user_type) VALUES('$name', '$email', '$cpass', '$user_type')") or die('query failed');
         $message[] = 'registered successfully!';
         header('location:login.php');
      }
   }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>register</title>
   <style>
    @import url('https://fonts.googleapis.com/css2?family=Ubuntu:ital,wght@0,500;1,400&display=swap'); 
    body{background-image:url("lru1.jpg");}
    h3{
                font-family: 'Ubuntu', sans-serif;
                font-size: 60px;
                text-align: center;
                margin: 150px;
                color: rgb(0, 0, 0);
            }
            div{
                font-family: 'Ubuntu', sans-serif;
                font-size: 20px;
                text-align: center;
                color: rgb(0, 0, 0);
            }
            .form{
  width: 900px;
  margin: auto;
  padding-bottom: 200px;
}

.hello
{
    margin: 20px 0;
    padding: auto;
    font-size: 25px;
    font-family: 'Ubuntu', sans-serif;
}
p{
   font-size: 25px;
    font-family: 'Ubuntu', sans-serif;
}
   </style>
</head>
<body>

<?php
if(isset($message)){
   foreach($message as $message){
      echo '
      <div class="message">
         <span>'.$message.'</span>
         <i class="fas fa-times" onclick="this.parentElement.remove();"></i>
      </div>
      ';
   }
}
?>
   
<div>

   <form action="" method="post">
      <h3>register now</h3>
      <input type="text" name="name" placeholder="enter your name">
      <input type="email" name="email" placeholder="enter your email">
      <input type="password" name="password" placeholder="enter your password">
      <input type="password" name="cpassword" placeholder="confirm your password">
      <input type="submit" name="submit" value="register now" class="btn">
      <p>already have an account? <a href="login.php">login now</a></p>
   </form>

</div>

</body>
</html>