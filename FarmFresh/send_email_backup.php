<?php

if (!isset($_POST['name']) || empty($_POST['name'])) {
	$result = array(
            "code" => 101,
            "message" => "name not defined."
        );
	echo json_encode($result);
	exit();
} 

if (!isset($_POST['email']) || empty($_POST['email'])) {
	$result = array(
            "code" => 102,
            "message" => "email not defined."
        );
	echo json_encode($result);
	exit();
} 

	$pin = sprintf("%06d", mt_rand(1, 999999));
	$name = $_POST['name'];
	$to = $_POST['email'];

	$subject = "Farm Fresh Registration!";
	$message = "Hello $name,\n\nYour registration pin code is : $pin";
	$from = "no-reply-farmfresh@uw.edu";
	$headers = "From: $from";
	mail($to,$subject,$message,$headers);
	
	$result = array(
            "code" => 300,
            "message" => $pin
        );
	echo json_encode($result);
	
?>