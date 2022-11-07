<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $category = $_POST['category'];
    $user_id = $_POST['user_id'];

    require_once 'connection.php';

    $sql = "SELECT * FROM products WHERE category='$category' AND user_id='$user_id'";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['product'] = array();
    
    if ( mysqli_num_rows($response)  >0) {
        
        while ($row = mysqli_fetch_assoc($response)) {
            $index = array();
            $index['id'] = $row['id'];
            $index['title'] = $row['title'];
            $index['category'] = $row['category'];
            $index['amount'] = $row['amount'];
            $index['measure'] = $row['measure'];
    
            array_push($result['product'], $index);
        }
        
        $result['success'] = "1";
        $result['message'] = "success";
        echo json_encode($result);

        mysqli_close($conn);


    } else {

        $result['success'] = "0";
        $result['message'] = "error";
        echo json_encode($result);

        mysqli_close($conn);

    }

}

?>