<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    
    $title = $_POST['title'];
    $recipe_id = $_POST['recipe_id'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "DELETE FROM depending WHERE user_id='$user_id' AND title='$title' AND recipe_id='$recipe_id'";

    if ( mysqli_query($conn, $sql) ) {
        $result["success"] = "1";
        $result["message"] = "success";

        echo json_encode($result);
        mysqli_close($conn);

    } else {

        $result["success"] = "0";
        $result["message"] = "error";

        echo json_encode($result);
        mysqli_close($conn);
    }
}

?>