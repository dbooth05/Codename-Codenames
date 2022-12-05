package codenames;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

@TestMethodOrder(OrderAnnotation.class)	// enfoce a specific order 
class BenTestGameControllerLobbies {

	/**
	 * force rest-assured to use JSON parsing for plaintext responses
	 * ...because rest-assured is annoying
	 */
	@BeforeAll
	static void setup() {
		RestAssured.registerParser("text/plain", Parser.JSON);
		
		with()	// create player for testing
		.body("{\"username\":\"test\",\"password\":\"world\"}")
		.contentType("application/json")	// set this to json type
		.post("/users/register");
	}
 
	/**
	 * adds a bunch of words for testing
	 */
	@Order(1)
	@Test
	void testAddWords() {
		with()
		.body("[\r\n"
				+ "    {\"word\":\"Apple\"},\r\n"
				+ "    {\"word\":\"Banana\"},\r\n"
				+ "    {\"word\":\"Pear\"},\r\n"
				+ "    {\"word\":\"Orange\"},\r\n"
				+ "    {\"word\":\"Peach\"},\r\n"
				+ "    {\"word\":\"Alex\"},\r\n"
				+ "    {\"word\":\"Isaac\"},\r\n"
				+ "    {\"word\":\"Dylan\"},\r\n"
				+ "    {\"word\":\"Jimmy\"},\r\n"
				+ "    {\"word\":\"Ben\"},\r\n"
				+ "    {\"word\":\"Mitra\"},\r\n"
				+ "    {\"word\":\"Gold\"},\r\n"
				+ "    {\"word\":\"Silver\"},\r\n"
				+ "    {\"word\":\"Platinum\"},\r\n"
				+ "    {\"word\":\"Diamond\"},\r\n"
				+ "    {\"word\":\"Bronze\"},\r\n"
				+ "    {\"word\":\"customer\"},\r\n"
				+ "    {\"word\":\"stranger\"},\r\n"
				+ "    {\"word\":\"painting\"},\r\n"
				+ "    {\"word\":\"sculpture\"},\r\n"
				+ "    {\"word\":\"Iowa\"},\r\n"
				+ "    {\"word\":\"Utah\"},\r\n"
				+ "    {\"word\":\"Kansas\"},\r\n"
				+ "    {\"word\":\"Arkansas\"},\r\n"
				+ "    {\"word\":\"Nebraska\"},\r\n"
				+ "    {\"word\":\"Missouri\"},\r\n"
				+ "    {\"word\":\"Cyclone\"},\r\n"
				+ "    {\"word\":\"Tornado\"},\r\n"
				+ "    {\"word\":\"Hurricane\"},\r\n"
				+ "    {\"word\":\"Tsunami\"}\r\n"
				+ "]")
		.contentType("application/json")	// set this to json type
		.put("http://10.90.75.56:8080/admin/cards/addbulk")
		
		.then()
		
		.statusCode(200)
		.assertThat()
		.body("message", equalTo("success"));
	}
	
	/**
	 * test creating a new lobby
	 * 
	 */
	@Order(2)
	@Test
	void testCreateLobby() {
		with()
		.body("{\"gameLobbyName\":\"lobby1\"}")
		.contentType("application/json")	// set this to json type
		.post("/games/add")
		
		.then()
		
		.statusCode(200)
		.assertThat()
		.body("message", equalTo("success"));
	}
	
	/**
	 * checks the lobby has no people in it
	 */
	@Order(3)
	@Test
	void testCheckLobbySize() {
		with()
		.get("/games/1/numplayers")
		
		.then()
		
		.statusCode(200)
		.assertThat()
		.body("playerNum", equalTo("0"));
	}
	
	/**
	 * tries to add a player to a lobby
	 */
	@Order(4)
	@Test
	void testAddPlayerToLobby() {
		
		with()
		.post("/games/1/addplayer/test")
		
		.then()
		.statusCode(200)
		.assertThat()
		.body("message", equalTo("success"));
	}

	/**
	 * tests removing a player from the lobby
	 */
	@Order(5)
	@Test
	void testRemovePlayerFromLobby() {
		with().delete("/games/1/removeplayer/test");
		
		with()	// check player has been removed
		.get("/games/1/numplayers")
		
		.then()
		
		.statusCode(200)
		.assertThat()
		.body("playerNum", equalTo("0"));
	}
	
	@Order(6)
	@Test
	void testDeleteLobby() {
		with().delete("/games/1/delete");
		with()	// check player has been removed
		.get("/games/1/numplayers")
		
		.then()
		
		.statusCode(200)
		.assertThat()
		.body("playerNum", equalTo("0"));
	}
	
	
	
	@AfterAll
	static void cleanUp() {
		
		with()	// clear games
		.delete("/games/removeall/98765");
		
		with() 	// clear users
		.delete("users/clearusers/75362");
		
		with().delete("/end");
	}
}
