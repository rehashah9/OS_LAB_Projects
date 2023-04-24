<?php
  $servername = "localhost";
  $username = "root";
  $password = "reha";
  $dbname = "os_lab";
  $conn = mysqli_connect($servername, $username, $password, $dbname);



  if(isset($_GET['delete'])){
   $delete_id = $_GET['delete'];
   mysqli_query($conn, "DELETE FROM `users` WHERE id = '$delete_id'") or die('query failed');
   header('location:admin_page.php');
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>Admin Page</title>
  <style>
    @import url('https://fonts.googleapis.com/css2?family=Ubuntu:ital,wght@0,500;1,400&display=swap'); 
body{background-image:url("lru1.jpg");}
h1{
            font-family: 'Ubuntu', sans-serif;
            font-size: 50px;
            text-align: center;
            margin: 100px;
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
text-align: left;
font-size: 25px;
font-family: 'Ubuntu', sans-serif;
margin-left: 300px;
}
</style>
</head>
<body>
   <h1 class="title"> User accounts </h1>

   <div>
      <?php
         $select_users = mysqli_query($conn, "SELECT * FROM `users`") or die('query failed');
         while($fetch_users = mysqli_fetch_assoc($select_users)){
      ?>
      <div>
         <p> user id : <span><?php echo $fetch_users['id']; ?></span> </p>
         <p> username : <span><?php echo $fetch_users['name']; ?></span> </p>
         <p> email : <span><?php echo $fetch_users['email']; ?></span> </p>
         <p> no of simulation performed : <span><?php echo $fetch_users['No_Sim']; ?></span> </p>
         <p> no of correct answers : <span><?php echo $fetch_users['Correct_ans']; ?></span> </p>
         <p> no of wrong answers : <span><?php echo $fetch_users['Wrong_ans']; ?></span> </p>
         <a href="admin_page.php?delete=<?php echo $fetch_users['id']; ?>" onclick="return confirm('delete this user?');" class="delete-btn">delete user</a>
      </div>
      <?php
         };
      ?>
   </div>

</body>
</html>