package API_Chaining;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
Response response;
JsonPath jPath;
String strURI="http://44.202.123.136:8088/employees";
@Test
public void test1()
{
	String fname;
	String lname;
//GetMethod to GetAll Employee details 
response=GetMethodAll();
Assert.assertEquals(response.getStatusCode(), 200);
System.out.println(response.asString());

//Create new employee
response=CreateNewEmployee("Sam","William",3000,"sam.w@abc.com");
Assert.assertEquals(response.getStatusCode(), 201);
jPath=response.jsonPath();
int ID=jPath.getInt("id");
System.out.println("Employee "+ID+" was created successfully!");

//Verify and display new Exployee details
response=GetMethodByID(ID);
jPath=response.jsonPath();
fname=jPath.getString("firstName");
lname=jPath.getString("lastName");
Assert.assertEquals(fname+lname, "SamWilliam");
Assert.assertEquals(response.getStatusCode(), 200);
System.out.println("New Employee details as follows :");
System.out.println(response.asString());

//Update new employee
response=UpdateEmployee("Tom","Sunny",5000,"tom.s@abc.com",ID);
Assert.assertEquals(response.getStatusCode(), 200);
System.out.println("Employee "+ID+" was updated successfully!");

//Verify the Updated employee details
response=GetMethodByID(ID);
jPath=response.jsonPath();
fname=jPath.getString("firstName");
lname=jPath.getString("lastName");
Assert.assertEquals(fname+lname, "TomSunny");
System.out.println("New Employee details updation verified");

//Delete the new employee
response=DeleteEmployee(ID);
Assert.assertEquals(response.getStatusCode(), 200);
System.out.println("Employee "+ID+" was deleted successfully!");

//Verify if the employee is deleted
response=GetMethodAll();
jPath=response.jsonPath();
List<Integer> listID=jPath.getList("id");
Assert.assertFalse(listID.contains(ID));
Assert.assertEquals(response.getStatusCode(), 200);
System.out.println("Employee "+ID+" deletion verified!!!");

}
private Response DeleteEmployee(int ID) {
	RestAssured.baseURI=strURI;
	RequestSpecification request=RestAssured.given();
	Response response=request.delete("/"+ID);
	return response;
}
private Response UpdateEmployee(String fname,String lname,int salary,String email,int ID) {
	RestAssured.baseURI=strURI;
	RequestSpecification request=RestAssured.given();
	Map<String,Object> mapObj=new HashMap<String,Object>();
	mapObj.put("firstName", fname);
	mapObj.put("lastName", lname);
	mapObj.put("salary", salary);	
	mapObj.put("email", email);
	Response response=request.contentType(ContentType.JSON).accept(ContentType.JSON).body(mapObj).put("/"+ID);
	return response;
}
private Response GetMethodByID(int ID) {
	RestAssured.baseURI=strURI;
	RequestSpecification request=RestAssured.given();
	Response response=request.get("/"+ID);
	return response;
}
private Response CreateNewEmployee(String fname,String lname,int salary,String email) {
	RestAssured.baseURI=strURI;
	RequestSpecification request=RestAssured.given();
	Map<String,Object> mapObj=new HashMap<String,Object>();
	mapObj.put("firstName", fname);
	mapObj.put("lastName", lname);
	mapObj.put("salary", salary);	
	mapObj.put("email", email);
	Response response=request.contentType(ContentType.JSON).accept(ContentType.JSON).body(mapObj).post();
	return response;
}
private Response GetMethodAll() {
	RestAssured.baseURI=strURI;
	RequestSpecification request=RestAssured.given();
	Response response=request.get();
	return response;
}

}
