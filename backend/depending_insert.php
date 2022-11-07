<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $recipe_id = $_POST['recipe_id'];
    $product_id = $_POST['product_id'];
    $title = $_POST['title'];
    $amount = $_POST['amount'];
    $measure = $_POST['measure'];
    $user_id = $_POST['user_id'];


    require_once 'connection.php';

    $sql = "INSERT INTO depending (recipe_id, product_id, title, amount, measure, user_id) VALUES ('$recipe_id', '$product_id', '$title', '$amount', '$measure', '$user_id')";
    
    
    $sql_unic = "SELECT * FROM depending WHERE user_id='$user_id' AND title='$title' AND recipe_id='$recipe_id'";
    

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
        $result["success"] = "1";
        $result["message"] = "Ингредиент с таким названием уже существует";

        echo json_encode($result);
        mysqli_close($conn);
        
    }
}

?>
    