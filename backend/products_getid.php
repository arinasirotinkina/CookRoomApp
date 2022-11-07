<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $user_id = $_POST['user_id'];
    $title = $_POST['title'];

    require_once 'connection.php';

    $sql = "SELECT * FROM products WHERE title = '$title' AND user_id = '$user_id'";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['product'] = array();
    if ( mysqli_num_rows($response) === 1 ) {
        
        $row = mysqli_fetch_assoc($response);

            $index['id'] = $row['id'];

            array_push($result['product'], $index);

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

            mysqli_close($conn);

        

    }

}

?>