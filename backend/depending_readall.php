<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $user_id = $_POST['user_id'];
    $recipe_id = $_POST['recipe_id'];

    require_once 'connection.php';

    $sql = "SELECT * FROM depending WHERE user_id='$user_id' AND recipe_id='$recipe_id'";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['product'] = array();
    
    if ( mysqli_num_rows($response)  >0) {
        
        while ($row = mysqli_fetch_assoc($response)) {
            $index = array();
            $index['title'] = $row['title'];
            $index['amount'] = $row['amount'];
            $index['measure'] = $row['measure'];
            $index['product_id'] = $row['product_id'];
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