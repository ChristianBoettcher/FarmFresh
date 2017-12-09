<?php
ini_set('display_errors', '1');

error_reporting(E_ALL);
// Connect to the Database

$dsn 	  = 'mysql:host=cssgate.insttech.washington.edu;dbname=doseon';
$username = 'doseon';
$password = 'Getex~';

if (!isset($_POST['user']) || empty($_POST['user'])) {
	$result = array(
            "code" => 100,
            "message" => "Username not defined."
        );
	echo json_encode($result);
	exit();
}
if (!isset($_POST['pass']) || empty($_POST['pass'])) {
	$result = array(
            "code" => 101,
            "message" => "password not defined."
        );
	echo json_encode($result);
	exit();
} 

if (!isset($_POST['name']) || empty($_POST['name'])) {
	$result = array(
            "code" => 102,
            "message" => "name not defined."
        );
	echo json_encode($result);
	exit();
} 

$userin = $_POST['user'];
$passin = $_POST['pass'];
$namein = $_POST['name'];


try {
    #make a new DB object to interact with
    $db = new PDO($dsn, $username, $password);
    #build a SQL statement to query the DB
    
    $select_sql = "INSERT INTO User VALUES ('$userin','$passin', '$namein');";
    #make a query object
    if ($user_query = $db->query($select_sql)) {
		//success
		$result = array(
            "code" => 300,
            "message" => "Successfully registered."
        );
	} else {
		//failed
		$result = array(
            "code" => 200,
            "message" => "Username already exists."
        );
	}
    echo json_encode($result);
    $db = null;
}
catch (PDOException $e) {
    $error_message = $e->getMessage();
    $result        = array(
        "code" => 400,
        "message" => "There was an error connecting to
the database: $error_message"
    );
    echo json_encode($result);
    exit();
}
?>