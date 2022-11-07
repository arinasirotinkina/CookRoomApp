<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    
    $title = $_POST['title'];
    $description = $_POST['description'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "INSERT INTO recipes (title, description, user_id) VALUES ('$title', '$description', '$user_id')";

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