<?php
$servername = "localhost";
$username = "root";
$password = "reha";
$dbname = "os_lab";
$conn = mysqli_connect($servername, $username, $password, $dbname);

if(isset($_POST['submit'])){
   $email = mysqli_real_escape_string($conn, $_POST['email']);
   $pass = mysqli_real_escape_string($conn, $_POST['password']);
   $select_users = mysqli_query($conn, "SELECT * FROM `users` WHERE email = '$email' AND password = '$pass'") or die('query failed 1');

   if(mysqli_num_rows($select_users) > 0){
              
            $select_user = mysqli_query($conn, "SELECT id FROM `users` where email = '$email' ") or die('query failed 2');
            while($fetch_users = mysqli_fetch_assoc($select_user))
            {
               $id = $fetch_users['id'];
               mysqli_query($conn, "UPDATE current_user_lru SET id = '$id' ") or die('query failed 3');
            }
   
      $row = mysqli_fetch_assoc($select_users);

      if($row['user_type'] == 'admin'){

         $_SESSION['admin_name'] = $row['name'];
         $_SESSION['admin_email'] = $row['email'];
         $_SESSION['admin_id'] = $row['id'];
         header('location:admin_page.php');

      }elseif($row['user_type'] == 'user'){

         $_SESSION['user_name'] = $row['name'];
         $_SESSION['user_email'] = $row['email'];
         $_SESSION['user_id'] = $row['id'];
         header('location:index.html');

      }

   }else{
      $message[] = 'incorrect email or password!';
   }
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>login</title>
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
    margin: 10px 0;
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
$id_1 = mysqli_query($conn, "SELECT id FROM `current_user_lru` ") or die('query failed');  
while($id = mysqli_fetch_assoc($id_1))
{
   if($id['id'] == 0)
   {  
?> 
<div>
   <form action="" method="post">
      <h3>Login now</h3>
      <input type="email" name="email" placeholder="enter your email">
      <input type="password" name="password" placeholder="enter your password">
      <input type="submit" name="submit" value="login now" class="btn">
      <p>don't have an account? <a href="register.php">register now</a></p>
   </form>
<?php
   } 
   else{
      echo "Current user is still logged in so new user can not log in... ";
   }; 
};   
?>
</div>
</body>
</html>