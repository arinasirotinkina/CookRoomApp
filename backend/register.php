<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    
    $email = $_POST['email'];
    $password = $_POST['password'];

    $password = password_hash($password, PASSWORD_DEFAULT);

    require_once 'connection.php';

    $sql = "INSERT INTO users (email, password) VALUES ('$email', '$password')";
    $sql_unic = "SELECT * FROM users WHERE email='$email'";

    $response_check = mysqli_query($conn, $sql_unic);
    if ( mysqli_num_rows($response_check) === 0) {
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
    else {

        $result["success"] = "0";
        $result["message"] = "Email существует";

        echo json_encode($result);
        mysqli_close($conn);
    }
}

?>