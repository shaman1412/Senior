'use strict'


const extend = require('lodash').assign;
const mysql = require('mysql');
const config = require('../config');

function getConnection () {
  const options = {
    user: config.get('MYSQL_USER'),
    password: config.get('MYSQL_PASSWORD'),
    database: 'faff'
  };

  if (config.get('INSTANCE_CONNECTION_NAME') && config.get('NODE_ENV') === 'production') {
    options.socketPath = `/cloudsql/${config.get('INSTANCE_CONNECTION_NAME')}`;
  }

  return mysql.createConnection(options);
}
////////////////////////////////////////////// Recommend list /////////////////////////////////////////////////////////////////////
function  recommend_list (type, cb) {
  const connection = getConnection();
const type_split = type.split(",");
let sql = 'SELECT * FROM `restaurant_profile`  WHERE  type_food IN (';
for(let i = 0; i< type_split.length; i++){
	sql = sql + type_split[i] ;
	let check = i + 1;
	if(check != type_split.length)
	{
		sql = sql + ',' ;
	}
}
sql = sql + ') ORDER BY create_time DESC;' ; 
debugger;
  connection.query(
    sql , (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      cb(null, results);
    });
  connection.end();
};
/////////////////////////////////////////////// nearby list ///////////////////////////////////////////////////////////////////////////
function nearby_list(lat,long,cb){
	const connection = getConnection();
	let getresult = [];
	let sql = 'SELECT * FROM `restaurant_profile`  ';
	connection.query( sql , (err,results) =>{
		if(err){
			cb(err)
			return;
		}
		debugger;
		 getresult = calculateNearby(lat,long, results);

		cb(null, getresult);
	});
	connection.end();

};
function calculateNearby(lat,long, results){
	
	let list = [];
	for(let i = 0; i < results.length; i++)
	{
		let check_latlo = results[i].location.split(",");
		let distance =	getDistanceFromLatLonInKm(check_latlo[0],check_latlo[1],lat,long);
		if(distance < 10)
		{
			list.push(results[i]);
		}
	}
	debugger;
	return list;
};

function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
  var R = 6371; // Radius of the earth in km
  var dLat = deg2rad(lat2-lat1);  // deg2rad below
  var dLon = deg2rad(lon2-lon1); 
  var a = 
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
    Math.sin(dLon/2) * Math.sin(dLon/2)
    ; 
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  var d = R * c; // Distance in km
  return d;
};

function deg2rad(deg) {
  return deg * (Math.PI/180)
};
///////////////////////////////////////////////  Top restaurant //////////////////////////////////////////////////////////
function top_list(cb){
	const connection = getConnection();
	connection.query('SELECT * FROM `restaurant_profile`  ORDER BY  `score` DESC' , (err , results ) =>{
		if(err){
			cb(err);
			return;
		}
		cb(null,results);
	})
	connection.end();
};
//////////////////////////////////////////////   All restaurant  /////////////////////////////////////////////////////////
function all_restaurant(cb){
	const connection = getConnection();
	connection.query('SELECT * FROM  `restaurant_profile`  ', (err , results) =>{
		if(err){
			cb(err);
			return;
		}
		cb(null,results);
	})
	connection.end();
}



module.exports = {
 nearby_list:  nearby_list,
 all_restaurant : all_restaurant,
 top_list : top_list,
 recommend_list : recommend_list
};
