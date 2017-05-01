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
function nearby_list(dis,type,lat,long,cb){
	const connection = getConnection();
	let getresult = [];
	let sql = 'SELECT * FROM `restaurant_profile`  ';
	connection.query( sql , (err,results) =>{
		if(err){
			cb(err)
			return;
		}
		debugger;
		 getresult = calculateNearby(dis,type,lat,long, results);

		cb(null, getresult);
	});
	connection.end();

};
function calculateNearby(dis,type,lat,long, results){
	
	let list = [];
	let typef = type.split(",");
	for(let i = 0; i < results.length; i++)
	{
		let have = false;
		if(results[i].type_food != null){
			let check_latlo = results[i].location.split(",");
			let check_type = results[i].type_food.split(",");
			let distance =	getDistanceFromLatLonInKm(check_latlo[0],check_latlo[1],lat,long);
			if(distance < dis)
			{

					
				if(type.localeCompare("all") != 0){
				for(let j = 0; j < typef.length ; j++){
					for(let k = 0 ; k < check_type.length; k ++){
						let check = typef[j].localeCompare(check_type[k])
						let y=  typef[j];
						 if(check == 0){
						 	have = true;
						 }
					}
				
				}
			}
			else{
				list.push(results[i]);
			}

			if(have == true)	
				list.push(results[i]);
			}
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
function  getdetail_resid (group_resid, cb) {
  const connection = getConnection();
  if(group_resid != null){
	const userid = group_resid.split(",");
	let sql = 'SELECT * FROM `restaurant_profile`  WHERE  resid IN (';
	for(let i = 0; i< userid.length; i++){
		sql = sql + '\'' + userid[i] + '\'' ;
		let check = i + 1;
		if(check != userid.length)
		{
			sql = sql + ',' ;
		}
	}
	sql = sql + ') ;' ; 
  connection.query(
    sql , (err, results) => {
      if (err) {
        cb(err);
        return;
      }
      cb(null, results);
    });

  connection.end();
}else{
	 cb(err);
     return;
}
};

function  search_name (res_name, cb) {
  const connection = getConnection();
	let sql = 'SELECT * FROM `restaurant_profile`  WHERE  name = ' + '\''+ res_name + '\''  ;
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

function  get_favorite (userid, cb) {
  const connection = getConnection();
	let sql = 'SELECT * FROM `restaurant_profile`  WHERE  userid = ' + '\''+ userid + '\''  ;
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

module.exports = {
 nearby_list:  nearby_list,
 all_restaurant : all_restaurant,
 top_list : top_list,
 recommend_list : recommend_list,
 getdetail_resid : getdetail_resid,
 search_name : search_name,
 get_favorite : get_favorite
};
