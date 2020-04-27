<form action="" method="get">
	<input type="text" name="search" placeholder="Name">
	<input type="text" name="tags" placeholder="Tags">
	<input type="Submit" value="Search">
</form>

<?php
	if(@$_GET['search']) {
		$api_url = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&titles=".ucwords($_GET['search'])."&redirects=true";
		$api_url = str_replace(' ', '%20', $api_url);
		
		if($data = json_decode(@file_get_contents($api_url))) {
			foreach($data->query->pages as $key=>$val) {
				$pageId = $key;
				break;
			}
			$content = $data->query->pages->$pageId->extract;
			
			header('Content-Type:text/html; charset=utf-8');
						
			$file = 'E:\Folder\Новая папка\thumbnote\src\main\java\net\thumbtack\thumbnote\content.txt';
			$content =  preg_replace( "/\n\s+/", "\n", rtrim(html_entity_decode(strip_tags($content))));
			file_put_contents($file, $content);
						
			$data = array("login" => "alexander", "password" => "12345pass",
						  "notebookId" => "22", 
						  "name" => $_GET['search'], 
						  "text" => $content, 
						  "tags" => $_GET['tags']);                                                                    
			$data_string = json_encode($data);
			
			$chx = curl_init('http://thumbnote-dev.thumbtack.lo:8080/rest/note/create');
			curl_setopt($chx, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
			curl_setopt($chx, CURLOPT_POSTFIELDS, $data_string);                                                                  
			curl_setopt($chx, CURLOPT_RETURNTRANSFER, true);                                                                      
			curl_setopt($chx, CURLOPT_HTTPHEADER, array(                                                                          
			'Content-Type: application/json',                                                                                
			'Content-Length: ' . strlen($data_string))                                                                       
			);                                                                                                                                                                                              
			$result = curl_exec($chx);
			curl_close($chx);
			echo $result;
			echo '\n';
		} else {
			echo 'No results found';
		}
	}
?>