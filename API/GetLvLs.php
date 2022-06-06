<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['difficulte'])) {
    if ($db->dbConnect()) {
        $json = $db->geLvL($_POST['difficulte']);
        echo $json;
    } else echo "Error: Database connection";
} else echo "difficulte necessaire";
?>
