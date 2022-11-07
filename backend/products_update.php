<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $id = $_POST['id'];
    $title = $_POST['title'];
    $category = $_POST['category'];
    $amount = $_POST['amount'];
    $measure = $_POST['measure'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "UPDATE products SET title='$title', category='$category', amount='$amount', measure='$measure', user_id = '$user_id' WHERE id='$id'";


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