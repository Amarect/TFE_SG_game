<?php
require "DataBase.php";
$db = new DataBase();
if ($db->dbConnect()) {
    $json = $db->getRegionsfromDB();
    echo $json;
} else echo "Error: Database connection";
?>
