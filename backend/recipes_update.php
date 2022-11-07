<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){
    $id = $_POST['id'];
    $title = $_POST['title'];
    $description = $_POST['description'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "UPDATE recipes SET title='$title', description='$description' WHERE id='$id' and user_id='$user_id'";


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
} else {
    $result["success"] = "0";
    $result["message"] = "error";

    echo json_encode($result);
}

?>