<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $user_id = $_POST['user_id'];
    $time = $_POST['time'];

    require_once 'connection.php';

    $sql = "UPDATE users SET time='$time' WHERE id='$user_id'";


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