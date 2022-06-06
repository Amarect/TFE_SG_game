<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['nom']) && isset($_POST['password']) && isset($_POST['max_LvL']) && isset($_POST['actu_LvL']) && isset($_POST['mail']) && isset($_POST['po']) && isset($_POST['actu_path_position']) && isset($_POST['nbr_bonus_time']) && isset($_POST['nbr_bonus_embuscade']) && isset($_POST['nbr_bonus_vue']) ) {
    if ($db->dbConnect()) {
        if($json = $db->insertUser("users", $_POST['nom'], $_POST['password'], $_POST['max_LvL'], $_POST['actu_LvL'], $_POST['mail'], $_POST['po'], $_POST['actu_path_position'], $_POST['nbr_bonus_time'], $_POST['nbr_bonus_embuscade'], $_POST['nbr_bonus_vue'])){
            echo "Operation reussite";
        }else{
            echo "insertion echoue";
        }
    } else echo "Error: Database connection";
} else echo "Data incomplete";
?>
