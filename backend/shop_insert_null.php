<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    
    $title = $_POST['title'];
    $amount = $_POST['amount'];
    $measure = $_POST['measure'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "INSERT INTO shop_list (title, amount, measure, user_id) VALUES ('$title', '$amount', '$measure', '$user_id')";
    $sql_unic = "SELECT * FROM shop_list WHERE user_id='$user_id' AND title='$title'";
    $sql_update = "UPDATE shop_list SET amount='$amount', measure='$measure' WHERE user_id='$user_id' AND title='$title'";
    

    $response_check = mysqli_query($conn, $sql_unic);
    if ( mysqli_num_rows($response_check) === 0) {
        if ( mysqli_query($conn, $sql) ) {
            $result["success"] = "1";
            $result["message"] = "successi";
    
            echo json_encode($result);
            mysqli_close($conn);
        
        } else {
    
            $result["success"] = "0";
            $result["message"] = "error";
    
            echo json_encode($result);
            mysqli_close($conn);
        }
    }
}

?>