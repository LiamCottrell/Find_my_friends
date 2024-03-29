//API keys
var mapBoxApiKey="sk.eyJ1IjoibGlhbWNvdHRyZWxsIiwiYSI6ImNqb2lyeTYwYTBiYW0za21kM2F1cnZnaDEifQ.ZzWwgyMX3ziVNyuhIWv3TA";
var openWeatherAPIKey="c1e8f1040e9aa955348f8333b8aa1ff9";
var baseURL="api";

//the document ready function
try	{
	$(function()
		{
		init();
		}
	);
	} catch (e)
		{
		alert("*** jQuery not loaded. ***");
		}

//
// Initialise page.
//
function init()
{
var map=makeMap("map",1,0.0,0.0);	//make map using Leaflet or GoogleMap API
var marker=makeMarker(map,0.0,0.0);	//make and put marker on map, keeping reference

//make dialog box
$("#cityDetails").dialog({	modal:true,			//modal dialog to disable parent when dialog is active
							autoOpen:false,		//set autoOpen to false, hidding dialog after creation
							title: "Add City",	//set title of dialog box
							minWidth: 500,
							minHeight: 400
						}
						);

//set click handler of Add City button
$("#addCity").click(function()
				{
				$("#cityName").val("");					//clear city name text input
				$("#cityDetails").dialog("open",true);	//open dialog box
				}
			);

//set click handler of Cancel button in Add City dialog
$("#cancelCity").click(function()
						{
						$("#cityDetails").dialog("close");
						}
					);

//set click handler of Save City button in Add City dialog
$("#saveCity").click(function()
		{
		saveCity(marker);	//save city to web service
		$("#cityDetails").dialog("close");
		}
	);

//set click handler of Delete Selected button
$("#deleteCity").click(function()
				{
				$("#cities .selected").each(function()
											{
											deleteCity($(this).attr("id"));
											$(this).remove();
											}
										);
				}
			);

populateCities();	//populate list of known cities
}

//
// save a city using the City service, given its position
//
function saveCity(marker)
{
var longitude=marker.getLatLng().lng;	//get longitude from position
var latitude=marker.getLatLng().lat;	//get latitude from position

var name=$("#cityName").val();	//get city name input text box value

var url=baseURL+"/city";					//URL of web service
var data={	"name":name,				//request parameters as a map
			"longitude":longitude,
			"latitude":latitude
		};

//use jQuery shorthand Ajax POST function
$.post(	url,			//URL of service
		data,			//parameters of request
		function()		//successful callback function
		{
		alert("City saved: "+name+" ("+longitude+","+latitude+")");
		} //end callback function
	); //end post call
} //end function

//
// retrieve all cities from City service and populate list
//
function populateCities()
{
var url=baseURL+"/city";		//URL of city service

//use jQuery shorthand Ajax function to get JSON data
$.getJSON(url,				//URL of service
		function(cities)		//successful callback function
		{
		$("#cities").empty();		//find city list and remove its children
		for (var i in cities)
			{
			var city=cities[i];		//get 1 city from the JSON list
			var id=city["id"];		//get city ID from JSON data
			var name=city["name"];	//get city name from JSON data
			//compose HTML of a list item using the city ID and name.
			var htmlCode="<li id='"+id+"'>"+name+"</li>";
			$("#cities").append(htmlCode);	//add a child to the city list
			}
		
		//look for all list items (i.e. cities), set their click handler
		$("#cities li").click(function()
								{
								// Call the cityClicked(...) function.
								// The parameter is the ID of the city clicked.
								// See how we get the ID from the located li element/tag.
								cityClicked($(this).attr("id"));
								} //end click handler function
						); //end click call
		} //end Ajax callback function
	); //end Ajax call
} //end function

//
// click handler of a city in the list
// parameter ID is the unique city identifier
//
function cityClicked(id)
{
$("#cities li").removeClass("selected"); //remove all list items from the class "selected, thus clearing previous selection

// Find the selected city (i.e. list item) and add the class "selected" to it.
// This will highlight it according to the "selected" class.
$("#"+id).addClass("selected");

//retrieve city coordinates from city service
var url=baseURL+"/city/"+id;		//URL of service, notice that ID is part of URL path

//use jQuery shorthand Ajax function to get JSON data
$.getJSON(	url,					//URL of service
			function(jsonData)	//successful callback function
			{
			longitude=jsonData["longitude"];			//get longitude from JSON data
			latitude=jsonData["latitude"];			//get latitude from JSON data
			// *** Add JS code to update h1 on page to show city name
			//alert("Add JS to show city name on page.\nThere is a h1 inside the section of weather details.");
			$("#cityWeather h1").html(jsonData["name"]+" Weather");
			showCityWeather(longitude,latitude);
			}
		);
} //end function

function deleteCity(id)
{
var url=baseURL+"/city/"+id;				//URL pattern of delete service
var settings={type:"DELETE"};	//options to the $.ajax(...) function call

$.ajax(url,settings);
} //end function

function showCityWeather(longitude,latitude)
{
var url="http://api.openweathermap.org/data/2.5/weather";		//URL of OpenWeatherMap service

// Compose request parameters as a map
// The following parameters (in the request) are defined in the OpenWeatherMap API.
var data={	"lat":latitude,		//latitude
			"lon":longitude,		//longitude
			"cnt":1,				//data count to return
			"units":"metric",		//unit of result
			"appid": openWeatherAPIKey
		};

//
// Retrieve weather data from OpenWeatherMap web service.
// When the JSON data come back, update information on the web page.
//
$.getJSON(	url,
			data,
			function(reply)
			{
			var weather=reply["weather"][0]["main"];
			var icon=reply["weather"][0]["icon"];
			var temp=reply["main"]["temp"];
			$("#cityWeather img").attr("src","http://openweathermap.org/img/w/"+icon+".png");
			$("#cityWeather span").html(temp+"C");
			} //end callback function
		); //end Ajax call
} //end function

//
//create map in a given division, given its centre coordinates
//the map is returned as it is need to place the marker
//
function makeMap(divId,zoomLevel,longitude,latitude)
{
var location=L.latLng(longitude,latitude);		//create location
var map=L.map(divId).setView(location,zoomLevel);	//put map into division
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token='+mapBoxApiKey,
		{attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
		maxZoom: 18,
		id: 'mapbox.streets',
		accessToken: mapBoxApiKey}
		).addTo(map);
return map;	//return map object
} //end function

//
//create a marker on a map
//the marker is returned as we need to get its position later
//
function makeMarker(map,longitude,latitude)
{
var location=L.latLng({lon:longitude,lat:latitude});	//create marker at given position
var marker=L.marker(location,{draggable:true});			//make a draggable marker
marker.addTo(map);										//add marker to map	
return marker;				//return marker object
} //end function
