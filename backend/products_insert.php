<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    
    $title = $_POST['title'];
    $category = $_POST['category'];
    $amount = $_POST['amount'];
    $measure = $_POST['measure'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "INSERT INTO products (title, category, amount, measure, user_id) VALUES ('$title', '$category', '$amount', '$measure', '$user_id')";

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