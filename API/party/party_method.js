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

function  getdetail_userid (group_userid, cb) {
  const connection = getConnection();
const userid = group_userid.split(",");
let sql = 'SELECT * FROM `user_profile`  WHERE  userid IN (';
for(let i = 0; i< userid.length; i++){
	sql = sql + '\'' + userid[i] + '\'' ;
	let check = i + 1;
	if(check != userid.length)
	{
		sql = sql + ',' ;
	}
}
sql = sql + ') ;' ; 
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



module.exports = {
getdetail_userid:  getdetail_userid
};