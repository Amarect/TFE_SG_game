<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }
    function getEmbusc($difficulte){
        
        $this->sql = "select * from embuscades where difficulte <= '" . $difficulte . "'";
        
        $result = mysqli_query($this->connect, $this->sql);
        $rows = array();
        while($r = mysqli_fetch_assoc($result)) {

            $rows[] = $r;
        }
        
        return json_encode($rows);
    }
    function geLvL($difficulte){
        
        $this->sql = "select * from niveaux where difficulte = '" . $difficulte . "'";
        
        $result = mysqli_query($this->connect, $this->sql);
        $rows = array();
        while($r = mysqli_fetch_assoc($result)) {

            $rows[] = $r;
        }
        
        return json_encode($rows);
    }

    function logIn($table, $nom, $password)
    {
        $nom = $this->prepareData($nom);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where nom = '" . $nom . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbnom = $row['nom'];
            $dbpassword = $row['password'];
            if ($dbnom == $nom && password_verify($password, $dbpassword)) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }
    function getRegionsfromDB(){
        $this->sql = "select * from regions";
        
        $result = mysqli_query($this->connect, $this->sql);
        $rows = array();
        while($r = mysqli_fetch_assoc($result)) {

            $rows[] = $r;
        }
        
        return json_encode($rows);
    }
    function insertUser($table, $nom, $password,$max_LvL,$actu_LvL,$mail,$po,$actu_path_position,$nbr_bonus_time,$nbr_bonus_embuscade,$nbr_bonus_vue){

        $nom = $this->prepareData($nom);
        $password = $this->prepareData($password);
        $max_LvL = $this->prepareData($max_LvL);
        $actu_LvL = $this->prepareData($actu_LvL);
        $mail = $this->prepareData($mail);
        $po = $this->prepareData($po);
        $actu_path_position = $this->prepareData($actu_path_position);
        $nbr_bonus_time = $this->prepareData($nbr_bonus_time);
        $nbr_bonus_embuscade = $this->prepareData($nbr_bonus_embuscade);
        $nbr_bonus_vue = $this->prepareData($nbr_bonus_vue);

        $this->sql ="update  ". $table ." set max_LvL='". $max_LvL ."', actu_LvL='". $actu_LvL ."', mail='". $mail ."', po='". $po ."', actu_path_position='". $actu_path_position ."', nbr_bonus_time='". $nbr_bonus_time ."', nbr_bonus_embuscade='". $nbr_bonus_embuscade ."', nbr_bonus_vue='". $nbr_bonus_vue ."'   WHERE nom= '". $nom."' and password = '".$password."'";

        $result = mysqli_query($this->connect, $this->sql);

        if($result){
            $inserted = true;
        }
        else{
            $inserted = false;
        }
        return $inserted;


    }
    function getUser($table, $nom, $password){
        $nom = $this->prepareData($nom);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where nom = '" . $nom . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbnom = $row['nom'];
            $dbpassword = $row['password'];
            $dbmax_LvL = $row['max_LvL'];
            $dbactu_LvL = $row['actu_LvL'];
            $dbmail = $row['mail'];
            $dbpo = $row['po'];
            $dbactu_path_position= $row['actu_path_position'];
            $dbnbr_bonus_time = $row['nbr_bonus_time'];
            $dbnbr_bonus_embuscade = $row['nbr_bonus_embuscade'];
            $dbnbr_bonus_vue = $row['nbr_bonus_vue'];
            if ($dbnom == $nom && password_verify($password, $dbpassword)) {
                return json_encode(array(
                    "nom" => $dbnom,
                    "password" => $dbpassword,
                    "max_LvL" => $dbmax_LvL,
                    "actu_LvL" => $dbactu_LvL,
                    "mail" => $dbmail,
                    "po" => $dbpo,
                    "actu_path_position" => $dbactu_path_position,
                    "nbr_bonus_time" => $dbnbr_bonus_time,
                    "nbr_bonus_embuscade" => $dbnbr_bonus_embuscade,
                    "nbr_bonus_vue" => $dbnbr_bonus_vue,
                ));
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function signUp($table, $nom, $mail, $password)
    {
        $nom = $this->prepareData($nom);
        $password = $this->prepareData($password);
        $mail = $this->prepareData($mail);
        $password = password_hash($password, PASSWORD_DEFAULT);
        $this->sql =
            "INSERT INTO " . $table . " (nom, password, mail) VALUES ('" . $nom . "','" . $password . "','" . $mail . "')";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

}

?>
