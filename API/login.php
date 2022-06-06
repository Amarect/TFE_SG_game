<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['nom']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("users", $_POST['nom'], $_POST['password'])) {
            $json = $db->getUser("users", $_POST['nom'], $_POST['password']);
            echo $json;
        } else echo "Nom or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
