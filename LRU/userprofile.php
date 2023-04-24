<?php
            $servername = "localhost";
            $username = "root";
            $password = "reha";
            $dbname = "os_lab";
            $conn = mysqli_connect($servername, $username, $password, $dbname);

            if(isset($_GET['delete'])){
            $id = $_GET['delete'];
            mysqli_query($conn, "DELETE FROM `users` WHERE id = '$id'") or die('query failed');
            header('location:userprofile.php');
            }
?>

<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>User_Profile</title>
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
   
<section class="users">

   <h1> USERS ACCOUNT </h1>

   <div>
      <?php
         $id_1 = mysqli_query($conn, "SELECT id FROM `current_user_lru` ") or die('query failed');  
         while($id = mysqli_fetch_assoc($id_1))
         {
            if($id['id'] == 0)
            {
                echo "This is default user. No user details available"; 
            }
            else
            { 
              $id_2 = $id['id'];
              $select_users = mysqli_query($conn, "SELECT * FROM `users` where id = '$id_2' ") or die('query failed');
            
         
         while($fetch_users = mysqli_fetch_assoc($select_users)){

      ?>

      <div>
         <p> user id : <span><?php echo $fetch_users['id']; ?></span> </p>
         <p> username : <span><?php echo $fetch_users['name']; ?></span> </p>
         <p> email : <span><?php echo $fetch_users['email']; ?></span> </p>
         <p> no of simulation performed : <span><?php echo $fetch_users['No_Sim']; ?></span> </p>
         <p> no of correct answers : <span><?php echo $fetch_users['Correct_ans']; ?></span> </p>
         <p> no of wrong answers : <span><?php echo $fetch_users['Wrong_ans']; ?></span> </p>
         <a href="userprofile.php?delete=<?php echo $fetch_users['id']; ?>" onclick="return confirm('delete this user?');" class="delete-btn">delete user</a>
      </div>
      <?php
             }; }; };   
      ?>
   </div>
</section>

</body>
</html>