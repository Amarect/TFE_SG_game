<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['nom']) && isset($_POST['mail']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("users", $_POST['nom'], $_POST['mail'], $_POST['password'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
