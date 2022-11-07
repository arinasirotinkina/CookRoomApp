<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $email = $_POST['email'];
    $password = $_POST['password'];

    require_once 'connection.php';

    $sql = "SELECT * FROM users WHERE email='$email' ";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['login'] = array();
    if ( mysqli_num_rows($response) === 1 ) {
        
        $row = mysqli_fetch_assoc($response);

        if ( password_verify($password, $row['password']) ) {
            
            $index['email'] = $row['email'];
            $index['id'] = $row['id'];
            $index['time'] = $row['time'];

            array_push($result['login'], $index);

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

            mysqli_close($conn);

        } else {

            $result['success'] = "0";
            $result['message'] = "Неверный пароль";
            echo json_encode($result);

            mysqli_close($conn);

        }

    }
    else {
        $result['success'] = "0";
        $result['message'] = "Email не зарегистрирован";
        echo json_encode($result);

        mysqli_close($conn);
    }

}

?>